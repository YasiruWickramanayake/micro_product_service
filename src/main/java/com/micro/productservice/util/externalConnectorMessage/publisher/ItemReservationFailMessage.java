package com.micro.productservice.util.externalConnectorMessage.publisher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ItemReservationFailMessage {
    private String sagaId;
}
