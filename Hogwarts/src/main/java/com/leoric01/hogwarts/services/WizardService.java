package com.leoric01.hogwarts.services;

import com.leoric01.hogwarts.models.wizard.Wizard;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public interface WizardService {

    List<Wizard> findAll();

    Wizard save(Wizard newWizard);

    Wizard update(Long wizardId, Wizard update);

    void delete(Long wizardId);

    Wizard findById(Long wizardId);
    void assignArtifact(Long wizardId, Long artifactId);
}
