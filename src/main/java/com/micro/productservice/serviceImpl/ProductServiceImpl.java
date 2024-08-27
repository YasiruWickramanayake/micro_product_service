package com.micro.productservice.serviceImpl;

import com.micro.productservice.dao.ProductRepository;
import com.micro.productservice.dao.ReservedItemRepository;
import com.micro.productservice.util.externalConnectorMessage.subscriber.ItemReleaseRequest;
import com.micro.productservice.util.externalConnectorMessage.subscriber.ProductReservationRequest;
import com.micro.productservice.entity.Product;
import com.micro.productservice.entity.ReservedItem;
import com.micro.productservice.exception.ProductException;
import com.micro.productservice.exception.ProductOptimisticLockingException;
import com.micro.productservice.externalConnectors.publisher.ProductPublisherManager;
import com.micro.productservice.service.ProductService;
import com.micro.productservice.util.ErrorMessage;
import com.micro.productservice.util.ReservationStatus;
import com.micro.productservice.util.externalConnectorMessage.common.ItemReservationPublisherMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReservedItemRepository reservedItemRepository;

    @Autowired
    private ProductPublisherManager productPublisherManager;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void reserveProduct(ProductReservationRequest productReservationRequest) {
        try{
        productReservationRequest.getReservedProducts().forEach(reservedProduct -> {
            Product product = getProductById(reservedProduct.getProductId());
            if(isAvailableAmountToBlock(product.getAvailableQuantity(),reservedProduct.getQuantity())){
                updateProductEntity(product,
                        reservedProduct.getQuantity(),
                        productReservationRequest.getSagaId(),
                        reservedProduct.getAmount());
                saveProduct(product);
            }
        });

        productPublisherManager.publishMessage(
                this.gerenateItemReservationPublisherMessage(productReservationRequest.getSagaId(),
                        ReservationStatus.ITEM_RESERVED_SUCCESSFULLY.getCode()));
        }catch (ObjectOptimisticLockingFailureException ex){
                // when record is update shortly. when
                this.saveUnReserveProduct(productReservationRequest);

        } catch (RuntimeException ex){
            // pass details to the product reservation fail message queue
            productPublisherManager.publishMessage(
                    this.gerenateItemReservationPublisherMessage(productReservationRequest.getSagaId(),
                            ReservationStatus.ITEM_RESERVATION_FAILED.getCode()));

        }
    }

    private void saveUnReserveProduct(ProductReservationRequest productReservationRequest) {

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void releaseReservedItems(ItemReleaseRequest itemReleaseRequest) {
        try {
            // get all reserved items by saga id
            List<ReservedItem> allReservedItemsBySagaId = getAllReservedItemsBySagaId(itemReleaseRequest.getSagaId());
            itemReleaseRequest.getReservedProducts().forEach(reservedProduct -> {
                ReservedItem availableReservedItem = allReservedItemsBySagaId.stream()
                        .filter(reservedItem ->
                                reservedItem.getProduct().getProductId().equals(reservedProduct.getProductId()))
                        .findFirst()
                        .orElseThrow(() -> new ProductException(ErrorMessage.RESERVED_ITEM_NOT_IN_DB.getErrorCode(),
                                ErrorMessage.RESERVED_ITEM_NOT_IN_DB.getErrorMessage()));

                updateReservedItem(availableReservedItem);
                reservedItemRepository.save(availableReservedItem);
            });
            productPublisherManager.publishMessage(
                    this.gerenateItemReservationPublisherMessage(itemReleaseRequest.getSagaId(),
                            ReservationStatus.ITEM_RELEASED_SUCCESSFULLY.getCode()));
        }catch (ProductOptimisticLockingException ex){
            // record is updated after you take the details
            this.saveUnReleasedProduct(itemReleaseRequest);
        }
    }

    private void saveUnReleasedProduct(ItemReleaseRequest itemReleaseRequest) {

    }


    private Product getProductById(Integer productId){
       return productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductException(ErrorMessage.PRODUCT_IS_NOT_IN_DB.getErrorCode(),
                        ErrorMessage.PRODUCT_IS_NOT_IN_DB.getErrorMessage()));
    }

    private List<ReservedItem> getAllReservedItemsBySagaId(String sagaId){
        return reservedItemRepository
                .findAllBySagaId(sagaId)
                .orElseThrow(() -> new ProductException(ErrorMessage.RESERVED_ITEM_NOT_IN_DB.getErrorCode(),
                        ErrorMessage.RESERVED_ITEM_NOT_IN_DB.getErrorMessage()));
    }

    private boolean isAvailableAmountToBlock(Integer availableAmount, Integer reserveAmount){
        if(availableAmount < reserveAmount){
            throw new ProductException(ErrorMessage.RESERVED_ITEM_IS_NOT_ENOUGH.getErrorCode(),
                    ErrorMessage.RESERVED_ITEM_IS_NOT_ENOUGH.getErrorMessage());
        }
        return true;
    }

    private void updateProductEntity(Product product, Integer reservedQuantity, String sagaId, Double amount) {
        product.setBlockQuantity(product.getBlockQuantity() + reservedQuantity);
        product.setAvailableQuantity(product.getAvailableQuantity() - reservedQuantity);

        product.getReservedItems().add(this.generateProductReservation(sagaId, product, reservedQuantity, amount));
    }

    private void updateReservedItem(ReservedItem reservedItem){
        reservedItem.getProduct().setAvailableQuantity(reservedItem.getProduct().getAvailableQuantity() + reservedItem.getQuantity());
        reservedItem.getProduct().setBlockQuantity(reservedItem.getProduct().getBlockQuantity() - reservedItem.getQuantity());
        reservedItem.setReservedStatus(ReservationStatus.ITEM_RELEASED_SUCCESSFULLY.getCode());
        reservedItem.setNarration(ReservationStatus.ITEM_RELEASED_SUCCESSFULLY.getMessage());
    }

    private ReservedItem generateProductReservation(String sagaId,
                                                    Product product,
                                                    Integer quantity,
                                                    Double amount){
        return ReservedItem.builder()
                .product(product)
                .sagaId(sagaId)
                .quantity(quantity)
                .amount(amount)
                .reservedStatus(ReservationStatus.ITEM_RESERVED_SUCCESSFULLY.getCode())
                .narration(ReservationStatus.ITEM_RESERVED_SUCCESSFULLY.getMessage())
                .build();
    }

    private synchronized void saveProduct(Product product){
        try{
            productRepository.save(product);
        }catch (ObjectOptimisticLockingFailureException ex){
            product = null;
            throw new ProductOptimisticLockingException(400, "this record is updated by another please try again");
        }
    }

    private ItemReservationPublisherMessage gerenateItemReservationPublisherMessage(String sagaId,
                                                                                    Integer reservationStatus){
        return ItemReservationPublisherMessage.builder()
                .sagaId(sagaId)
                .reservationStatus(reservationStatus)
                .build();
    }

}
