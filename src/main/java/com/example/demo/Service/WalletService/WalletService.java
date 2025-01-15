package com.example.demo.Service.WalletService;

import com.example.demo.DAORepo.WalletRepository;
import com.example.demo.Model.Wallet.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;
    
    // Create a new wallet for a user
    public void createWallet(int userId, String userType, float initialBalance) {
    	Wallet wallet = new Wallet();
    	
    	wallet.setUserId(userId);
    	wallet.setUserType(userType);
    	wallet.setBalance(initialBalance);
    	
        walletRepository.createWallet(wallet);
    }
    
    
    public Wallet getWalletByUserIdAndType(int userId, String userType) {
        Wallet wallet = walletRepository.findByUserIdAndType(userId, userType);
        
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for user ID: " + userId + " and type: " + userType);
        }
        
        return wallet;
    }

    public void updateWalletBalance(int userId, float amount, String userType) {
        Wallet wallet = walletRepository.findByUserIdAndType(userId, userType);
        
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for user ID: " + userId + " and type: " + userType);
        }
        
        float newBalance = wallet.getBalance() + amount; // Add amount to the existing balance
        
        walletRepository.updateWalletBalance(userId, userType, newBalance);
    }
    

    public boolean deductFromWallet(int userId, float amount, String userType) {
        Wallet wallet = walletRepository.findByUserIdAndType(userId, userType);
        
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for user ID: " + userId);
        }
        
        if (wallet.getBalance() < amount) {
            return false; // Insufficient balance
        }
        
        float newBalance = wallet.getBalance() - amount; // Deduct amount from the existing balance
        walletRepository.updateWalletBalance(userId, userType, newBalance);
        
        return true;
    }
    
    
    // Get wallet balance by user ID
    public float getWalletBalanceByUserId(int userId, String userType) {
        Wallet wallet = walletRepository.findByUserIdAndType(userId, userType);
        
        if (wallet != null) {
            return wallet.getBalance();
        }
        
        return 0.0f; // Return 0 if no wallet is found for the user
    }
    
  
}
