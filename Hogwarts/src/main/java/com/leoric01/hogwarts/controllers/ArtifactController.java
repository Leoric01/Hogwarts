package com.leoric01.hogwarts.controllers;

import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.artifact.converter.ArtifactToArtifactDtoConverter;
import com.leoric01.hogwarts.models.artifact.dto.ArtifactDto;
import com.leoric01.hogwarts.services.ArtifactService;
import com.leoric01.hogwarts.system.Result;
import com.leoric01.hogwarts.system.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ArtifactController {
  private final ArtifactService artifactService;
  private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;

  @Autowired
  public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter) {
    this.artifactService = artifactService;
    this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
  }

  @GetMapping("/artifacts/{artifactId}")
  public Result findArtifactById(@PathVariable Long artifactId){
    Artifact foundArtifact = artifactService.findById(artifactId);
    ArtifactDto artifactDto = artifactToArtifactDtoConverter.convert(foundArtifact);
    return new Result(true, StatusCode.SUCCESS, "Find one Success", artifactDto);
  }
}
