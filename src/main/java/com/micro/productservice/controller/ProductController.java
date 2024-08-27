package com.micro.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.productservice.util.externalConnectorMessage.subscriber.ItemReleaseRequest;
import com.micro.productservice.util.externalConnectorMessage.subscriber.ProductReservationRequest;
import com.micro.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/reserve-produt-for-order")
    public @ResponseBody ProductReservationRequest reserveProduct(@RequestBody String reserveProduct) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductReservationRequest productReservationRequest = objectMapper.readValue(reserveProduct, ProductReservationRequest.class);
        productService.reserveProduct(productReservationRequest);
        return productReservationRequest;
    }

    @PostMapping("/release-items")
    public String releaseItem(@RequestBody String itemReleaseRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ItemReleaseRequest itemReleaseRequestObject = objectMapper.readValue(itemReleaseRequest, ItemReleaseRequest.class);
        productService.releaseReservedItems(itemReleaseRequestObject);
        return "item release call done";
    }
}
