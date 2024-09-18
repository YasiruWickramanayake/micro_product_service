package com.micro.productservice.externalConnectorImpl.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.micro.productService.*;
import com.micro.productservice.externalConnectors.publisher.ProductPublisher;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReleaseSuccessMessage;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReservationFailMessage;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReservationSuccessMessage;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductPublisherImpl implements ProductPublisher {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GrpcClient("product-reserve-success")
    ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStubSuccessMessage;

    @GrpcClient("product-reserve-fail")
    ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStubFailMessage;

    @GrpcClient("product-release-success")
    ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStubReleaseSuccess;

    @Override
    public void itemReservationSuccessMessagePublish(ProductReserveSuccessRequest productReserveSuccessRequest) {
        try {
            ProductReserveSuccessResponse productReserveSuccessResponse =
                    productServiceBlockingStubSuccessMessage.productReserveSuccess(productReserveSuccessRequest);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void itemReservationFailMessagePublish(ProductReserveFailRequest productReserveFailRequest) {
        try {
            ProductReserveFailResponse productReserveFailResponse =
                    productServiceBlockingStubFailMessage.productReserveFail(productReserveFailRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void itemReleaseSuccessMessagePublish(ProductReleaseSuccessRequest productReleaseSuccessRequest) {
        try {
            ProductReleaseSuccessResponse productReleaseSuccessResponse =
                    productServiceBlockingStubReleaseSuccess.productReleaseSuccess(productReleaseSuccessRequest);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
