package ru.glebov.notification_microservice.service;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@EnableKafka
@Service
public class NotificationService {

    @KafkaListener(topics="sent_orders")
    public void orderListener(String order){
        System.out.println("Заказ доставлен: " + order);
    }
}
