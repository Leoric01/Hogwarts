package com.leoric01.hogwarts.models.artifact;

import com.leoric01.hogwarts.models.wizard.Wizard;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "artifacts")
public class Artifact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String description;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "wizard_id")
    private Wizard owner;

    public Artifact() {
    }

    public Artifact(String id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Wizard getOwner() {
        return owner;
    }

    public void setOwner(Wizard owner) {
        this.owner = owner;
    }
}
