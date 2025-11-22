package com.ferb.expenseMoneyTracker.service;

import com.ferb.expenseMoneyTracker.dto.CreateTransactionRequest;
import com.ferb.expenseMoneyTracker.dto.UpdateTransactionRequest;
import com.ferb.expenseMoneyTracker.entity.Category;
import com.ferb.expenseMoneyTracker.entity.Transaction;
import com.ferb.expenseMoneyTracker.entity.User;
import com.ferb.expenseMoneyTracker.entity.Wallet;
import com.ferb.expenseMoneyTracker.exception.NotFound;
import com.ferb.expenseMoneyTracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    CategoryService categoryService;
    @Autowired
    WalletService walletService;

    public List<Transaction> getListTransactionDateBetween (User owner, LocalDate fromDate, LocalDate toDate) {
        return transactionRepository.getByOwnerAndDateBetween(owner, fromDate, toDate);
    }

    public Transaction createNewTransaction (User owner, CreateTransactionRequest createTransactionRequest) {
        Category category= categoryService.getById(createTransactionRequest.getCategoryId(), owner);
        if (category == null) throw new NotFound("Category");
        Wallet wallet = walletService.findByWalletId(createTransactionRequest.getWalletId(), owner);
        if (wallet == null) throw new NotFound("Wallet");

        Transaction newTransaction = Transaction.builder()
                .date(createTransactionRequest.getDate())
                .category(category)
                .note(createTransactionRequest.getNote())
                .amount(createTransactionRequest.getAmount())
                .wallet(wallet)
                .owner(owner)
                .title(createTransactionRequest.getTitle())
                .build();

        return transactionRepository.save(newTransaction);
    }

    public Transaction updateTransaction(UUID id,User owner, UpdateTransactionRequest updateTransactionRequest) {
        Transaction transaction = transactionRepository.getByIdAndOwner(id, owner);
        if (transaction == null) {
            throw  new NotFound("Transaction");
        }

        if (updateTransactionRequest.getCategoryId() != null) {
            Category category= categoryService.getById(updateTransactionRequest.getCategoryId(), owner);
            if (category == null) throw new NotFound("Category");
            transaction.setCategory(category);
        }

        if (updateTransactionRequest.getWalletId() != null) {
            Wallet wallet = walletService.findByWalletId(updateTransactionRequest.getWalletId(), owner);
            if (wallet == null) throw new NotFound("Wallet");
            transaction.setWallet(wallet);
        }

        if (updateTransactionRequest.getTitle() != null) {
            transaction.setTitle(updateTransactionRequest.getTitle());
        }

        if (updateTransactionRequest.getNote() != null) {
            transaction.setNote(updateTransactionRequest.getNote());
        }

        if (updateTransactionRequest.getDate() != null) {
            transaction.setDate(updateTransactionRequest.getDate());
        }

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(UUID id, User owner) {
        Transaction transaction = transactionRepository.getByIdAndOwner(id, owner);
        if (transaction == null) {
            throw  new NotFound("Transaction");
        }

        transactionRepository.delete(transaction);
    }
}
