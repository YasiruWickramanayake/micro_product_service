package com.micro.productservice.util.externalConnectorMessage.subscriber;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservedProduct {
    @JsonProperty(value = "productId")
    private Integer productId;
    @JsonProperty(value = "quantity")
    private Integer quantity;
    @JsonProperty(value = "amount")
    private Double amount;
}
