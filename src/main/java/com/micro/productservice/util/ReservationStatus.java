package com.micro.productservice.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatus {

    ITEM_RESERVED_SUCCESSFULLY(200, "Item reserved successfully"),
    ITEM_RESERVATION_FAILED(401, "Item Reservation is failed"),
    ITEM_RELEASED_SUCCESSFULLY(201, "item released successfully");

    private final Integer code;
    private final String message;
}
