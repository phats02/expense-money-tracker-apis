package com.ferb.expenseMoneyTracker.repository;

import com.ferb.expenseMoneyTracker.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    @Query("SELECT w FROM Wallet w WHERE w.owner.id=:ownerId")
    List<Wallet> findByOwner(UUID ownerId);
    @Query("select case when count(w)> 0 then true else false end from Wallet w where w.title = :title and w.owner.id = :ownerId")
    Boolean existsByOwnerAndWalletAndTitle(UUID ownerId, String title);
    @Query("SELECT w FROM Wallet w WHERE w.owner.id=:ownerId and w.id=:id")
    Wallet findByIdAndOwner(UUID id, UUID ownerId);

}
