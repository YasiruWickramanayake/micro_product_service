package com.micro.productservice.util.externalConnectorMessage.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ItemReservationPublisherMessage {
    private String sagaId;
    private Integer reservationStatus;
}
