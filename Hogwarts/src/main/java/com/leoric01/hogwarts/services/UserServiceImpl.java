package com.leoric01.hogwarts.services;

import com.leoric01.hogwarts.models.hogwartsuser.HogwartsUser;
import com.leoric01.hogwarts.models.hogwartsuser.UserNotFoundException;
import com.leoric01.hogwarts.models.hogwartsuser.dto.UserDto;
import com.leoric01.hogwarts.respositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<HogwartsUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public HogwartsUser findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public HogwartsUser save(HogwartsUser newHogwartsUser) {
        return userRepository.save(newHogwartsUser);
    }

    @Override
    public HogwartsUser update(Long userId, HogwartsUser update) {
        HogwartsUser oldUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        oldUser.setUsername(update.getUsername());
        oldUser.setEnabled(update.isEnabled());
        oldUser.setRoles(update.getRoles());
        return userRepository.save(oldUser);
    }

    @Override
    public void delete(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.deleteById(userId);
    }
}
