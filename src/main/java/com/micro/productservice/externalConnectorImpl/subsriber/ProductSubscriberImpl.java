package com.micro.productservice.externalConnectorImpl.subsriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.productservice.service.ProductService;
import com.micro.productservice.util.externalConnectorMessage.subscriber.ItemReleaseRequest;
import com.micro.productservice.util.externalConnectorMessage.subscriber.ProductReservationRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductSubscriberImpl {

    @Autowired
    private ProductService productService;


    @KafkaListener(topics = "init-item-reservation")
    public void initiateItemReservationMessage(ConsumerRecord<String, String> record) {
        System.out.println("Received message: " + record.value());
        ObjectMapper ob = new ObjectMapper();

        try {
            ProductReservationRequest productReservationRequest = ob.readValue(record.value(), ProductReservationRequest.class);
            productService.reserveProduct(productReservationRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "init-product-release")
    public void releaseItemMessage(ConsumerRecord<String, String> record) {
        System.out.println("Received message: " + record.value());
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ItemReleaseRequest itemReleaseRequestObject = objectMapper.readValue(record.value(), ItemReleaseRequest.class);
            productService.releaseReservedItems(itemReleaseRequestObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
