package com.micro.productservice.externalConnectorImpl.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.micro.productservice.externalConnectors.publisher.ProductPublisher;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReleaseSuccessMessage;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReservationFailMessage;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReservationSuccessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductPublisherImpl implements ProductPublisher {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void itemReservationSuccessMessagePublish(ItemReservationSuccessMessage itemReservationSuccessMessage) {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = null;
            json = ow.writeValueAsString(itemReservationSuccessMessage);
            kafkaTemplate.send("item-reservation-success", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void itemReservationFailMessagePublish(ItemReservationFailMessage itemReservationFailMessage) {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = null;
            json = ow.writeValueAsString(itemReservationFailMessage);
            kafkaTemplate.send("item-reservation-fail", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void itemReleaseSuccessMessagePublish(ItemReleaseSuccessMessage itemReleaseSuccessMessage) {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = null;
            json = ow.writeValueAsString(itemReleaseSuccessMessage);
            kafkaTemplate.send("item-release-success", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
