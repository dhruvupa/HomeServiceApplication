package com.example.demo.DAORepo;

import com.example.demo.Model.OTP.OTPServiceModel;
import com.example.demo.Model.OTP.OTPServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OTPRepository {

    @Autowired
    private JdbcTemplate jdbctemplate;

    public void updateBookingStatusTOComplete(String bookingId, String status) {
        String sql = "UPDATE booking SET status = ? WHERE booking_id = ?";
        jdbctemplate.update(sql, status, bookingId);
    }

    public OTPServiceModel findOTPByCode(String otpCode, String bookingId) {
        String sql = "SELECT * FROM OTPService WHERE otp_code = ? AND booking_id = ?";
        try {
            return jdbctemplate.queryForObject(sql, new OTPServiceMapper(), otpCode, bookingId);
        } catch (Exception e) {
            return null; // OTP not found
        }
    }

    // Method to save OTP to the database
    public void saveOTP(OTPServiceModel otpService) {
        String sql = "INSERT INTO OTPService (booking_id, otp_code, timestamp) VALUES (?, ?, ?)";
        jdbctemplate.update(sql, otpService.getBooking_id(), otpService.getOtp_code(), otpService.getTimestamp());
    }


}
