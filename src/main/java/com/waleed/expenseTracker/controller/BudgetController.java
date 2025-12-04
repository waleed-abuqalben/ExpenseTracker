package com.waleed.expenseTracker.controller;

import com.waleed.expenseTracker.model.dto.BudgetDto;
import com.waleed.expenseTracker.model.dto.TransactionDto;
import com.waleed.expenseTracker.model.entity.Budget;
import com.waleed.expenseTracker.model.entity.Transaction;
import com.waleed.expenseTracker.model.mappers.BudgetMapper;
import com.waleed.expenseTracker.model.mappers.TransactionMapper;
import com.waleed.expenseTracker.model.request.budget.CreateBudgetRequest;
import com.waleed.expenseTracker.model.request.transaction.CreateTransactionRequest;
import com.waleed.expenseTracker.model.response.ApiResponse;
import com.waleed.expenseTracker.security.user.SystemUserDetails;
import com.waleed.expenseTracker.service.budget.BudgetService;
import com.waleed.expenseTracker.service.transaction.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/budgets")
public class BudgetController {
    private final BudgetService budgetService;
    private final BudgetMapper mapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BudgetDto>>> getAllBudgets(
            @AuthenticationPrincipal SystemUserDetails userDetails
    ) {
        return ResponseEntity.ok(ApiResponse.of(
                mapper.toDtoList(budgetService.findAll(userDetails.getId()))));
    }

    @PostMapping("budget")
    public ResponseEntity<ApiResponse<BudgetDto>> createBudget(
            @Valid  @RequestBody CreateBudgetRequest request,
            @AuthenticationPrincipal SystemUserDetails userDetails
    ) {
        Budget created = budgetService.create(request, userDetails.getId()) ;
        return ResponseEntity.status(CREATED)
                .body(ApiResponse.of("Budget created", mapper.toDto(created)));
    }

    @GetMapping("budget/{budgetId}")
    public ResponseEntity<ApiResponse<BudgetDto>> findById(
            @PathVariable long budgetId,
            @AuthenticationPrincipal SystemUserDetails userDetails)
    {
        return ResponseEntity.ok(ApiResponse.of(mapper.toDto(budgetService.findById(budgetId, userDetails.getId()))));
    }

}
