package com.leoric01.hogwarts.models.hogwartsuser;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("Could not find user with id "+id+" :(");
    }
}
