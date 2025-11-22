package com.ferb.expenseMoneyTracker.repository;

import com.ferb.expenseMoneyTracker.entity.User;
import com.ferb.expenseMoneyTracker.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    List<Wallet> findByOwner(User owner);
    @Query("select case when count(w)> 0 then true else false end from Wallet w where w.title = :title and w.owner.id = :ownerId")
    Boolean existsByOwnerAndWalletAndTitle(UUID ownerId, String title);
    Wallet findByIdAndOwner(UUID id, User owner);

}
