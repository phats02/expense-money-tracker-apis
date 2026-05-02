package com.ferb.expenseMoneyTracker.service;

import com.ferb.expenseMoneyTracker.dto.CreateTransactionRequest;
import com.ferb.expenseMoneyTracker.dto.UpdateTransactionRequest;
import com.ferb.expenseMoneyTracker.entity.Category;
import com.ferb.expenseMoneyTracker.entity.Transaction;
import com.ferb.expenseMoneyTracker.entity.User;
import com.ferb.expenseMoneyTracker.entity.Wallet;
import com.ferb.expenseMoneyTracker.enums.TransactionType;
import com.ferb.expenseMoneyTracker.exception.NotFound;
import com.ferb.expenseMoneyTracker.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Transactional
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
                .type(createTransactionRequest.getType())
                .build();

        BigDecimal newBalance = this.calcNewWalletAmount(wallet, newTransaction);
        if (newBalance != null) {
            wallet.setBalance(newBalance);
            walletService.updateWallet(wallet);
        }


        return transactionRepository.save(newTransaction);
    }

    @Transactional
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

        Wallet newWallet = null;
        if (updateTransactionRequest.getWalletId() != null) {
            newWallet = walletService.findByWalletId(updateTransactionRequest.getWalletId(), owner);
            if (newWallet == null) throw new NotFound("Wallet");
            transaction.setWallet(newWallet);
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

        if (updateTransactionRequest.getType() != null) {
            transaction.setType(updateTransactionRequest.getType());
        }


        Wallet oldWallet = transaction.getWallet();
        BigDecimal rollbackAmount = this.rollbackWalletAmount(oldWallet, transaction);
        if (rollbackAmount != null) {
            oldWallet.setBalance(rollbackAmount);
            walletService.updateWallet(oldWallet);
        }

        if (updateTransactionRequest.getAmount() != null) {
            transaction.setAmount(updateTransactionRequest.getAmount());
        }

        BigDecimal newAmount = this.calcNewWalletAmount(newWallet, transaction);
        if (newWallet != null) {
            newWallet.setBalance(newAmount);
            walletService.updateWallet(newWallet);
        }


        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(UUID id, User owner) {
        Transaction transaction = transactionRepository.getByIdAndOwner(id, owner);
        if (transaction == null) {
            throw  new NotFound("Transaction");
        }
        Wallet wallet = transaction.getWallet();

        BigDecimal newBalance = this.rollbackWalletAmount(wallet, transaction);

        if (newBalance != null) {
            wallet.setBalance(newBalance);
            walletService.updateWallet(wallet);
        }

        transactionRepository.delete(transaction);
    }

    private BigDecimal calcNewWalletAmount(Wallet wallet, Transaction transaction) {
        BigDecimal newBalance = null;
        switch(transaction.getType()) {
            case TransactionType.expense:
                newBalance = wallet.getBalance().subtract(transaction.getAmount());
                break;
            case TransactionType.income:
                newBalance = wallet.getBalance().add(transaction.getAmount());
            default:
                break;
        }

        return newBalance;
    }

    private BigDecimal rollbackWalletAmount(Wallet wallet, Transaction transaction) {
        BigDecimal newBalance = null;
        switch(transaction.getType()) {
            case TransactionType.expense:
                newBalance = wallet.getBalance().add(transaction.getAmount());
                break;
            case TransactionType.income:
                newBalance = wallet.getBalance().subtract(transaction.getAmount());
            default:
                break;
        }

        return newBalance;
    }
}
