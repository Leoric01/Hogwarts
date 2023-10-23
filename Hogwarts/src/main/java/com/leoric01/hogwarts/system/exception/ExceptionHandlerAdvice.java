package com.leoric01.hogwarts.system.exception;

import com.leoric01.hogwarts.models.artifact.ArtifactNotFoundException;
import com.leoric01.hogwarts.system.Result;
import com.leoric01.hogwarts.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ArtifactNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleArtifactNotFoundException(ArtifactNotFoundException ex){
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
    }
}
