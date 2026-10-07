package com.rikkei.pharmacyservice.service;

import com.rikkei.pharmacyservice.dto.CreateOrderRequest;
import com.rikkei.pharmacyservice.dto.OrderAcceptedResponse;
import com.rikkei.pharmacyservice.event.OrderEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OrderProducerService {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final String topic;

    public OrderProducerService(KafkaTemplate<String, OrderEvent> kafkaTemplate,
                                @Value("${pharmacy.kafka.stock-topic:medicine-stock-events}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public OrderAcceptedResponse placeOrder(CreateOrderRequest request) {
        OrderEvent event = new OrderEvent(UUID.randomUUID().toString(), request.medicineId(),
                request.quantity(), Instant.now());
        try {
            SendResult<String, OrderEvent> result = kafkaTemplate
                    .send(topic, event.medicineId(), event)
                    .get(10, TimeUnit.SECONDS);
            return new OrderAcceptedResponse(event.orderId(), event.medicineId(), event.quantity(),
                    result.getRecordMetadata().topic(), result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OrderPublishException("Sending the order event was interrupted", e);
        } catch (Exception e) {
            throw new OrderPublishException("Could not publish the order event to Kafka", e);
        }
    }
}
