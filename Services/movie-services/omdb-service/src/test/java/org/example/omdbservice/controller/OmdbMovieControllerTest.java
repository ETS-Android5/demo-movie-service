package org.example.omdbservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OmdbMovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void canGetByTitle() throws Exception {
        mockMvc.perform(get("/api/v1/omdb-service/harry"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Harry Potter and the Deathly Hallows: Part 2")));
    }

    @Test
    void canGetByTitleAndYear() throws Exception {
        mockMvc.perform(get("/api/v1/omdb-service/harry/2011"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Harry Potter and the Deathly Hallows: Part 2")));
    }

    @Test
    void shouldGetNothingIfMovieNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/omdb-service/harrymbhgfcvbhjgv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is("False")));
    }
}