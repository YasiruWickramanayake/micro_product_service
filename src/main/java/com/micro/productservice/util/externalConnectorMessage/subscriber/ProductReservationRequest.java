package com.micro.productservice.util.externalConnectorMessage.subscriber;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductReservationRequest {
    @JsonProperty(value = "sagaId")
    private String sagaId;
    @JsonProperty(value = "orderItems")
    private List<ReservedProduct> reservedProducts;

}
