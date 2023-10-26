package com.leoric01.hogwarts.models.wizard;

public class WizardNotFoundException extends RuntimeException{

    public WizardNotFoundException(Long wizardId) {
        super("Could not find wizard with id " + wizardId + " :(");
    }
}
