package com.waleed.expenseTracker.model.mappers;

import com.waleed.expenseTracker.model.dto.BudgetDto;
import com.waleed.expenseTracker.model.entity.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = TransactionMapper.class)
public interface BudgetMapper {
    @Mapping(source = "user.id", target = "userId")
    BudgetDto toDto(Budget budget);
    List<BudgetDto> toDtoList(List<Budget> budget);
}
