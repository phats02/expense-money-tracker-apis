package com.ferb.expenseMoneyTracker.controller;

import com.ferb.expenseMoneyTracker.dto.CreateWalletRequest;
import com.ferb.expenseMoneyTracker.dto.CustomUserDetail;
import com.ferb.expenseMoneyTracker.dto.SuccessResponse;
import com.ferb.expenseMoneyTracker.dto.UpdateWalletRequest;
import com.ferb.expenseMoneyTracker.entity.Wallet;
import com.ferb.expenseMoneyTracker.exception.FieldAlreadyExisted;
import com.ferb.expenseMoneyTracker.exception.NotFound;
import com.ferb.expenseMoneyTracker.service.WalletService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name="Wallet apis")
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    WalletService walletService;

    @GetMapping("")
    public SuccessResponse<List<Wallet>> getAllWalletsByUserId(@AuthenticationPrincipal CustomUserDetail userDetail) {
        return new SuccessResponse<>(walletService.findByOwnerId(userDetail.getUserId()));
    };

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Wallet> createWallet(@AuthenticationPrincipal CustomUserDetail userDetail,
                                                @Valid @RequestBody CreateWalletRequest createWalletRequest){
        if (walletService.isWalletTitleExist(
                userDetail.getUserId(),
                createWalletRequest.getTitle())){
            throw new FieldAlreadyExisted("Wallet title");
        }
        return new SuccessResponse<>(walletService.createNewWallet(createWalletRequest, userDetail));
    }

    @GetMapping("/isTitleAvailable")
    public SuccessResponse<Boolean> isWalletTitleAvailable(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestParam(name = "title", required = true) String title) {
        return new SuccessResponse<Boolean>(!walletService.isWalletTitleExist(userDetail.getUserId(), title));
    }

    @PutMapping("/{id}")
    public SuccessResponse<Wallet> updateWallet(@PathVariable UUID id,
                                                 @AuthenticationPrincipal CustomUserDetail userDetail,
                                                 @Valid @RequestBody UpdateWalletRequest updateWalletRequest) {
        Wallet wallet = walletService.findByWalletId(id, userDetail.getUserId());

        if (wallet == null) {
            throw new NotFound("Wallet");
        }


        if (updateWalletRequest.getTitle() != null) {
            if (!Objects.equals(wallet.getTitle(), updateWalletRequest.getTitle())) {
                if (walletService.isWalletTitleExist(
                        userDetail.getUserId(),
                        updateWalletRequest.getTitle())){
                    throw new FieldAlreadyExisted("Wallet title");
                }
            }

            wallet.setTitle(updateWalletRequest.getTitle());
        }

        if (updateWalletRequest.getDescription() != null) {
            wallet.setDescription(updateWalletRequest.getDescription());
        }


        return new SuccessResponse<>(walletService.updateWallet(wallet));
    }

    @DeleteMapping("/{id}")
    public SuccessResponse<Object> deleteWallet(@PathVariable UUID id,
                                                @AuthenticationPrincipal CustomUserDetail userDetail) {
        Wallet wallet = walletService.findByWalletId(id, userDetail.getUserId());

        if (wallet == null) {
            throw new NotFound("Wallet");
        }

        walletService.deleteWallet(wallet);

        return new SuccessResponse<>(null);
    }
}
