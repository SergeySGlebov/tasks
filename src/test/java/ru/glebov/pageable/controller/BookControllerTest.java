package ru.glebov.pageable.controller;

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
import ru.glebov.pageable.model.Book;
import ru.glebov.pageable.model.Author;
import ru.glebov.pageable.repository.AuthorRepository;
import ru.glebov.pageable.repository.BookRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    static final String REST_URL = "/api/v1/book";

    Author pushkin;
    Book hobbit;

    @Autowired
    MockMvc mvc;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        pushkin = new Author("Пушкин");
        Author tolkin = new Author("Толкин");
        Author duma = new Author("Дюма");
        pushkin = authorRepository.save(pushkin);
        tolkin = authorRepository.save(tolkin);
        duma = authorRepository.save(duma);
        Book ruslanILudmila = new Book("Руслан и Людмила", pushkin);
        bookRepository.save(ruslanILudmila);
        Book skazkaOZolotomPetushke = new Book("Сказка о золотом петушке", pushkin);
        bookRepository.save(skazkaOZolotomPetushke);
        Book evgeniiOnegin = new Book("Евгений Онегин", pushkin);
        bookRepository.save(evgeniiOnegin);
        Book vlastelinKolec = new Book("Властелин колец", tolkin);
        bookRepository.save(vlastelinKolec);
        hobbit = new Book("Хоббит, туда и обратно", tolkin);
        bookRepository.save(hobbit);
        Book silmarillion = new Book("Сильмариллион", tolkin);
        bookRepository.save(silmarillion);
        Book triMushketera = new Book("Три мушкетера", duma);
        bookRepository.save(triMushketera);
        Book grafMonteCristo = new Book("Граф Монте-Кристо", duma);
        bookRepository.save(grafMonteCristo);
        Book korolevaMargo = new Book("Королева Марго", duma);
        bookRepository.save(korolevaMargo);
    }

    @Test
    void getBook() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + hobbit.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tittle").value("Хоббит, туда и обратно"))
                .andExpect(jsonPath("$.author.name").value("Толкин"))
                .andDo(print());
    }

    @Test
    void getAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(REST_URL + "?page=1&size=3&sort=tittle,asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].tittle").value("Королева Марго"))
                .andExpect(jsonPath("$.content[1].tittle").value("Руслан и Людмила"))
                .andExpect(jsonPath("$.content[2].tittle").value("Сильмариллион"))
                .andDo(print());
    }

    @Test
    void save() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Author nosov = new Author("Носов");
        Book neznaika = new Book("Незнайка", nosov);
        String jsonResponse = mvc.perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(neznaika)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(neznaika)
                .usingRecursiveComparison()
                .ignoringFields("id", "author.id")
                .isEqualTo(objectMapper.readValue(jsonResponse, Book.class));
    }

    @Test
    void update() throws Exception {
        hobbit.setAuthor(pushkin);
        mvc.perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(hobbit)))
                .andExpect(status().isOk());
        assertThat(hobbit)
                .usingRecursiveComparison()
                        .isEqualTo(bookRepository.getReferenceById(hobbit.getId()));
    }

    @Test
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + hobbit.getId()))
                .andExpect(status().isNoContent());
        assertEquals(8, bookRepository.findAll().size());
    }
}