package com.leoric01.hogwarts.controllers;

import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.artifact.ArtifactNotFoundException;
import com.leoric01.hogwarts.services.ArtifactService;
import com.leoric01.hogwarts.system.StatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact(11111L, "Deluminator", "description1","ImagUrl1");
        Artifact a2 = new Artifact(11112L, "Invisibility Cloak", "description2","ImagUrl2");
        Artifact a3 = new Artifact(11113L, "Elder's Wand", "description3","ImagUrl3");
        Artifact a4 = new Artifact(11114L, "Map", "description4","ImagUrl4");
        Artifact a5 = new Artifact(11115L, "Sword of Gryffindor", "description5","ImagUrl5");
        Artifact a6 = new Artifact(11116L, "Resurrection Stone", "description6","ImagUrl6");
        this.artifacts = new ArrayList<>(Arrays.asList(a1,a2,a3,a4,a5,a6));
    }

    @AfterEach
    void tearDown() {
      }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        given(this.artifactService.findById(11111L)).willReturn(this.artifacts.get(0));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/artifacts/11111")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find one Success"))
                .andExpect(jsonPath("$.data.id").value(11111))
                .andExpect(jsonPath("$.data.name").value("Deluminator"));
    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception {
    given(this.artifactService.findById(11111L)).willThrow(new ArtifactNotFoundException(11111L));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/artifacts/11111")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 11111 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}