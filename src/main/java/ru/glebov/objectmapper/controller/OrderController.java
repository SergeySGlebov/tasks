package ru.glebov.objectmapper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Validator;
import org.springframework.web.bind.annotation.*;
import ru.glebov.objectmapper.model.Order;
import ru.glebov.objectmapper.model.Product;
import ru.glebov.objectmapper.repository.OrderRepository;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final Validator validator;

    private final OrderRepository orderRepository;

    private final ObjectMapper objectMapper;

    public OrderController(OrderRepository orderRepository, ObjectMapper objectMapper, Validator validator) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOne(@PathVariable int id) {
        return orderRepository.findById(id).map(body -> {
            try {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(body));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody String json) throws JsonProcessingException {
        Order orderFromJson = objectMapper.readValue(json, Order.class);

        Set<ConstraintViolation<Order>> violations = validator.validate(orderFromJson);
        if (!violations.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (orderFromJson.getCustomer().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        for (Product product : orderFromJson.getProducts()) {
            if (product.getId() == null) {
                return ResponseEntity.badRequest().build();
            }
        }
        Order savedOrder = orderRepository.save(orderFromJson);
        return ResponseEntity.created(URI.create("/api/orders/" + savedOrder.getId())).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(savedOrder));

    }

}

