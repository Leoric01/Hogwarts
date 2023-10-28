package com.leoric01.hogwarts.controllers;

import com.leoric01.hogwarts.models.hogwartsuser.HogwartsUser;
import com.leoric01.hogwarts.models.hogwartsuser.converter.UserDtoToUserConverter;
import com.leoric01.hogwarts.models.hogwartsuser.converter.UserToUserDtoConverter;
import com.leoric01.hogwarts.models.hogwartsuser.dto.UserDto;
import com.leoric01.hogwarts.services.UserService;
import com.leoric01.hogwarts.system.Result;
import com.leoric01.hogwarts.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserDtoToUserConverter userDtoToUserConverter;

    @Autowired
    public UserController(UserService userService, UserToUserDtoConverter userToUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }
    @GetMapping
    public Result findAllUsers(){
        List<HogwartsUser> foundHogwartsUser = userService.findAll();
        List<UserDto> userDtos =foundHogwartsUser.stream().map(userToUserDtoConverter::convert).collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);
    }
    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Long userId){
        HogwartsUser foundHogwartsUser = userService.findById(userId);
        UserDto userDto = userToUserDtoConverter.convert(foundHogwartsUser);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }
    @PostMapping
    public Result addUser(@Valid @RequestBody HogwartsUser newHogwartsUser){
        HogwartsUser savedUser = userService.save(newHogwartsUser);
        UserDto savedUserDto = userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedUserDto);
    }
    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDto){
        HogwartsUser update = userDtoToUserConverter.convert(userDto);
        HogwartsUser updatedUser = userService.update(userId, update);
        UserDto updatedUserDto = userToUserDtoConverter.convert(updatedUser);
        return new Result(true, StatusCode.SUCCESS, "Update Success",updatedUserDto);
    }
    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Long userId){
        userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

}
