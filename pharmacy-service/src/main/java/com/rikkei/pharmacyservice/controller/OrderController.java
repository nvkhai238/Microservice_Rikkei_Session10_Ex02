package com.rikkei.pharmacyservice.controller;

import com.rikkei.pharmacyservice.dto.CreateOrderRequest;
import com.rikkei.pharmacyservice.dto.OrderAcceptedResponse;
import com.rikkei.pharmacyservice.service.OrderProducerService;
import com.rikkei.pharmacyservice.service.OrderPublishException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderProducerService orderProducerService;

    public OrderController(OrderProducerService orderProducerService) {
        this.orderProducerService = orderProducerService;
    }

    @PostMapping
    public ResponseEntity<OrderAcceptedResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderProducerService.placeOrder(request));
    }

    @ExceptionHandler(OrderPublishException.class)
    public ResponseEntity<Map<String, String>> handlePublishFailure(OrderPublishException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("message", exception.getMessage()));
    }
}
