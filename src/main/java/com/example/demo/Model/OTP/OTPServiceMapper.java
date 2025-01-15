package com.example.demo.Model.OTP;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OTPServiceMapper implements RowMapper<OTPServiceModel> {

    @Override
    public OTPServiceModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        OTPServiceModel otpService = new OTPServiceModel();

        otpService.setOtp_id(rs.getString("otp_id"));
        otpService.setBooking_id(rs.getString("booking_id"));
        otpService.setOtp_code(rs.getString("otp_code"));
        otpService.setTimestamp(rs.getString("timestamp"));

        return otpService;
    }
}
