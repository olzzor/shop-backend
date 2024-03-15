package com.shop.module.category.repository;

import com.shop.module.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCode(String code);

    @Query("SELECT c.name FROM Category c WHERE c.code = :code")
    Optional<String> findNameByCode(@Param("code") String code);

    Optional<Category> findByCode(String code);
}