package com.leoric01.hogwarts.services;
import com.leoric01.hogwarts.models.hogwartsuser.HogwartsUser;
import com.leoric01.hogwarts.models.hogwartsuser.UserNotFoundException;
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
    @Test
    void testFindAllSuccess(){
        given(userRepository.findAll()).willReturn(hogwartsUsers);
        List<HogwartsUser> actualUsers = userService.findAll();
        assertThat(actualUsers.size()).isEqualTo(this.hogwartsUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess(){
        HogwartsUser u = new HogwartsUser();
        u.setId(1L);
        u.setUsername("john");
        u.setPassword("123");
        u.setEnabled(true);
        u.setRoles("ADMIN USER");
        given(userRepository.findById(1L)).willReturn(Optional.of(u));
        HogwartsUser returnedUser = userService.findById(1L);
        assertThat(returnedUser.getId()).isEqualTo(u.getId());
        assertThat(returnedUser.getUsername()).isEqualTo(u.getUsername());
        assertThat(returnedUser.getPassword()).isEqualTo(u.getPassword());
        assertThat(returnedUser.isEnabled()).isEqualTo(u.isEnabled());
        assertThat(returnedUser.getRoles()).isEqualTo(u.getRoles());
        verify(userRepository, times(1)).findById(1L);
    }
    @Test
    void testFindByIdNotFound(){
        given(userRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());
        Throwable thrown = catchThrowable(() -> {
            HogwartsUser returnedUser = userService.findById(1L);
        });
        assertThat(thrown).isInstanceOf(UserNotFoundException.class)
                .hasMessage("Could not find user with id 1 :(");
        verify(userRepository, times(1)).findById(1L);
    }
    @Test
    void testSaveSuccess(){
        HogwartsUser u = new HogwartsUser();
        u.setId(1L);
        u.setUsername("john");
        u.setPassword("123");
        u.setEnabled(true);
        u.setRoles("ADMIN USER");
        given(userRepository.save(u)).willReturn(u);
        HogwartsUser returnedUser = userService.save(u);
        assertThat(returnedUser.getUsername()).isEqualTo(u.getUsername());
        assertThat(returnedUser.getPassword()).isEqualTo(u.getPassword());
        assertThat(returnedUser.isEnabled()).isEqualTo(u.isEnabled());
        assertThat(returnedUser.getRoles()).isEqualTo(u.getRoles());
        verify(userRepository, times(1)).save(u);
    }

  @Test
  void testUpdateSuccess() {
    HogwartsUser oldUser = new HogwartsUser();
    oldUser.setId(1L);
    oldUser.setUsername("john");
    oldUser.setPassword("123");
    oldUser.setEnabled(true);
    oldUser.setRoles("ADMIN USER");

      HogwartsUser update = new HogwartsUser();
      update.setUsername("john update");
      update.setPassword("123");
      update.setEnabled(true);
      update.setRoles("ADMIN USER");
      given(userRepository.findById(1L)).willReturn(Optional.of(oldUser));
      given(userRepository.save(oldUser)).willReturn(oldUser);
      HogwartsUser updatedUser = userService.update(1L, update);
      assertThat(updatedUser.getId()).isEqualTo(1L);
      assertThat(updatedUser.getUsername()).isEqualTo(update.getUsername());
      verify(userRepository, times(1)).findById(1L);
      verify(userRepository, times(1)).save(oldUser);
    }
    @Test
    void testUpdateNotFound(){
        HogwartsUser update = new HogwartsUser();
        update.setUsername("john update");
        update.setPassword("123");
        update.setEnabled(true);
        update.setRoles("ADMIN USER");
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        Throwable thrown = assertThrows(UserNotFoundException.class, () -> userService.update(1L, update));
        assertThat(thrown).isInstanceOf(UserNotFoundException.class).hasMessage("Could not find user with id 1 :(");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteSuccess(){
        HogwartsUser u = new HogwartsUser();
        u.setId(1L);
        u.setUsername("john");
        u.setPassword("123");
        u.setEnabled(true);
        u.setRoles("ADMIN USER");
        given(userRepository.findById(1L)).willReturn(Optional.of(u));
        doNothing().when(userRepository).deleteById(1L);
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteNotFound(){
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        Throwable thrown = assertThrows(UserNotFoundException.class, () -> userService.delete(1L));
        assertThat(thrown).isInstanceOf(UserNotFoundException.class).hasMessage("Could not find user with id 1 :(");
        verify(userRepository, times(1)).findById(1L);

    }

}
