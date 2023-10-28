package com.leoric01.hogwarts.services;

import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.artifact.ArtifactNotFoundException;
import com.leoric01.hogwarts.models.wizard.Wizard;
import com.leoric01.hogwarts.models.wizard.WizardNotFoundException;
import com.leoric01.hogwarts.respositories.ArtifactRepository;
import com.leoric01.hogwarts.respositories.WizardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class WizardServiceImplTest {
    @Mock
    WizardRepository wizardRepository;
    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    WizardServiceImpl wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp(){
        Wizard w1 = new Wizard();
        w1.setId(1L);
        w1.setName("Harry Potter");

        Wizard w2 = new Wizard();
        w2.setId(2L);
        w2.setName("Draco Malfoy");

        Wizard w3 = new Wizard();
        w3.setId(3L);
        w3.setName("Hermione Granger");

        this.wizards = new ArrayList<>();
        wizards.add(w1);
        wizards.add(w2);
        wizards.add(w3);
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    void testAssignArtifactSuccess(){
        Artifact a1 = new Artifact(11111L, "Deluminator", "description1", "ImagUrl1");
        Wizard w2 = new Wizard();
        w2.setId(2L);
        w2.setName("Harry");
        w2.addArtifact(a1);

        Wizard w3 = new Wizard();
        w3.setId(3L);
        w3.setName("Hagrid");
        given(artifactRepository.findById(11111L)).willReturn(Optional.of(a1));
        given(wizardRepository.findById(3L)).willReturn(Optional.of(w3));
        wizardService.assignArtifact(3L,11111L);
        assertThat(a1.getOwner().getId()).isEqualTo(3);
//        assertThat(w3.getArtifacts()).contains(a1); //CANT FIND CORRECT IMPORT
    }
    @Test
    void testAssignArtifactWithNonExistentWizardId(){
        Artifact a1 = new Artifact(11111L, "Deluminator", "description1", "ImagUrl1");
        Wizard w2 = new Wizard();
        w2.setId(2L);
        w2.setName("Harry");
        w2.addArtifact(a1);

        given(artifactRepository.findById(11111L)).willReturn(Optional.of(a1));
        given(wizardRepository.findById(3L)).willReturn(Optional.empty());
        Throwable thrown = assertThrows(WizardNotFoundException.class, () -> {
            wizardService.assignArtifact(3L, 11111L);
        });
        assertThat(thrown).isInstanceOf(WizardNotFoundException.class).hasMessage("Could not find wizard with id 3 :(");
        assertThat(a1.getOwner().getId()).isEqualTo(2L);
    }
    @Test
    void testAssignArtifactWithNonExistentArtifactId(){
        given(artifactRepository.findById(11111L)).willReturn(Optional.empty());
        Throwable thrown = assertThrows(ArtifactNotFoundException.class, () -> {
            wizardService.assignArtifact(3L, 11111L);
        });
        assertThat(thrown).isInstanceOf(ArtifactNotFoundException.class).hasMessage("Could not find artifact with id 11111 :(");
    }

    @Test
    void testFindAllSuccess(){
        given(wizardRepository.findAll()).willReturn(this.wizards);
        List<Wizard> actualWizards = wizardService.findAll();
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());
        verify(wizardRepository, times(1)).findAll();
    }
    @Test
    void testFindByIdSuccess(){
        Wizard w = new Wizard();
        w.setId(1L);
        w.setName("Albus Dumbledore");
        given(wizardRepository.findById(1L)).willReturn(Optional.of(w));
        Wizard returnedWizard = wizardService.findById(1L);
        assertThat(returnedWizard.getId()).isEqualTo(w.getId());
        assertThat(returnedWizard.getName()).isEqualTo(w.getName());
        verify(wizardRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound(){
        given(wizardRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
        Throwable thrown = catchThrowable(() -> {
            Wizard returnedWizard = wizardService.findById(1L);
        });
        assertThat(thrown).isInstanceOf(WizardNotFoundException.class)
                .hasMessage("Could not find wizard with id 1 :(");
        verify(wizardRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveSuccess(){
        Wizard newWizard = new Wizard();
        newWizard.setName("Ron Weasley");
        given(wizardService.save(newWizard)).willReturn(newWizard);
        Wizard returnedWizard = wizardService.save(newWizard);
        assertThat(returnedWizard.getName()).isEqualTo(newWizard.getName());
        verify(wizardRepository, times(1)).save(newWizard);
    }
    @Test
    void testUpdateSuccess(){
        Wizard oldWizard = new Wizard();
        oldWizard.setId(1L);
        oldWizard.setName("Severus");
        Wizard update = new Wizard();
        update.setName("Albus - update");
        given(wizardRepository.findById(1L)).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(oldWizard)).willReturn(oldWizard);
        Wizard updatedWizard = wizardService.update(1L, update);
        assertThat(updatedWizard.getId()).isEqualTo(1L);
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());
        verify(wizardRepository, times(1)).findById(1L);
        verify(wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound(){
        Wizard update = new Wizard();
        update.setName("Albus - update");
        given(wizardRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(WizardNotFoundException.class, () -> {
            wizardService.update(1L, update);
        });
        verify(wizardRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteSuccess(){
        Wizard wizard = new Wizard();
        wizard.setId(1L);
        wizard.setName("Severus");

        given(wizardRepository.findById(1L)).willReturn(Optional.of(wizard));
        doNothing().when(wizardRepository).deleteById(1L);
        wizardService.delete(1L);
        verify(wizardRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound(){
        given(wizardRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(WizardNotFoundException.class, () -> {
            wizardService.delete(1L);
        });
        verify(wizardRepository, times(1)).findById(1L);
    }


}
