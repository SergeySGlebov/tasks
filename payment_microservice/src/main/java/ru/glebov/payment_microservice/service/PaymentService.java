package ru.glebov.payment_microservice.service;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@EnableKafka
@Service
public class PaymentService {

    private KafkaTemplate<String, String> kafkaTemplate;

    public PaymentService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics="new_orders")
    public void orderListener(String order){
        System.out.println(order);
        kafkaTemplate.send("payed_orders", order + ", Заказ оплачен");
    }

}
