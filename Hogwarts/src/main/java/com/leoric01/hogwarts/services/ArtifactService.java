package com.leoric01.hogwarts.services;

import com.leoric01.hogwarts.models.artifact.Artifact;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public interface ArtifactService {
    Artifact findById(Long id);
    List<Artifact> findAllArtifacts();
    Artifact save(Artifact artifact);
}
