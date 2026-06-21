package com.waleed.expenseTracker.controller;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.model.dto.TransactionDto;
import com.waleed.expenseTracker.model.entity.Transaction;
import com.waleed.expenseTracker.model.mappers.TransactionMapper;
import com.waleed.expenseTracker.model.request.transaction.CreateTransactionRequest;
import com.waleed.expenseTracker.model.request.transaction.UpdateTransactionRequest;
import com.waleed.expenseTracker.model.response.ApiResponse;
import com.waleed.expenseTracker.security.user.SystemUserDetails;
import com.waleed.expenseTracker.service.transaction.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/transactions")
public class TransactionController {
    private final TransactionService txnService;
    private final TransactionMapper mapper;

    @GetMapping("budgets/{budgetId}")
    public ResponseEntity<ApiResponse<List<TransactionDto>>> byBudge(
            @PathVariable Long budgetId,
            @RequestParam(required = false) CategoryType type,
            @AuthenticationPrincipal SystemUserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.of(
                mapper.toDtoList(txnService.findByBudget(budgetId, type, userDetails.getId())))
        );
    }

    @PostMapping("transaction")
    public ResponseEntity<ApiResponse<TransactionDto>> create(
            @Valid @RequestBody CreateTransactionRequest request,
            @AuthenticationPrincipal SystemUserDetails userDetails) {
        Transaction created = txnService.create(request, userDetails.getId());
        return ResponseEntity.status(CREATED)
                .body(ApiResponse.of(mapper.toDto(created)));
    }

    @PutMapping("transaction/{txId}")
    public ResponseEntity<ApiResponse<TransactionDto>> update(
            @PathVariable long txId,
            @Valid @RequestBody UpdateTransactionRequest request,
            @AuthenticationPrincipal SystemUserDetails userDetails) {
        Transaction created = txnService.update(request, txId, userDetails.getId());
        return ResponseEntity.ok((ApiResponse.of(mapper.toDto(created))));
    }

}
