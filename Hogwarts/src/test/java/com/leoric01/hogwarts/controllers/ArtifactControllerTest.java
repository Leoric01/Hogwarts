package com.leoric01.hogwarts.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.artifact.ArtifactNotFoundException;
import com.leoric01.hogwarts.models.artifact.dto.ArtifactDto;
import com.leoric01.hogwarts.services.ArtifactService;
import com.leoric01.hogwarts.system.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;

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
    void updateArtifactSuccess() throws Exception {
        ArtifactDto artifactDto = new ArtifactDto(null, "Remembrall","very interesting artifact", "ImageUrl",null);
        String json = objectMapper.writeValueAsString(artifactDto);
        Artifact savedArtifact = new Artifact(123L, "Remembrall", "very interesting artifact","ImageUrl");

        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl()));
    }
    @Test
    void testUpdateArtifactErrorWithNonExistentId() throws Exception {
        ArtifactDto artifactDto = new ArtifactDto(555L, "UpdatedArtifact","very interesting artifact", "ImageUrl",null);
        String json = objectMapper.writeValueAsString(artifactDto);
        given(artifactService.update(eq(555L), Mockito.any(Artifact.class))).willThrow(new ArtifactNotFoundException(555L));

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/artifacts/555").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 555 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        ArtifactDto artifactDto = new ArtifactDto(555L, "UpdatedArtifact","very interesting artifact", "ImageUrl",null);
        String json = objectMapper.writeValueAsString(artifactDto);
        Artifact updateArtifact = new Artifact(555L, "UpdatedArtifact", "very interesting artifact","ImageUrl");

        given(artifactService.update(eq(555L), Mockito.any(Artifact.class))).willReturn(updateArtifact);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/artifacts/555").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(updateArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updateArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updateArtifact.getImageUrl()));
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
    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        given(this.artifactService.findAllArtifacts()).willReturn(this.artifacts);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/artifacts")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value(11111))
                .andExpect(jsonPath("$.data[0].name").value("Deluminator"))
                .andExpect(jsonPath("$.data[1].id").value(11112))
                .andExpect(jsonPath("$.data[1].name").value("Invisibility Cloak"));
    }
}