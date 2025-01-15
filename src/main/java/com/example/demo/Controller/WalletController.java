package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Model.Wallet.Wallet;
import com.example.demo.Service.WalletService.WalletService;

@RequestMapping("/wallet")
public class WalletController {
	
	@Autowired
	private WalletService walletService;

    @GetMapping("/{userId}/{userType}")
    public Wallet getWallet(@PathVariable int userId, @PathVariable String userType) {
        return walletService.getWalletByUserIdAndType(userId, userType);
    }
    
    @PostMapping("/create")
    public String createWallet(@RequestParam int userId, @RequestParam String userType, @RequestParam float initialBalance) {
        walletService.createWallet(userId, userType, initialBalance);

        return "Wallet created successfully!";
    }


    @PostMapping("/addFunds")
    public String addFunds(@RequestParam int userId, @RequestParam String userType, @RequestParam float amount) {
        walletService.updateWalletBalance(userId, amount, userType);
        return "Funds added successfully!";
    }
}