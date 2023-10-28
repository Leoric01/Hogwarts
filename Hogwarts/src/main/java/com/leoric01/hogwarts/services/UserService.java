package com.leoric01.hogwarts.services;

import com.leoric01.hogwarts.models.hogwartsuser.HogwartsUser;
import com.leoric01.hogwarts.models.hogwartsuser.dto.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public interface UserService {
    List<HogwartsUser> findAll();

    HogwartsUser findById(Long userId);

    HogwartsUser save(HogwartsUser newHogwartsUser);

    HogwartsUser update(Long userId, HogwartsUser user);

    void delete(Long userId);

}
