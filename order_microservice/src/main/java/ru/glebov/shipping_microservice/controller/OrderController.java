package ru.glebov.shipping_microservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    private KafkaTemplate<String, String> kafkaTemplate;

    public OrderController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/{order}")
    public String order(@PathVariable String order) {
        try {
            log.info("Processing order: {}", order);
            kafkaTemplate.send("new_orders", order);
            System.out.println(order);
            return order;
        } catch (Exception e) {
            log.error("Error processing order: {}", order, e);
            throw new RuntimeException(e);
        }
    }
}
