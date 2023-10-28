package com.leoric01.hogwarts.controllers;

import com.leoric01.hogwarts.models.wizard.Wizard;
import com.leoric01.hogwarts.models.wizard.converter.WizardDtoToWizardConverter;
import com.leoric01.hogwarts.models.wizard.converter.WizardToWizardDtoConverter;
import com.leoric01.hogwarts.models.wizard.dto.WizardDto;
import com.leoric01.hogwarts.services.WizardService;
import com.leoric01.hogwarts.system.Result;
import com.leoric01.hogwarts.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wizards")
public class WizardController {

    private final WizardService wizardService;
    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    @Autowired
    public WizardController(WizardService wizardService, WizardDtoToWizardConverter wizardDtoToWizardConverter, WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardService = wizardService;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }
    @GetMapping
    public Result findAllWizards(){
        List<Wizard> foundWizards = wizardService.findAll();
        List<WizardDto> wizardDtos = foundWizards.stream()
                .map(wizardToWizardDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", wizardDtos);
    }
    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Long wizardId){
        Wizard foundWizard = wizardService.findById(wizardId);
        WizardDto wizardDto = wizardToWizardDtoConverter.convert(foundWizard);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardDto);
    }

    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto){
        Wizard newWizard = wizardDtoToWizardConverter.convert(wizardDto);
        Wizard savedWizard = wizardService.save(newWizard);
        WizardDto savedWizardDto = wizardToWizardDtoConverter.convert(savedWizard);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedWizardDto);
    }
    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Long wizardId, @Valid @RequestBody WizardDto wizardDto){
        Wizard update = wizardDtoToWizardConverter.convert(wizardDto);
        Wizard updatedWizard = wizardService.update(wizardId, update);
        WizardDto updatedWizardDto = wizardToWizardDtoConverter.convert(updatedWizard);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedWizardDto);
    }
    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Long wizardId){
        wizardService.delete(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success", null);
    }
    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact(@PathVariable Long wizardId, @PathVariable Long artifactId){
        wizardService.assignArtifact(wizardId, artifactId);
    return new Result(true, StatusCode.SUCCESS, "Artifact Assignment Success");
    }

}
