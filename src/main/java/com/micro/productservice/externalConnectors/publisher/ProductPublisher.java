package com.micro.productservice.externalConnectors.publisher;

import com.micro.productService.ProductReleaseSuccessRequest;
import com.micro.productService.ProductReserveFailRequest;
import com.micro.productService.ProductReserveSuccessRequest;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReleaseSuccessMessage;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReservationFailMessage;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReservationSuccessMessage;

public interface ProductPublisher {

    public void itemReservationSuccessMessagePublish(ProductReserveSuccessRequest productReserveSuccessRequest);
    public void itemReservationFailMessagePublish(ProductReserveFailRequest productReserveFailRequest);

    public void itemReleaseSuccessMessagePublish(ProductReleaseSuccessRequest productReleaseSuccessRequest);
}
