package com.micro.productservice.externalConnectors.publisher;

import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReleaseSuccessMessage;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReservationFailMessage;
import com.micro.productservice.util.externalConnectorMessage.publisher.ItemReservationSuccessMessage;

public interface ProductPublisher {

    public void itemReservationSuccessMessagePublish(ItemReservationSuccessMessage itemReservationSuccessMessage);
    public void itemReservationFailMessagePublish(ItemReservationFailMessage itemReservationFailMessage);

    public void itemReleaseSuccessMessagePublish(ItemReleaseSuccessMessage itemReleaseSuccessMessage);
}
