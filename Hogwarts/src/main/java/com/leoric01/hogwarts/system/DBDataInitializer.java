package com.leoric01.hogwarts.system;

import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.hogwartsuser.HogwartsUser;
import com.leoric01.hogwarts.models.wizard.Wizard;
import com.leoric01.hogwarts.respositories.ArtifactRepository;
import com.leoric01.hogwarts.respositories.UserRepository;
import com.leoric01.hogwarts.respositories.WizardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;

    private final WizardRepository wizardRepository;
    private final UserRepository userRepository;
    @Autowired
    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository, UserRepository userRepository) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
        this.userRepository = userRepository;
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

        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1L);
        u1.setUsername("john");
        u1.setPassword("123");
        u1.setEnabled(true);
        u1.setRoles("ADMIN USER");

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2L);
        u2.setUsername("eric");
        u2.setPassword("321");
        u2.setEnabled(false);
        u2.setRoles("USER");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3L);
        u3.setUsername("monika");
        u3.setPassword("ccc");
        u3.setEnabled(true);
        u3.setRoles("ADMIN");
        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);

    }
}
