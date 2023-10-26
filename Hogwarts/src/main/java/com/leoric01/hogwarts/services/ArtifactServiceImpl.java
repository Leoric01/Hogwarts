package com.leoric01.hogwarts.services;

import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.artifact.ArtifactNotFoundException;
import com.leoric01.hogwarts.models.artifact.utils.IdWorker;
import com.leoric01.hogwarts.respositories.ArtifactRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArtifactServiceImpl implements ArtifactService {
  private final ArtifactRepository artifactRepository;
  private final IdWorker idWorker;

  @Autowired
  public ArtifactServiceImpl(ArtifactRepository artifactRepository, IdWorker idWorker) {
    this.artifactRepository = artifactRepository;
    this.idWorker = idWorker;
  }

  @Override
  public Artifact findById(Long id) {
    return artifactRepository.findById(id).orElseThrow(() -> new ArtifactNotFoundException(id));
  }

  @Override
  public List<Artifact> findAllArtifacts() {
    return artifactRepository.findAll();
  }

  @Override
  public Artifact save(Artifact artifact) {
    artifact.setId(idWorker.nextId());
    return artifactRepository.save(artifact);
  }

  @Override
  public Artifact update(Long id, Artifact artifact) {
    return artifactRepository.findById(id)
            .map(oldArtifact ->{
              oldArtifact.setName(artifact.getName());
              oldArtifact.setDescription(artifact.getDescription());
              oldArtifact.setImageUrl(artifact.getImageUrl());
              return artifactRepository.save(oldArtifact);
            })
            .orElseThrow(() -> new ArtifactNotFoundException(id));
  }

  @Override
  public void delete(Long id) {
    artifactRepository.findById(id).orElseThrow(() -> new ArtifactNotFoundException(id));
    artifactRepository.deleteById(id);
  }
}
