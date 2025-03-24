package ru.glebov.jsonview.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import ru.glebov.jsonview.model.Order;
import ru.glebov.jsonview.model.OrderStatus;
import ru.glebov.jsonview.model.User;
import ru.glebov.jsonview.repository.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    static final String REST_URL = "/api/v1/user";

    User testUser;

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("email@email.ru");
        user.setName("Test user");
        List<Order> list = new ArrayList<>();
        Order order = new Order();
        order.setOrderAmount(new BigDecimal("123.45"));
        order.setOrderStatus(OrderStatus.DONE);
        order.getGoods().add("Good1");
        order.getGoods().add("Good2");
        list.add(order);
        order = new Order();
        order.setOrderAmount(new BigDecimal("300.3"));
        order.setOrderStatus(OrderStatus.NEW);
        order.getGoods().add("Good3");
        order.getGoods().add("Good4");
        list.add(order);
        user.setOrderList(list);
        testUser = userRepository.save(user);
    }

    @Test
    void getUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test user"))
                .andExpect(jsonPath("$.orderList", hasSize(2)))
                .andDo(print());

    }

    @Test
    void getAll() throws Exception {
        User user = new User();
        user.setName("Second user");
        user.setEmail("second@email.ru");
        userRepository.save(user);
        mvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].orderList").doesNotExist())
                .andExpect(jsonPath("$[1].orderList").doesNotExist())
                .andDo(print());
    }

    @Test
    void save() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();
        user.setName("Second user");
        user.setEmail("second@email.ru");
        Order order = new Order();
        order.setOrderAmount(new BigDecimal("111.11"));
        order.setOrderStatus(OrderStatus.DONE);
        order.getGoods().add("Good6");
        order.getGoods().add("Good7");
        user.getOrderList().add(order);
        String jsonResponse = mvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderList.id")
                .isEqualTo(objectMapper.readValue(jsonResponse, User.class));
    }

    @Test
    void update() throws Exception {
        testUser.setEmail("newEmail@email.ru");
        mvc.perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(testUser)))
                .andExpect(status().isNoContent());
        assertEquals(testUser, userRepository.getReferenceById(testUser.getId()));
    }

    @Test
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + testUser.getId()))
                .andExpect(status().isNoContent());
        assertEquals(0, userRepository.findAll().size());
    }
}