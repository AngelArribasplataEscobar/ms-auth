package com.auth.ms_auth.repository;
import com.auth.ms_auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * @file: UserRepository
 * @author: Angel Arribasplata
 * @created: 3/05/2025
 */
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
}
