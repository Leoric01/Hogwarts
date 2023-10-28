package com.leoric01.hogwarts.services;

import com.leoric01.hogwarts.models.hogwartsuser.HogwartsUser;
import com.leoric01.hogwarts.models.hogwartsuser.MyUserPrincipal;
import com.leoric01.hogwarts.models.hogwartsuser.UserNotFoundException;
import com.leoric01.hogwarts.respositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        newHogwartsUser.setPassword(passwordEncoder.encode(newHogwartsUser.getPassword()));
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.userRepository
        .findByUsername(username)
        .map(hogwartsUser -> new MyUserPrincipal(hogwartsUser))
        .orElseThrow(() -> new UsernameNotFoundException("username " + username + " is not found."));
    }
}
