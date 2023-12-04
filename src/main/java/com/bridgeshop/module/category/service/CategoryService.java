package com.bridgeshop.module.category.service;

import com.bridgeshop.module.category.dto.CategoryDto;
import com.bridgeshop.module.category.entity.Category;
import com.bridgeshop.module.category.mapper.CategoryMapper;
import com.bridgeshop.module.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public boolean existCategory(String code) {
        return categoryRepository.existsByCode(code);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public CategoryDto convertToDto(Category category) {
        return categoryMapper.mapToDto(category);
    }

    public List<CategoryDto> convertToDtoList(List<Category> categoryList) {
        return categoryMapper.mapToDtoList(categoryList);
    }
}