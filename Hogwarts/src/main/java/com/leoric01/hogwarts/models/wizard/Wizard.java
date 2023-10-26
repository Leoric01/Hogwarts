package com.leoric01.hogwarts.models.wizard;

import com.leoric01.hogwarts.models.artifact.Artifact;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wizards")
public class Wizard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "wizard", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JsonIgnore
    private List<Artifact> artifacts = new ArrayList<>();

    public Wizard() {
    }

    public Wizard(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size();
    }

    public void removeAllArtifacts() {
        this.artifacts.forEach(artifact -> artifact.setOwner(null));
        this.artifacts = null;
    }

    public void addArtifact(Artifact a1) {
        a1.setOwner(this);
        this.artifacts.add(a1);
    }
}
