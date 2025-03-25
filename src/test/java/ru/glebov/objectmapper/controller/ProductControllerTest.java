package ru.glebov.objectmapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.glebov.objectmapper.model.Product;
import ru.glebov.objectmapper.repository.ProductRepository;

import java.math.BigDecimal;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    static final String REST_URL = "/api/products";

    Product testProduct;

    @Autowired
    MockMvc mvc;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setDescription("Спелый апельсин из Африки");
        testProduct.setName("Апельсин");
        testProduct.setPrice(BigDecimal.valueOf(23));
        testProduct.setQuantityStock(123);
        Product product2 = new Product();
        product2.setDescription("Вкусное яблоко из Молдавии");
        product2.setName("Яблоко");
        product2.setPrice(BigDecimal.valueOf(12));
        product2.setQuantityStock(500);
        Product product3 = new Product();
        product3.setDescription("Кислый лимон неизвестно откуда");
        product3.setName("Лимон");
        product3.setPrice(BigDecimal.valueOf(46));
        product3.setQuantityStock(150);
        testProduct = productRepository.save(testProduct);
        productRepository.save(product2);
        productRepository.save(product3);
    }

    @Test
    void getAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    void getOne() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Апельсин"))
                .andExpect(jsonPath("$.description").value("Спелый апельсин из Африки"))
                .andDo(print());
    }

    @Test
    void create() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Product strawberry = new Product();
        strawberry.setDescription("Вкусная клубника, местная");
        strawberry.setName("Клубника");
        strawberry.setPrice(BigDecimal.valueOf(7));
        strawberry.setQuantityStock(1200);
        String jsonResponse = mvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(strawberry)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(strawberry)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(objectMapper.readValue(jsonResponse, Product.class));
    }

    @Test
    void update() throws Exception {
        testProduct.setQuantityStock(888);
        mvc.perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(testProduct)))
                .andExpect(status().isOk());
        assertThat(testProduct)
                .usingRecursiveComparison()
                .isEqualTo(productRepository.getReferenceById(testProduct.getId()));
    }

    @Test
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + testProduct.getId()))
                .andExpect(status().isNoContent());
        assertEquals(2, productRepository.findAll().size());
    }

    @Test
    void createWithFrongData() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = new Product();
        product.setName("Продукт без описания");
        product.setPrice(BigDecimal.valueOf(2));
        product.setQuantityStock(10);
        mvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))).andExpect(status().isBadRequest());

    }
}