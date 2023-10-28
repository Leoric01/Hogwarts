package com.leoric01.hogwarts.models.hogwartsuser.converter;

import com.leoric01.hogwarts.models.hogwartsuser.HogwartsUser;
import com.leoric01.hogwarts.models.hogwartsuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<HogwartsUser, UserDto> {
    @Override
    public UserDto convert(HogwartsUser source) {
        final UserDto userDto = new UserDto(source.getId(), source.getUsername(),source.isEnabled(),source.getRoles());
        return userDto;
    }
}
