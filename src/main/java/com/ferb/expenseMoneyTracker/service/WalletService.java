package com.ferb.expenseMoneyTracker.service;

import com.ferb.expenseMoneyTracker.dto.CreateWalletRequest;
import com.ferb.expenseMoneyTracker.dto.CustomUserDetail;
import com.ferb.expenseMoneyTracker.entity.User;
import com.ferb.expenseMoneyTracker.entity.Wallet;
import com.ferb.expenseMoneyTracker.repository.UserRepository;
import com.ferb.expenseMoneyTracker.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WalletService {
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    UserService userService;

    public List<Wallet> findByOwnerId(User owner) {
        return walletRepository.findByOwner(owner);
    }

    public Boolean isWalletTitleExist(UUID ownerId, String walletName) {
        return walletRepository.existsByOwnerAndWalletAndTitle(ownerId, walletName);
    }

    public Wallet createNewWallet(CreateWalletRequest createWalletRequest, CustomUserDetail userDetail) {
        User owner = userService.findOneUserByEmail(userDetail.getUsername());

        Wallet wallet = Wallet.builder()
                .title(createWalletRequest.getTitle())
                .owner(owner)
                .balance(createWalletRequest.getBalance())
                .iconUrl(null)
                .build();

        return walletRepository.save(wallet);
    }

    public Wallet findByWalletId(UUID walletId, User owner) {
        return walletRepository.findByIdAndOwner(walletId, owner);
    }

    public Wallet updateWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public void deleteWallet(Wallet wallet) {
        walletRepository.delete(wallet);
    }
}
