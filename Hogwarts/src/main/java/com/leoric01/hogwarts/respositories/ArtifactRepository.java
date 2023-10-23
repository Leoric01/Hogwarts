package com.leoric01.hogwarts.respositories;

import com.leoric01.hogwarts.models.artifact.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
}
