package com.leoric01.hogwarts.models.artifact.converter;

import com.leoric01.hogwarts.models.artifact.Artifact;
import com.leoric01.hogwarts.models.artifact.dto.ArtifactDto;
import com.leoric01.hogwarts.models.wizard.converter.WizardToWizardDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {

    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    @Autowired
    public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        ArtifactDto artifactDto = new ArtifactDto(source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImageUrl(),
                source.getOwner() != null ? wizardToWizardDtoConverter.convert(source.getOwner()) : null);
        return artifactDto;
    }
}
