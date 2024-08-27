package com.micro.productservice.externalConnectors.publisher;

import com.micro.productservice.util.externalConnectorMessage.common.ItemReservationPublisherMessage;

public interface ProductPublisherManager {
    public void publishMessage(ItemReservationPublisherMessage itemReservationPublisherMessage);
}
