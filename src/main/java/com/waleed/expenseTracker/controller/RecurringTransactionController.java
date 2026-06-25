package com.waleed.expenseTracker.controller;

import com.waleed.expenseTracker.enums.CategoryType;
import com.waleed.expenseTracker.model.dto.RecurringTransactionDto;
import com.waleed.expenseTracker.model.entity.RecurringTransaction;
import com.waleed.expenseTracker.model.mappers.RecurringTransactionMapper;
import com.waleed.expenseTracker.model.request.recurringtransaction.CreateRecurringTransactionRequest;
import com.waleed.expenseTracker.model.request.recurringtransaction.UpdateActivationRequest;
import com.waleed.expenseTracker.model.request.recurringtransaction.UpdateRecurringTransactionRequest;
import com.waleed.expenseTracker.model.response.ApiResponse;
import com.waleed.expenseTracker.security.user.SystemUserDetails;
import com.waleed.expenseTracker.service.recurringtransaction.RecurringTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/recurring-transactions")
public class RecurringTransactionController {
    private final RecurringTransactionService service;
    private final RecurringTransactionMapper mapper;

    @GetMapping("user")
    public ResponseEntity<ApiResponse<List<RecurringTransactionDto>>> findAll(
            @RequestParam(required = false) CategoryType type,
            @AuthenticationPrincipal SystemUserDetails userDetails) {
        return ResponseEntity.ok(
                ApiResponse.of(mapper.toDtoList(service.findAll(userDetails.getId(), type))));
    }

    @PostMapping("recurring-transaction")
    public ResponseEntity<ApiResponse<RecurringTransactionDto>> create(
            @Valid @RequestBody CreateRecurringTransactionRequest request,
            @AuthenticationPrincipal SystemUserDetails userDetails) {
        RecurringTransaction created = service.create(request, userDetails.getId());
        return ResponseEntity.status(CREATED)
                .body(ApiResponse.of("RecurringTransaction Created Successfully", mapper.toDto(created)));
    }

    @PutMapping("recurring-transaction/{id}")
    public ResponseEntity<ApiResponse<RecurringTransactionDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRecurringTransactionRequest request,
            @AuthenticationPrincipal SystemUserDetails userDetails) {
        RecurringTransaction updated = service.update(id, request, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.of("RecurringTransaction Updated Successfully", mapper.toDto(updated)));
    }

    @PatchMapping("recurring-transaction/{id}/activation")
    public ResponseEntity<ApiResponse<RecurringTransactionDto>> updateActivation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateActivationRequest request,
            @AuthenticationPrincipal SystemUserDetails userDetails) {
        RecurringTransaction updated = service.updateActivation(id, request.active(), userDetails.getId());
        return ResponseEntity.ok(ApiResponse.of("RecurringTransaction Activation Updated Successfully", mapper.toDto(updated)));
    }

    @DeleteMapping("recurring-transaction/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal SystemUserDetails userDetails) {
        service.delete(id, userDetails.getId());
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
