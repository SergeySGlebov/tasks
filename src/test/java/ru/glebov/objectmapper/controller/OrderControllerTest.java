package ru.glebov.objectmapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.glebov.objectmapper.model.Customer;
import ru.glebov.objectmapper.model.Order;
import ru.glebov.objectmapper.model.OrderStatus;
import ru.glebov.objectmapper.model.Product;
import ru.glebov.objectmapper.repository.CustomerRepository;
import ru.glebov.objectmapper.repository.OrderRepository;
import ru.glebov.objectmapper.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    static final String REST_URL = "/api/orders";

    Order testOrder;

    @Autowired
    MockMvc mvc;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setOrderStatus(OrderStatus.NEW);
        testOrder.setShippingAddress("Адрес доставки какой-то");
        testOrder.setTotalPrice(BigDecimal.valueOf(140));
        Customer customer = new Customer();
        customer.setFirstName("Иван");
        customer.setLastName("Иванов");
        customer.setContactNumber("+7-777-555-55-55");
        customer.setEmail("email@email.com");
        customer = customerRepository.save(customer);
        Product product2 = new Product();
        product2.setDescription("Вкусное яблоко из Молдавии");
        product2.setName("Яблоко");
        product2.setPrice(BigDecimal.valueOf(12));
        product2.setQuantityStock(4);
        product2 = productRepository.save(product2);
        Product product3 = new Product();
        product2.setDescription("Кислый лимон неизвестно откуда");
        product2.setName("Лимон");
        product2.setPrice(BigDecimal.valueOf(46));
        product2.setQuantityStock(2);
        product3 = productRepository.save(product3);
        testOrder.setCustomer(customer);
        testOrder.setProducts(Set.of(product2, product3));
    }

    @Test
    void getOne() throws Exception {
        testOrder = orderRepository.save(testOrder);
        ResultActions action = mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print());
        Order fromController = objectMapper.readValue(action.andReturn().getResponse().getContentAsString(), Order.class);
        assertThat(testOrder)
                .usingRecursiveComparison()
                .isEqualTo(fromController);
    }

    @Test
    void save() throws Exception {
        String jsonResponse = mvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrder)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(testOrder)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(objectMapper.readValue(jsonResponse, Order.class));
    }
}