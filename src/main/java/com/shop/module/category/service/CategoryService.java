package com.shop.module.category.service;

import com.shop.module.category.dto.CategoryDto;
import com.shop.module.category.entity.Category;
import com.shop.module.category.mapper.CategoryMapper;
import com.shop.module.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public boolean existCategory(String slug) {
        return categoryRepository.existsBySlug(slug);
    }

    public boolean isMain(String slug) {
        Optional<String> codeRefOptional = categoryRepository.findCodeRefBySlug(slug);
        return !codeRefOptional.isPresent(); // codeRef가 없으면 true 반환
    }

    public Optional<Category> retrieveBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    public List<Category> retrieveAll() {
        return categoryRepository.findAll();
    }

    public CategoryDto convertToDto(Category category) {
        return categoryMapper.mapToDto(category);
    }

    public List<CategoryDto> convertToDtoList(List<Category> categoryList) {
        return categoryMapper.mapToDtoList(categoryList);
    }
}