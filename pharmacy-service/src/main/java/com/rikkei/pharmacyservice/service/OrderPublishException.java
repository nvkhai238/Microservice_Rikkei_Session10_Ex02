package com.rikkei.pharmacyservice.service;

public class OrderPublishException extends RuntimeException {
    public OrderPublishException(String message, Throwable cause) {
        super(message, cause);
    }
}
