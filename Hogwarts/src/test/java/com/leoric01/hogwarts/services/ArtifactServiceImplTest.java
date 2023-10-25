package com.leoric01.hogwarts.services;

import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.artifact.ArtifactNotFoundException;
import com.leoric01.hogwarts.models.artifact.utils.IdWorker;
import com.leoric01.hogwarts.models.wizard.Wizard;
import com.leoric01.hogwarts.respositories.ArtifactRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceImplTest {

  @Mock
  private ArtifactRepository artifactRepository;
  @Mock
  private IdWorker idWorker;

  @InjectMocks
  private ArtifactServiceImpl artifactService;


  List<Artifact> artifacts;

  @BeforeEach
  void setUp() {
    Artifact a1 = new Artifact(11111L, "Deluminator", "description1", "ImagUrl1");
    Artifact a2 = new Artifact(11112L, "Invisibility Cloak", "description2", "ImagUrl2");
    Artifact a3 = new Artifact(11113L, "Elder's Wand", "description3", "ImagUrl3");
    Artifact a4 = new Artifact(11114L, "Map", "description4", "ImagUrl4");
    Artifact a5 = new Artifact(11115L, "Sword of Gryffindor", "description5", "ImagUrl5");
    Artifact a6 = new Artifact(11116L, "Resurrection Stone", "description6", "ImagUrl6");
    this.artifacts = new ArrayList<>(Arrays.asList(a1, a2, a3, a4, a5, a6));
  }

  @AfterEach
  void tearDown() {}

  @Test
  void testDeleteSuccess(){
    Artifact artifact = new Artifact(123L, "Invisibility Cloak", "provides invisibility", "ImageUrl");
    given(artifactRepository.findById(123L)).willReturn(Optional.of(artifact));
    doNothing().when(artifactRepository).deleteById(123L);
    artifactService.delete(123L);
    verify(artifactRepository, times(1)).deleteById(123L);
  }

  @Test
  void testDeleteNotFound(){
    given(artifactRepository.findById(123L)).willReturn(Optional.empty());
    assertThrows(ArtifactNotFoundException.class, () -> {
      artifactService.delete(123L);
    });
    verify(artifactRepository, times(1)).findById(123L);
    verify(artifactRepository, times(0)).deleteById(123L);

  }

  @Test
  void testUpdateNotFound(){
    Artifact update = new Artifact(11112L, "Invisibility Cloak", "updatedDescription", "ImagUrl2");
    given(artifactRepository.findById(update.getId()-100)).willReturn(Optional.empty());
    assertThrows(ArtifactNotFoundException.class, ()-> artifactService.update(update.getId()-100, update));
  }
  @Test
  void testUpdateSuccess(){
    Artifact oldArtifact = new Artifact(11112L, "Invisibility Cloak", "description2", "ImagUrl2");
    Artifact update = new Artifact(11112L, "Invisibility Cloak", "updatedDescription", "ImagUrl2");
    given(artifactRepository.findById(oldArtifact.getId())).willReturn(Optional.of(oldArtifact));
    given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact); // on save old artifact already has updated info
    Artifact updatedArtifact = artifactService.update(11112L, update);
    assertThat(updatedArtifact.getId()).isEqualTo(11112L);
    assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
    verify(artifactRepository, times(1)).findById(11112L);
    verify(artifactRepository, times(1)).save(oldArtifact);

  }

  @Test
  void testSaveSuccess() {
    Artifact newArtifact = new Artifact();
    newArtifact.setName("test artifact name");
    newArtifact.setDescription("test description");
    newArtifact.setImageUrl("test ImageUrl");
    given(idWorker.nextId()).willReturn(123456L);
    given(artifactRepository.save(newArtifact)).willReturn(newArtifact);

    Artifact savedArtifact = artifactService.save(newArtifact);

    assertThat(savedArtifact.getId()).isEqualTo(123456L);
    assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
    assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
    assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());

    verify(artifactRepository, times(1)).save(newArtifact);
  }
  @Test
  void testFindByIdNotFound() {
    given(artifactRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
    Throwable thrown =
        catchThrowable(
            () -> {
              Artifact artifactById = artifactService.findById(12345L);
            });
    assertThat(thrown)
        .isInstanceOf(ArtifactNotFoundException.class)
        .hasMessage("Could not find artifact with id 12345 :(");
  }

  @Test
  void testFindByIdSuccess() {
    Artifact artifact = new Artifact();
    artifact.setId(12345L);
    artifact.setName("Invisibility cloak");
    artifact.setDescription("used to make the wearer invisible");
    artifact.setImageUrl("ImageUrl");

    Wizard wizard = new Wizard();
    wizard.setId(2L);
    wizard.setName("Ron Weasley");
    artifact.setOwner(wizard);

    given(artifactRepository.findById(12345L)).willReturn(Optional.of(artifact));

    Artifact artifactById = artifactService.findById(12345L);

    assertEquals(artifact.getId(), artifactById.getId());
    assertEquals(artifact.getName(), artifactById.getName());
    assertEquals(artifact.getDescription(), artifactById.getDescription());
    assertEquals(artifact.getImageUrl(), artifactById.getImageUrl());
    verify(artifactRepository, times(1)).findById(12345L);
  }

  @Test
  void testFindAllArtifactsSuccess() {
    given(artifactRepository.findAll()).willReturn(this.artifacts);
    List<Artifact> actualArtifacts = artifactService.findAllArtifacts();
    assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
    verify(artifactRepository, times(1)).findAll();
  }
}
