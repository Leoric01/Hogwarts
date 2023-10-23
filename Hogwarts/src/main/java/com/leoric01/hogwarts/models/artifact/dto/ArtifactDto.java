package com.leoric01.hogwarts.models.artifact.dto;

import com.leoric01.hogwarts.models.wizard.dto.WizardDto;
import jakarta.validation.constraints.NotEmpty;

public record ArtifactDto(Long id,
                          @NotEmpty(message = "name is required") String name,
                          @NotEmpty(message = "description is required") String description,
                          @NotEmpty(message = "imageUrl is required") String imageUrl,
                          WizardDto owner) {
}
