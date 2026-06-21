package com.waleed.expenseTracker.model.mappers;

import com.waleed.expenseTracker.model.dto.CategoryDto;
import com.waleed.expenseTracker.model.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    List<CategoryDto> toDtoList(List<Category> categories);
}
