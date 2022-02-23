package org.example.movieservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SearchMovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void canGetByTitle() throws Exception {
        mockMvc.perform(get("/api/v1/movie?username=ABC123&title=harry"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Harry Potter and the Deathly Hallows: Part 2")));
        mockMvc.perform(get("/api/v1/movie?username=ABC123&title=harry"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Harry Potter and the Deathly Hallows: Part 2")));
        mockMvc.perform(get("/api/v1/movie?username=ABC123&title=harry"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Harry Potter and the Deathly Hallows: Part 2")));
    }

    @Test
    void canGetByTitleAndYear() throws Exception {
        mockMvc.perform(get("/api/v1/movie?username=ABC123&title=harry&year=2011"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Harry Potter and the Deathly Hallows: Part 2")));
    }

    @Test
    void shouldGetNothingIfMovieNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/movie?username=ABC123&title=harryar5etrymbhgfcvbhjgv"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is("False")))
                .andExpect(jsonPath("$.error", is("MOVIE_NOT_FOUND")));

    }

    @Test
    void shouldReturnErrorIfUsernameNotPresented() throws Exception {
        mockMvc.perform(get("/api/v1/movie?title=harryar5etrymbhgfcvbhjgv"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.response", is("False")))
                .andExpect(jsonPath("$.error", is("USERNAME_INVALID")));

    }

    @Test
    void shouldReturnErrorIfTitleNotPresented() throws Exception {
        mockMvc.perform(get("/api/v1/movie?username=harryar5etrymbhgfcvbhjgv"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.response", is("False")))
                .andExpect(jsonPath("$.error", is("TITLE_INVALID")));

    }
}