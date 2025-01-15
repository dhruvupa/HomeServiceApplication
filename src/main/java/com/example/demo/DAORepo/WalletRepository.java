package com.example.demo.DAORepo;

import com.example.demo.Model.Wallet.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class WalletRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map the Wallet result set to Wallet object
    private final RowMapper<Wallet> walletRowMapper = (rs, rowNum) -> {
        Wallet wallet = new Wallet();
        wallet.setId(rs.getLong("id"));
        wallet.setUserId(rs.getInt("user_id"));
        wallet.setUserType(rs.getString("user_type"));
        wallet.setBalance(rs.getFloat("balance"));
        
        return wallet;
    };
    
    // Insert a new wallet
    public void createWallet(Wallet wallet) {
        String sql = "INSERT INTO wallet (user_id, balance, user_type) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, wallet.getUserId(), wallet.getBalance(), wallet.getUserType());
    }

    
    // Fetch wallet by user ID
    public Wallet findByUserIdAndType(int userId, String userType) {
        String sql = "SELECT * FROM wallet WHERE user_id = ? AND user_type = ?";
        return jdbcTemplate.queryForObject(sql, walletRowMapper, userId, userType);
    }
    

    // Fetch wallet balance by user ID
    public float getWalletBalanceByUserId(int userId, String userType) {
        String sql = "SELECT balance FROM wallet WHERE user_id = ?  AND user_type = ?";
        return jdbcTemplate.queryForObject(sql, Float.class, userId, userType);
    }
    

    // Update wallet balance by user ID
    public void updateWalletBalance(int userId, String userType, float newBalance) {
        String sql = "UPDATE wallet SET balance = ? WHERE user_id = ? AND user_type = ?";
        jdbcTemplate.update(sql, newBalance, userId, userType);
    }
    
}
