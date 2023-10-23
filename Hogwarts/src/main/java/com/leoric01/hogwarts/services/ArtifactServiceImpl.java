package com.leoric01.hogwarts.services;

import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.artifact.ArtifactNotFoundException;
import com.leoric01.hogwarts.respositories.ArtifactRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ArtifactServiceImpl implements ArtifactService {
  private final ArtifactRepository artifactRepository;

  @Autowired
  public ArtifactServiceImpl(ArtifactRepository artifactRepository) {
    this.artifactRepository = artifactRepository;
  }

  @Override
  public Artifact findById(Long id) {
    return artifactRepository.findById(id).orElseThrow(() -> new ArtifactNotFoundException(id));
  }
}
