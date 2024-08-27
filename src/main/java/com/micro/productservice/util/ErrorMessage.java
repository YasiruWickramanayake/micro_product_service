package com.micro.productservice.util;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    PRODUCT_IS_NOT_IN_DB(501, "This item does not exist in DB"),
    RESERVED_ITEM_NOT_IN_DB(502, "Reserved Items not in DB"),
    RESERVED_ITEM_IS_NOT_ENOUGH(503, "Reserved Item is not enough in inventory");

    private final Integer errorCode;
    private final String errorMessage;
}
