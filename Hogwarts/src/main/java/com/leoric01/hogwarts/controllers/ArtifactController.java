package com.leoric01.hogwarts.controllers;

import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.artifact.converter.ArtifactDtoToArtifactConverter;
import com.leoric01.hogwarts.models.artifact.converter.ArtifactToArtifactDtoConverter;
import com.leoric01.hogwarts.models.artifact.dto.ArtifactDto;
import com.leoric01.hogwarts.services.ArtifactService;
import com.leoric01.hogwarts.system.Result;
import com.leoric01.hogwarts.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ArtifactController {
  private final ArtifactService artifactService;
  private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
  private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

  @Autowired
  public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
    this.artifactService = artifactService;
    this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
    this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
  }

  @GetMapping("/artifacts/{artifactId}")
  public Result findArtifactById(@PathVariable Long artifactId){
    Artifact foundArtifact = artifactService.findById(artifactId);
    ArtifactDto artifactDto = artifactToArtifactDtoConverter.convert(foundArtifact);
    return new Result(true, StatusCode.SUCCESS, "Find one Success", artifactDto);
  }
  @GetMapping("/artifacts")
  public Result findAllArtifacts(){
    List<Artifact> artifacts = artifactService.findAllArtifacts();
    List<ArtifactDto> artifactDtos = artifacts.stream()
            .map(artifactToArtifactDtoConverter::convert)
            .toList();
    return new Result(true, StatusCode.SUCCESS, "Find All Success", artifactDtos);
  }
  @PostMapping("/artifacts")
  public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto){
    Artifact artifact = artifactDtoToArtifactConverter.convert(artifactDto);
    Artifact savedArtifact = artifactService.save(artifact);
    ArtifactDto savedArtifactDto = artifactToArtifactDtoConverter.convert(savedArtifact);
    return new Result(true, StatusCode.SUCCESS,"Add Success", savedArtifactDto);
  }
}
