package ru.glebov.payment_microservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@EnableKafka
@Service
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private KafkaTemplate<String, String> kafkaTemplate;

    public PaymentService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "new_orders")
    public void orderListener(String order, Acknowledgment ack) {
        try {
            log.info("Processing order: {}", order);
            System.out.println(order);
            kafkaTemplate.send("payed_orders", order + ", Заказ оплачен");
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing order: {}", order, e);
            throw e;
        }
    }
}
