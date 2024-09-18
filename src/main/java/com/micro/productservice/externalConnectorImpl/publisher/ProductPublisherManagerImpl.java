package com.micro.productservice.externalConnectorImpl.publisher;

import com.micro.productService.ProductReleaseSuccessRequest;
import com.micro.productService.ProductReserveFailRequest;
import com.micro.productService.ProductReserveSuccessRequest;
import com.micro.productservice.externalConnectors.publisher.ProductPublisher;
import com.micro.productservice.externalConnectors.publisher.ProductPublisherManager;
import com.micro.productservice.util.ReservationStatus;
import com.micro.productservice.util.externalConnectorMessage.common.ItemReservationPublisherMessage;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReleaseSuccessMessage;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReservationFailMessage;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReservationSuccessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductPublisherManagerImpl implements ProductPublisherManager {

    @Autowired
    private ProductPublisher productPublisher;

    @Override
    public void publishMessage(ItemReservationPublisherMessage itemReservationPublisherMessage) {
        if (itemReservationPublisherMessage
                .getReservationStatus()
                .equals(ReservationStatus.ITEM_RESERVED_SUCCESSFULLY.getCode())) {

            this.publishItemReservationSuccessMessage(itemReservationPublisherMessage);

        } else if (itemReservationPublisherMessage
                .getReservationStatus()
                .equals(ReservationStatus.ITEM_RESERVATION_FAILED.getCode())) {

            this.publishItemReservationFailMessage(itemReservationPublisherMessage);

        } else if (itemReservationPublisherMessage
                .getReservationStatus()
                .equals(ReservationStatus.ITEM_RELEASED_SUCCESSFULLY.getCode())) {
            this.publishItemReleaseSuccessMessage(itemReservationPublisherMessage);
        }
    }

    private void publishItemReservationSuccessMessage(ItemReservationPublisherMessage itemReservationPublisherMessage) {
        ProductReserveSuccessRequest productReserveSuccessRequest = ProductReserveSuccessRequest.newBuilder()
                .setSagaId(itemReservationPublisherMessage.getSagaId())
                .build();
        productPublisher.itemReservationSuccessMessagePublish(productReserveSuccessRequest);
    }

    private void publishItemReservationFailMessage(ItemReservationPublisherMessage itemReservationPublisherMessage) {
        ProductReserveFailRequest productReserveFailRequest = ProductReserveFailRequest.newBuilder()
                .setSagaId(itemReservationPublisherMessage.getSagaId())
                .build();
        productPublisher.itemReservationFailMessagePublish(productReserveFailRequest);
    }

    private void publishItemReleaseSuccessMessage(ItemReservationPublisherMessage itemReservationPublisherMessage){
        ProductReleaseSuccessRequest productReleaseSuccessRequest = ProductReleaseSuccessRequest.newBuilder()
                .setSagaId(itemReservationPublisherMessage.getSagaId())
                .build();
        productPublisher.itemReleaseSuccessMessagePublish(productReleaseSuccessRequest);
    }


}
