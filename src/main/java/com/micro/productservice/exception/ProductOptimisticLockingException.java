package com.micro.productservice.exception;

import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductOptimisticLockingException extends OptimisticLockException {
    private Integer errorCode;
    private String message;
}
