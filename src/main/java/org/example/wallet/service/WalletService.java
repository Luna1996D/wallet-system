package org.example.wallet.service;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.wallet.model.Wallet;
import org.example.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final BonusService bonusService;

    public Wallet createWallet(String owner, BigDecimal amount) {
        Wallet wallet = new Wallet();
        wallet.setOwner(owner);
        wallet.setBalance(amount);
        return walletRepository.save(wallet);
    }
    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public Wallet getWalletByOwner(String owner) {
        return walletRepository.findByOwner(owner)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addBonus(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for bonus"));

        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(5)));
        walletRepository.save(wallet);

        throw new RuntimeException("Test bonus failure");
    }

    @Transactional
    public void withdraw(String owner, BigDecimal amount) {
        Wallet wallet = walletRepository.findByOwner(owner)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        BigDecimal newBalance = wallet.getBalance().subtract(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void topUp(String owner, BigDecimal amount) {
        Wallet wallet = walletRepository.findByOwner(owner)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        try {
            bonusService.addBonus(wallet.getId());
        } catch (Exception e) {
            System.out.println("Bonus transaction failed: " + e.getMessage());
        }
    }
}
