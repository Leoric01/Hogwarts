package com.leoric01.hogwarts.services;

import com.leoric01.hogwarts.models.wizard.Wizard;
import com.leoric01.hogwarts.models.wizard.WizardNotFoundException;
import com.leoric01.hogwarts.respositories.WizardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardServiceImpl implements WizardService{
    private final WizardRepository wizardRepository;

    @Autowired
    public WizardServiceImpl(WizardRepository wizardRepository) {
        this.wizardRepository = wizardRepository;
    }

    @Override
    public List<Wizard> findAll() {
        return wizardRepository.findAll();
    }

    @Override
    public Wizard save(Wizard newWizard) {
        return wizardRepository.save(newWizard);
    }

    @Override
    public Wizard update(Long wizardId, Wizard update) {
    return wizardRepository
        .findById(wizardId)
        .map(
            oldWizard -> {
              oldWizard.setName(update.getName());
              return wizardRepository.save(oldWizard);
            })
            .orElseThrow(() -> new WizardNotFoundException(wizardId));
    }

    @Override
    public void delete(Long wizardId) {
        Wizard wizardToBeDeleted = wizardRepository.findById(wizardId)
                .orElseThrow(() -> new WizardNotFoundException(wizardId));
        wizardToBeDeleted.removeAllArtifacts();
        wizardRepository.deleteById(wizardId);
    }

    @Override
    public Wizard findById(Long wizardId) {
        return wizardRepository.findById(wizardId).orElseThrow(() -> new WizardNotFoundException(wizardId));
    }
}
