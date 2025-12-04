package com.waleed.expenseTracker.controller;

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

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper mapper;

    @PostMapping("transaction")
    public ResponseEntity<ApiResponse<TransactionDto>> create(
            @Valid @RequestBody CreateTransactionRequest request,
            @AuthenticationPrincipal SystemUserDetails userDetails) {
        Transaction created = transactionService.create(request, userDetails.getId());
        return ResponseEntity.status(CREATED)
                .body(ApiResponse.of(mapper.toDto(created)));
    }

    @PutMapping("transaction/{txId}")
    public ResponseEntity<ApiResponse<TransactionDto>> update(
            @PathVariable long txId,
            @Valid @RequestBody UpdateTransactionRequest request,
            @AuthenticationPrincipal SystemUserDetails userDetails) {
        Transaction created = transactionService.update(request, txId, userDetails.getId());
        return ResponseEntity.ok((ApiResponse.of(mapper.toDto(created))));
    }

}
