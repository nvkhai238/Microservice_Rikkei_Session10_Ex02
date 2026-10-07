package com.rikkei.pharmacyservice.dto;

public record OrderAcceptedResponse(String orderId, String medicineId, int quantity,
                                    String topic, int partition, long offset) { }
