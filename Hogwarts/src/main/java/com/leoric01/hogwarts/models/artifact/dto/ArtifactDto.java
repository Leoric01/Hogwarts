package com.leoric01.hogwarts.models.artifact.dto;

import com.leoric01.hogwarts.models.wizard.dto.WizardDto;

public record ArtifactDto(Long id, String name, String description, String imageUrl, WizardDto owner) {

}
