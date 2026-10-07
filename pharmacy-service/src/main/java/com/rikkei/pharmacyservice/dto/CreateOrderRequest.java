package com.rikkei.pharmacyservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(@NotBlank String medicineId, @Positive int quantity) { }
