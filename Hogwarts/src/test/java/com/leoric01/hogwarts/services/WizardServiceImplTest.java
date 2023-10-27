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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
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
    void testAssignArtifactErrorWithNonExistentWizardId(){
        Artifact artifact = new Artifact();
        artifact.setId(1234L);
        artifact.setName("Elder Wand");
        artifact.setDescription("Powerful wand");
        artifact.setImageUrl("ImageUrl");

        Wizard wizard1 = new Wizard();
        wizard1.setId(999L);
        wizard1.setName("Hagrid");
        wizard1.addArtifact(artifact);

        assertThat(artifact.getOwner().getId()).isEqualTo(999L);

        given(artifactRepository.findById(1234L)).willReturn(Optional.of(artifact));
        given(wizardRepository.findById(3L)).willReturn(Optional.empty());

        Throwable thrown = assertThrows(WizardNotFoundException.class, () -> {
            wizardService.assignArtifact(3L, 1234L);
        });

        assertThat(thrown).isInstanceOf(WizardNotFoundException.class).hasMessage("Could not find wizard with id 3 :(");
        assertThat(artifact.getOwner().getId()).isEqualTo(999L);
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId(){
        given(artifactRepository.findById(1234L)).willReturn(Optional.empty());

        Throwable thrown = assertThrows(ArtifactNotFoundException.class, () -> {
            wizardService.assignArtifact(3L, 1234L);
        });
        assertThat(thrown).isInstanceOf(ArtifactNotFoundException.class).hasMessage("Could not find artifact with id 1234 :(");
    }

    @Test
    void testAssignArtifactSuccess(){
        Artifact artifact = new Artifact();
        artifact.setId(1234L);
        artifact.setName("Elder Wand");
        artifact.setDescription("Powerful wand");
        artifact.setImageUrl("ImageUrl");

        Wizard wizard1 = new Wizard();
        wizard1.setId(999L);
        wizard1.setName("Hagrid");
        wizard1.addArtifact(artifact);

        Wizard wizard2 =new Wizard();
        wizard2.setId(100L);
        wizard2.setName("Harry");

        assertThat(artifact.getOwner().getId()).isEqualTo(999L);

        given(wizardRepository.findById(100L)).willReturn(Optional.of(wizard2));
        given(artifactRepository.findById(1234L)).willReturn(Optional.of(artifact));

        wizardService.assignArtifact(100L, 1234L);

        assertThat(artifact.getOwner().getId()).isEqualTo(100L);
        assertThat(wizard2.getArtifacts()).contains(artifact);
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
