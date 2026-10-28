package com.mamlayev.studentapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamlayev.studentapi.dto.StudentRequest;
import com.mamlayev.studentapi.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests of the Student API (author: Mukhammed Mamlayev).
 */
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    private String json(StudentRequest request) throws Exception {
        return objectMapper.writeValueAsString(request);
    }

    private long createStudent(String firstName, String lastName, String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new StudentRequest(firstName, lastName, email, "SE-2401", 2))))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    void createsStudentWithAuthorName() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new StudentRequest("Mukhammed", "Mamlayev",
                                "mukhammed.mamlayev@example.com", "SE-2401", 2))))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value("Mukhammed"))
                .andExpect(jsonPath("$.lastName").value("Mamlayev"));
    }

    @Test
    void returnsAllStudents() throws Exception {
        createStudent("Mukhammed", "Mamlayev", "mukhammed.mamlayev@example.com");
        createStudent("Aigerim", "Nurlanova", "aigerim.nurlanova@example.com");

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void returnsStudentById() throws Exception {
        long id = createStudent("Mukhammed", "Mamlayev", "mukhammed.mamlayev@example.com");

        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.email").value("mukhammed.mamlayev@example.com"));
    }

    @Test
    void updatesStudent() throws Exception {
        long id = createStudent("Mukhammed", "Mamlayev", "mukhammed.mamlayev@example.com");

        mockMvc.perform(put("/api/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new StudentRequest("Mukhammed", "Mamlayev",
                                "m.mamlayev@example.com", "SE-2402", 3))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("m.mamlayev@example.com"))
                .andExpect(jsonPath("$.groupName").value("SE-2402"))
                .andExpect(jsonPath("$.course").value(3));
    }

    @Test
    void updateOfNonexistentStudentReturns404() throws Exception {
        mockMvc.perform(put("/api/students/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new StudentRequest("Mukhammed", "Mamlayev",
                                "mukhammed.mamlayev@example.com", null, 1))))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateRejectsEmailOfAnotherStudent() throws Exception {
        long id = createStudent("Mukhammed", "Mamlayev", "mukhammed.mamlayev@example.com");
        createStudent("Aigerim", "Nurlanova", "aigerim.nurlanova@example.com");

        mockMvc.perform(put("/api/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new StudentRequest("Mukhammed", "Mamlayev",
                                "aigerim.nurlanova@example.com", null, 1))))
                .andExpect(status().isConflict());
    }

    @Test
    void deletesStudent() throws Exception {
        long id = createStudent("Mukhammed", "Mamlayev", "mukhammed.mamlayev@example.com");

        mockMvc.perform(delete("/api/students/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void returns404ForNonexistentStudent() throws Exception {
        mockMvc.perform(get("/api/students/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(containsString("999999")));
    }

    @Test
    void rejectsInvalidStudent() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new StudentRequest("", "Mamlayev", "not-an-email", null, 9))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.firstName").exists())
                .andExpect(jsonPath("$.details.email").exists())
                .andExpect(jsonPath("$.details.course").exists());
    }

    @Test
    void rejectsDuplicateEmail() throws Exception {
        createStudent("Mukhammed", "Mamlayev", "mukhammed.mamlayev@example.com");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(new StudentRequest("Mukha", "Mamlayev",
                                "MUKHAMMED.MAMLAYEV@example.com", null, 1))))
                .andExpect(status().isConflict());
    }
}
