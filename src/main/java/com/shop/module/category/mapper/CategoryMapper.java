package com.shop.module.category.mapper;

import com.shop.module.category.dto.CategoryDto;
import com.shop.module.category.entity.Category;
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
                .codeRef(category.getCodeRef())
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }

    public List<CategoryDto> mapToDtoList(List<Category> categoryList) {
        return categoryList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Category mapToEntity(CategoryDto categoryDto) {
        Category category = Category.builder()
                .code(categoryDto.getCode())
                .codeRef(categoryDto.getCodeRef())
                .name(categoryDto.getName())
                .slug(categoryDto.getSlug())
                .build();
        return category;
    }

    public List<Category> mapToEntityList(List<CategoryDto> categoryDtoList) {
        return categoryDtoList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }
}