package com.bridgeshop.module.user.repository;

import com.bridgeshop.module.user.entity.AuthProvider;
import com.bridgeshop.module.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findBySocialId(String socialId);

    Page<User> findAllByOrderByIdDesc(Pageable pageable);

    Optional<User> findByEmailAndAuthProvider(String email, AuthProvider authProvider);

    boolean existsByEmailAndAuthProvider(String email, AuthProvider authProvider);
}
