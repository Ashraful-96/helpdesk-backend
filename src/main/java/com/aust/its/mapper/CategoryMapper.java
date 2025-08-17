package com.aust.its.mapper;

import com.aust.its.dto.CategoryDto;
import com.aust.its.entity.Category;

public class CategoryMapper {

    public static CategoryDto entityToDto(Category category) {
        return new CategoryDto(category.getId(), category.getCategoryName());
    }

    public static Category dtoToEntity(CategoryDto dto) {
        Category category = new Category();
        category.setCategoryName(dto.categoryName());
        return category;
    }
}
