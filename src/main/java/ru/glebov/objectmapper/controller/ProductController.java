package ru.glebov.objectmapper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.glebov.objectmapper.model.Product;
import ru.glebov.objectmapper.repository.ProductRepository;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final Validator validator;

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    public ProductController(Validator validator, ProductRepository productRepository, ObjectMapper objectMapper) {
        this.validator = validator;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<String> getAll() throws JsonProcessingException {
        String response = objectMapper.writeValueAsString(productRepository.findAll());

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOne(@PathVariable Integer id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(opt -> {
            try {
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(opt));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody String json) throws JsonProcessingException {
        Product productFromJson = objectMapper.readValue(json, Product.class);
        Set<ConstraintViolation<Product>> violations = validator.validate(productFromJson);
        if (!violations.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Product product = productRepository.save(productFromJson);

        return ResponseEntity.created(URI.create("/api/products/" + product.getId())).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(product));
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody String json) throws IOException {
        Product productFromJson = objectMapper.readValue(json, Product.class);
        Set<ConstraintViolation<Product>> violations = validator.validate(productFromJson);
        if (!violations.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (productRepository.findById(productFromJson.getId()).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Product updatedProduct = productRepository.save(productFromJson);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            productRepository.delete(product);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
