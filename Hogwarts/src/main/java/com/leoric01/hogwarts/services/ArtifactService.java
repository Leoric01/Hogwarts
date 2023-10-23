package com.leoric01.hogwarts.services;

import com.leoric01.hogwarts.models.artifact.Artifact;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public interface ArtifactService {
    Artifact findById(String id);
}
