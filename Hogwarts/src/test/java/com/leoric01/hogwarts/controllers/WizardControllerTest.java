package com.leoric01.hogwarts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.artifact.ArtifactNotFoundException;
import com.leoric01.hogwarts.models.artifact.dto.ArtifactDto;
import com.leoric01.hogwarts.models.wizard.Wizard;
import com.leoric01.hogwarts.models.wizard.WizardNotFoundException;
import com.leoric01.hogwarts.models.wizard.dto.WizardDto;
import com.leoric01.hogwarts.services.WizardServiceImpl;
import com.leoric01.hogwarts.system.StatusCode;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class WizardControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    WizardServiceImpl wizardService;

    @Autowired
    ObjectMapper objectMapper;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() throws Exception {
        Artifact a1 = new Artifact(11111L, "Deluminator", "description1","ImagUrl1");
        Artifact a2 = new Artifact(11112L, "Invisibility Cloak", "description2","ImagUrl2");
        Artifact a3 = new Artifact(11113L, "Elder's Wand", "description3","ImagUrl3");
        Artifact a4 = new Artifact(11114L, "Map", "description4","ImagUrl4");
        Artifact a5 = new Artifact(11115L, "Sword of Gryffindor", "description5","ImagUrl5");
        Artifact a6 = new Artifact(11116L, "Resurrection Stone", "description6","ImagUrl6");
        Wizard w1 = new Wizard();
        w1.setId(1L);
        w1.setName("Harry Potter");
        w1.addArtifact(a1);
        w1.addArtifact(a2);

        Wizard w2 = new Wizard();
        w2.setId(2L);
        w2.setName("Draco Malfoy");
        w2.addArtifact(a3);
        w2.addArtifact(a4);

        Wizard w3 = new Wizard();
        w3.setId(3L);
        w3.setName("Hermione Granger");
        w3.addArtifact(a5);

        this.wizards = new ArrayList<>();
        wizards.add(w1);
        wizards.add(w2);
        wizards.add(w3);
    }
    @Test
    void testFindWizardByIdSuccess () throws Exception{
        given(wizardService.findById(1L)).willReturn(this.wizards.get(0));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Harry Potter"));
    }
    @Test
    void testFindAllWizardsSuccess() throws Exception{
        given(wizardService.findAll()).willReturn(wizards);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wizards").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("Draco Malfoy"));
    }
    @Test
    void testFindWizardByIdNotFound () throws Exception{
    given(wizardService.findById(5L)).willThrow(new WizardNotFoundException(5L));
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wizards/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void testAddWizardSuccess() throws Exception {
        WizardDto wizardDto = new WizardDto(null, "Sirius", 4);
        String json = this.objectMapper.writeValueAsString(wizardDto);
        Wizard savedWizard = new Wizard();
        savedWizard.setId(4L);
        savedWizard.setName("Sirius");
        given(wizardService.save(Mockito.any(Wizard.class))).willReturn(savedWizard);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wizards").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Sirius"));
    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
        WizardDto wizardDto = new WizardDto(null, "Belatrix", 5);
        String json = objectMapper.writeValueAsString(wizardDto);
        Wizard wizard = new Wizard("Belatrix");
        wizard.setId(123L);
        given(this.wizardService.update(eq(123L), Mockito.any(Wizard.class))).willReturn(wizard);

        mockMvc.perform(put("/api/v1/wizards/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verify the response status code is OK (2xx).
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").isNumber()) // Check that "data.id" is a number.
                .andExpect(jsonPath("$.data.name").value(wizard.getName()));
    }

    @Test
    void testUpdateWizardsErrorWithNonExistentId() throws Exception{
        given(wizardService.update(eq(5L), Mockito.any(Wizard.class))).willThrow(new WizardNotFoundException(5L));
        WizardDto wizardDto = new WizardDto(5L, "Updated wizard name", 0);
        String json = this.objectMapper.writeValueAsString(wizardDto);
        mockMvc.perform(put("/api/v1/wizards/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void testDeleteWizardSuccess() throws Exception{
        doNothing().when(wizardService).delete(3L);
        mockMvc.perform(delete("/api/v1/wizards/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void testDeleteWizardNotFound() throws Exception{
    doThrow(new WizardNotFoundException(3L)).when(wizardService).delete(3L);
        mockMvc.perform(delete("/api/v1/wizards/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 3 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactSuccess() throws Exception {
        doNothing().when(wizardService).assignArtifact(2L, 11111L);
        mockMvc.perform(put("/api/v1/wizards/2/artifacts/11111")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact Assignment Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void testAssignArtifactErrorWithNonExistentWizard() throws Exception {
    doThrow(new WizardNotFoundException(5L)).when(wizardService).assignArtifact(5L, 11111L);
        mockMvc.perform(put("/api/v1/wizards/5/artifacts/11111")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void testAssignArtifactErrorWithNonExistentArtifact() throws Exception {
    doThrow(new ArtifactNotFoundException(66666L)).when(wizardService).assignArtifact(2L, 66666L);
        mockMvc.perform(put("/api/v1/wizards/2/artifacts/66666")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 66666 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
