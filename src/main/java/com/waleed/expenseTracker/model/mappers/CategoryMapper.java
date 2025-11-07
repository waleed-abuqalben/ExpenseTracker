package com.waleed.expenseTracker.model.mappers;

import com.waleed.expenseTracker.model.dto.CategoryDto;
import com.waleed.expenseTracker.model.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "user.id", target = "userId")
    CategoryDto toDto(Category category);

    List<CategoryDto> toDtoList(List<Category> categories);
}
