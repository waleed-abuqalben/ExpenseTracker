package com.waleed.expenseTracker.model.mappers;

import com.waleed.expenseTracker.model.dto.RecurringTransactionDto;
import com.waleed.expenseTracker.model.entity.RecurringTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface RecurringTransactionMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "category.type", target = "categoryType")
    RecurringTransactionDto toDto(RecurringTransaction entity);
    List<RecurringTransactionDto> toDtoList(List<RecurringTransaction> entities);
}
