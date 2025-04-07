package ru.glebov.shipping_microservice.service;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@EnableKafka
@Service
public class ShippingService {

    private KafkaTemplate<String, String> kafkaTemplate;

    public ShippingService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics="payed_orders")
    public void orderListener(String order){
        System.out.println(order);
        kafkaTemplate.send("sent_orders", order + ", Заказ отгружен");
    }
}
