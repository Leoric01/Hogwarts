package com.leoric01.hogwarts.services;
import com.leoric01.hogwarts.models.hogwartsuser.HogwartsUser;
import com.leoric01.hogwarts.respositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;
    List<HogwartsUser> hogwartsUsers;
    @BeforeEach
    void setUp(){
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1L);
        u1.setUsername("john");
        u1.setPassword("123");
        u1.setRoles("ADMIN USER");

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2L);
        u2.setUsername("eric");
        u2.setPassword("321");
        u2.setRoles("USER");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3L);
        u3.setUsername("monika");
        u3.setPassword("ccc");
        u3.setRoles("ADMIN");
        this.hogwartsUsers = new ArrayList<>();
        this.hogwartsUsers.add(u1);
        this.hogwartsUsers.add(u2);
        this.hogwartsUsers.add(u3);
    }
    @AfterEach
    void tearDown(){
    }

}
