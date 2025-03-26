package ru.glebov.spring_data_jdbc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.glebov.spring_data_jdbc.Repository.BookRepository;
import ru.glebov.spring_data_jdbc.model.Book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    static final String REST_URL = "/api/book";
    static final Book HOBBIT = new Book(3, "Хоббит, или Туда и обратно", "Джон Р.Р. Толкин", 1937);

    @Autowired
    MockMvc mvc;

    @Autowired
    BookRepository bookRepository;

    @Test
    void get() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + HOBBIT.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Хоббит, или Туда и обратно"))
                .andExpect(jsonPath("$.author").value("Джон Р.Р. Толкин"))
                .andDo(print());
    }

    @Test
    void save() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Book neznaika = new Book("Приключения Незнайки и его друзей", "Николай Носов", 1954);
        String jsonResponse = mvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(neznaika)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(neznaika)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(objectMapper.readValue(jsonResponse, Book.class));
    }

    @Test
    void update() throws Exception {
        HOBBIT.setPublicationYear(2004);
        mvc.perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(HOBBIT)))
                .andExpect(status().isNoContent());
        assertEquals(HOBBIT, bookRepository.get(HOBBIT.getId()));
    }

    @Test
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + HOBBIT.getId()))
                .andExpect(status().isNoContent());
        assertNull(bookRepository.get(HOBBIT.getId()));
    }
}