package com.shop.module.user.repository;

import com.shop.module.user.entity.AuthProvider;
import com.shop.module.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    @Query("SELECT u FROM User u WHERE u.socialId = :socialId AND u.activateFlag = true")
    Optional<User> findBySocialIdWithActive(String socialId);

    Page<User> findAllByOrderByIdDesc(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.authProvider = :authProvider AND u.activateFlag = true")
    Optional<User> findByEmailAndAuthProviderWithActive(String email, AuthProvider authProvider);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.authProvider = :authProvider AND u.activateFlag = true")
    boolean existsByEmailAndAuthProviderWithActive(String email, AuthProvider authProvider);
}
