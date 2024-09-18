package com.micro.productservice.externalConnectorImpl.subsriber;


import com.micro.productService.*;
import com.micro.productservice.service.ProductService;
import com.micro.productservice.util.externalConnectorMessage.subscriber.ItemReleaseRequest;
import com.micro.productservice.util.externalConnectorMessage.subscriber.ProductReservationRequest;
import com.micro.productservice.util.externalConnectorMessage.subscriber.ReservedProduct;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class ProductSubscriberImpl extends ProductServiceGrpc.ProductServiceImplBase {

    @Autowired
    private ProductService productService;

    @Override
    public void initiateProductReservation(ProductReservationInitRequest request, StreamObserver<com.micro.productService.ProductReservationInitResponse> responseObserver) {
        System.out.println("Received message: " + request);
        try{
            ProductReservationRequest productReservationRequest = ProductReservationRequest.builder()
                    .sagaId(request.getSagaId())
                    .reservedProducts(mappingReservedProduct(request.getOrderItemsList()))
                    .build();
            productService.reserveProduct(productReservationRequest);
            createResponse(responseObserver, request.getSagaId());
        }catch (Exception ex){

        }
    }

    @Override
    public void releaseReservedProduct(ProductReleaseInitRequest request, StreamObserver<ProductReleaseInitResponse> responseObserver) {
       try{
           ItemReleaseRequest itemReleaseRequest = ItemReleaseRequest.builder()
                   .sagaId(request.getSagaId())
                   .build();
           productService.releaseReservedItems(itemReleaseRequest);
           responseObserver.onNext(ProductReleaseInitResponse.newBuilder().setStatus(true).setMessage("message received").build());
           responseObserver.onCompleted();
       }catch (RuntimeException ex){
           responseObserver.onNext(ProductReleaseInitResponse.newBuilder().setStatus(false).setMessage("message received failed").build());
           responseObserver.onCompleted();
       }
    }


    //    @KafkaListener(topics = "init-product-release")
//    public void releaseItemMessage(ConsumerRecord<String, String> record) {
//        System.out.println("Received message: " + record.value());
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            ItemReleaseRequest itemReleaseRequestObject = objectMapper.readValue(record.value(), ItemReleaseRequest.class);
//            productService.releaseReservedItems(itemReleaseRequestObject);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private List<ReservedProduct> mappingReservedProduct(List<ReservedOrderItem> orderItemsList) {
        return orderItemsList.stream().map(reservedOrderItem ->
                ReservedProduct.builder()
                        .productId(reservedOrderItem.getProductId())
                        .amount(reservedOrderItem.getAmount())
                        .quantity(reservedOrderItem.getQuantity()).build())
                .collect(Collectors.toList());
    }

    private void createResponse(StreamObserver<ProductReservationInitResponse> responseObserver, String sagaId){
         responseObserver.onNext(ProductReservationInitResponse.newBuilder()
                .setSagaId(sagaId).build());
         responseObserver.onCompleted();
    }
}
