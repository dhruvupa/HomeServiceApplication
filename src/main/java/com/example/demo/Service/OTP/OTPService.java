package com.example.demo.Service.OTP;

import com.example.demo.DAORepo.OTPRepository;
import com.example.demo.Model.OTP.OTPServiceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OTPService {

    @Autowired
    private final OTPRepository otpRepository;

    public OTPService(OTPRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public void updateBookingStatusToCompleted(String bookingId) {
        // Use the repository to update the booking status
        otpRepository.updateBookingStatusTOComplete(bookingId, "Completed");
    }


    public boolean verifyOTP(String otpCode, String bookingId) {
        OTPServiceModel otpService = otpRepository.findOTPByCode(otpCode, bookingId);
        return otpService != null;
    }

    public String generateAndStoreOTP(String bookingId) {
        // Generate a 8-digit OTP
        String OTP = "OTP" + System.currentTimeMillis();
        String otpCode = OTP.substring(OTP.length() - 8);

        //Create OTPServiceModel object and store it in the database
        OTPServiceModel otpService = new OTPServiceModel();

        otpService.setBooking_id(bookingId);
        otpService.setOtp_code(otpCode);
        otpService.setTimestamp(java.time.LocalDateTime.now().toString()); // store the current timestamp

        // Save OTP to the database
        otpRepository.saveOTP(otpService);

        return otpCode;

    }


}


