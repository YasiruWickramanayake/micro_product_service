package com.micro.productservice.util.externalConnectorMessage.publisher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ItemReservationSuccessMessage {
    private String sagaId;
}
