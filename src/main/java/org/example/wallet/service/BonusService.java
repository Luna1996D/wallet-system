package org.example.wallet.service;

import lombok.RequiredArgsConstructor;
import org.example.wallet.model.Wallet;
import org.example.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BonusService {
    private final WalletRepository walletRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addBonus(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for bonus"));

        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(5)));
        walletRepository.save(wallet);

        throw new RuntimeException("Test bonus failure");
    }
}

