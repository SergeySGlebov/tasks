package ru.glebov.shipping_microservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@EnableKafka
@Service
public class ShippingService {

    private final Logger log = LoggerFactory.getLogger(ShippingService.class);

    private KafkaTemplate<String, String> kafkaTemplate;

    public ShippingService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payed_orders")
    public void orderListener(String order, Acknowledgment ack) {
        try {
            log.info("Processing order: {}", order);
            System.out.println(order);
            kafkaTemplate.send("sent_orders", order + ", Заказ отгружен");
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing order: {}", order, e);
            throw e;
        }
    }
}
