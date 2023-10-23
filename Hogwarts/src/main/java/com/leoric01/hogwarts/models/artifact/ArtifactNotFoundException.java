package com.leoric01.hogwarts.models.artifact;

public class ArtifactNotFoundException extends RuntimeException{
    public ArtifactNotFoundException(Long id){
        super("Could not find artifact with id " + id + " :(");
    }
}
