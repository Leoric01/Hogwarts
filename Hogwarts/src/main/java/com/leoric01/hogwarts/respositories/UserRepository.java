package com.leoric01.hogwarts.respositories;

import com.leoric01.hogwarts.models.hogwartsuser.HogwartsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<HogwartsUser, Long> {
    Optional<HogwartsUser> findByUsername(String username);
}
