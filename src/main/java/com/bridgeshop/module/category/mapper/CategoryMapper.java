package com.bridgeshop.module.category.mapper;

import com.bridgeshop.module.category.dto.CategoryDto;
import com.bridgeshop.module.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    public CategoryDto mapToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .build();
    }

    public List<CategoryDto> mapToDtoList(List<Category> categoryList) {
        return categoryList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Category mapToEntity(CategoryDto categoryDto) {
        Category category = Category.builder()
                .code(categoryDto.getCode())
                .name(categoryDto.getName())
                .build();
        return category;
    }

    public List<Category> mapToEntityList(List<CategoryDto> categoryDtoList) {
        return categoryDtoList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }
}