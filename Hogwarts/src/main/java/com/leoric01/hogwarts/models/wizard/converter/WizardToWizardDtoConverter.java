package com.leoric01.hogwarts.models.wizard.converter;

import com.leoric01.hogwarts.models.wizard.Wizard;
import com.leoric01.hogwarts.models.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardToWizardDtoConverter implements Converter<Wizard, WizardDto>{

    @Override
    public WizardDto convert(Wizard source) {
        WizardDto wizardDto = new WizardDto(source.getId(),
                                            source.getName(),
                                            source.getNumberOfArtifacts());
        return wizardDto;
    }
}
