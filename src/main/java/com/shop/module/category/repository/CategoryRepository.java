package com.shop.module.category.repository;

import com.shop.module.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsBySlug(String slug);

    @Query("SELECT c.name FROM Category c WHERE c.code = :code")
    Optional<String> findNameByCode(@Param("code") String code);

    @Query("SELECT c.codeRef FROM Category c WHERE c.slug = :slug")
    Optional<String> findCodeRefBySlug(@Param("slug") String slug);

    Optional<Category> findByCode(String code);

    Optional<Category> findBySlug(String slug);
}