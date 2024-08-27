package com.micro.productservice.service;

import com.micro.productservice.util.externalConnectorMessage.subscriber.ItemReleaseRequest;
import com.micro.productservice.util.externalConnectorMessage.subscriber.ProductReservationRequest;

public interface ProductService {

    public void reserveProduct(ProductReservationRequest productReservationRequest);

    public void releaseReservedItems(ItemReleaseRequest itemReleaseRequest);
}
