package com.leoric01.hogwarts.system;

import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.wizard.Wizard;
import com.leoric01.hogwarts.respositories.ArtifactRepository;
import com.leoric01.hogwarts.respositories.WizardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;

    private final WizardRepository wizardRepository;
    @Autowired
    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Artifact a1 = new Artifact(11111L, "Deluminator", "Device to control lights","ImagUrl1");
        Artifact a2 = new Artifact(11112L, "Invisibility Cloak", "Provides invisibility to the wearer","ImagUrl2");
        Artifact a3 = new Artifact(11113L, "Elder's Wand", "Very powerful wand","ImagUrl3");
        Artifact a4 = new Artifact(11114L, "Map", "Shows you where you've been","ImagUrl4");
        Artifact a5 = new Artifact(11115L, "Sword of Gryffindor", "Sword previously owned by Gryffindor himself","ImagUrl5");
        Artifact a6 = new Artifact(11116L, "Resurrection Stone", "Allows resurrection of the dead","ImagUrl6");

        Wizard w1 = new Wizard("Harry");
        Wizard w2 = new Wizard("Ron");
        Wizard w3 = new Wizard("Draco");

        a1.setOwner(w1);
        a2.setOwner(w1);
        a3.setOwner(w2);
        a4.setOwner(w2);
        a5.setOwner(w3);

        wizardRepository.save(w1);
        wizardRepository.save(w2);
        wizardRepository.save(w3);

        artifactRepository.save(a1);
        artifactRepository.save(a2);
        artifactRepository.save(a3);
        artifactRepository.save(a4);
        artifactRepository.save(a5);
        artifactRepository.save(a6);

    }
}
