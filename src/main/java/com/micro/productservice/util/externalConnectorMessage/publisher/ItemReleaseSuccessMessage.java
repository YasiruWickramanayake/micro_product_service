package com.micro.productservice.util.externalConnectorMessage.publisher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemReleaseSuccessMessage {
    private String sagaId;
}
