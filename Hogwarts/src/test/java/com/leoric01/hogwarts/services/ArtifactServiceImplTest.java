package com.leoric01.hogwarts.services;

import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.artifact.ArtifactNotFoundException;
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

import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceImplTest {

    @Mock
    private ArtifactRepository artifactRepository;

    @InjectMocks
    private ArtifactServiceImpl artifactService;

    @BeforeEach
    void setUp() {
      }

    @AfterEach
    void tearDown() {
      }

    @Test
    void testFindByIdNotFound(){
        given(artifactRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
        Throwable thrown = catchThrowable(()->{
            Artifact artifactById = artifactService.findById(12345L);
        });
        assertThat(thrown).isInstanceOf(ArtifactNotFoundException.class).hasMessage("Could not find artifact with id 12345 :(");

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
}