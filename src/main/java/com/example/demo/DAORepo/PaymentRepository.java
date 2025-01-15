package com.example.demo.DAORepo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepository {

    @Autowired
    private JdbcTemplate queryTemplate;

    public void savePayment(String bookingId, float amount, String status, String timestamp) {
        String sql = "INSERT INTO payment (booking_id, amount, status, timestamp) VALUES (?, ?, ?, ?)";
        queryTemplate.update(sql, bookingId, amount, status, timestamp);
    }

    public List<Map<String, Object>> getBookingCountByCustomerId(int customerId) {
        String sql = "SELECT COUNT(*) FROM booking WHERE customer_id = ?";
        return queryTemplate.queryForList(sql, new Object[] {customerId});
    }
    
    //
    public int getServiceProviderIdByServiceId(String serviceId) {
        String sql = "SELECT provider_id FROM searchservice WHERE service_id = ?";
        
        // Query for the service provider ID
        List<Map<String, Object>> result = queryTemplate.queryForList(sql, new Object[] {serviceId});
        
        // If no result is found, throw an exception or handle the case as needed
        if (result.isEmpty()) {
            throw new IllegalArgumentException("No service provider found for service ID: " + serviceId);
        }
        
        // Get the first result (assuming there's only one service provider per service_id)
        Map<String, Object> row = result.get(0);
        
        // Return the service_provider_id as an integer
        return (Integer) row.get("provider_id");
    }




    public String createBooking(int customerId, String serviceId) {
        String bookingId = "BOOK" + System.currentTimeMillis(); // Generate unique booking ID
        String sql = "INSERT INTO booking (booking_id, customer_id, service_id, booking_date, status, payment_status) VALUES (?, ?, ?, NOW(), ?, ?)";
        queryTemplate.update(sql, bookingId, customerId, serviceId, "PENDING", "UNPAID");
        return bookingId;
    }

    public void updateBookingPaymentStatus(String bookingId) {
        String sql = "UPDATE booking SET payment_status = 'PAID' WHERE booking_id = ?";
        queryTemplate.update(sql, bookingId);
    }

}
