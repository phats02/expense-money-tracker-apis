package com.ferb.expenseMoneyTracker.controller;

import com.ferb.expenseMoneyTracker.dto.*;
import com.ferb.expenseMoneyTracker.entity.Transaction;
import com.ferb.expenseMoneyTracker.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction")
@Tag(name="Transaction apis")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @GetMapping("")
    public SuccessResponse<List<Transaction>> getListTransaction(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                                 @Valid @ModelAttribute GetTransactionRequest getTransactionRequest) {
        return new SuccessResponse<>(transactionService.getListTransactionDateBetween(customUserDetail.getUser(), getTransactionRequest.getFromDate(), getTransactionRequest.getToDate()));
    }

    @PostMapping("")
    public SuccessResponse<Transaction> createNewTransaction(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                             @Valid @RequestBody CreateTransactionRequest createTransactionRequest) {
        return new SuccessResponse<>(transactionService.createNewTransaction(customUserDetail.getUser(), createTransactionRequest));
    }

    @PutMapping("/{id}")
    public SuccessResponse<Transaction> updateTransaction(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                          @Valid @RequestBody UpdateTransactionRequest updateTransactionRequest,
                                                          @PathVariable UUID id) {
        return new SuccessResponse<>(transactionService.updateTransaction(id, customUserDetail.getUser(), updateTransactionRequest));
    }

    @DeleteMapping("/{id}")
    public SuccessResponse<Object> deleteTransaction(@AuthenticationPrincipal CustomUserDetail customUserDetail,
                                                     @Valid @PathVariable UUID id) {
        transactionService.deleteTransaction(id, customUserDetail.getUser());
        return new SuccessResponse<>(null);
    }
}
