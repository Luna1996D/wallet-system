package org.example.wallet.controller;

import org.example.wallet.service.WalletService;
import org.example.wallet.dto.CreateWalletRequest;
import org.example.wallet.dto.WalletRequest;
import org.example.wallet.model.Wallet;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<List<Wallet>> getAllWallets() {
        List<Wallet> wallets = walletService.getAllWallets();
        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/{owner}")
    public ResponseEntity<Wallet> getWalletByOwner(@PathVariable String owner) {
        Wallet wallet = walletService.getWalletByOwner(owner);
        return ResponseEntity.ok(wallet);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        Wallet wallet = walletService.createWallet(request.getOwner(), request.getAmount());
        return ResponseEntity.ok(wallet);
    }

    @PutMapping("/{owner}/topup")
    public ResponseEntity<String> topUpWallet(@PathVariable String owner, @Valid @RequestBody WalletRequest request) {
        walletService.topUp(owner, request.getAmount());
        return ResponseEntity.ok("Balance successfully topped up.");
    }

    @PutMapping("/{owner}/withdraw")
    public ResponseEntity<String> withdrawWallet(@PathVariable String owner, @Valid @RequestBody WalletRequest request) {
        walletService.withdraw(owner, request.getAmount());
        return ResponseEntity.ok("Wallet withdrawn successfully");
    }
}
