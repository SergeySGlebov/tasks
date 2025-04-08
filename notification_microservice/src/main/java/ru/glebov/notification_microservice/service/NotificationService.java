package ru.glebov.notification_microservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@EnableKafka
@Service
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @KafkaListener(topics="sent_orders")
    public void orderListener(String order, Acknowledgment ack){

        try {
            log.info("Processing order: {}", order);
            System.out.println("Заказ доставлен: " + order);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing order: {}", order, e);
            throw e;
        }

    }
}
