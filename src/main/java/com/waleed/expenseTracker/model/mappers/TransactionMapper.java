package com.waleed.expenseTracker.model.mappers;

import com.waleed.expenseTracker.model.dto.TransactionDto;
import com.waleed.expenseTracker.model.entity.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface TransactionMapper {
    TransactionDto toDto(Transaction transaction);
    List<TransactionDto> toDtoList(List<Transaction> transactions);
}
