package com.example.demo.Model.Booking;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingRowMapper implements RowMapper<ServiceProviderBookingDTO> {
    @Override
    public ServiceProviderBookingDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ServiceProviderBookingDTO serviceProviderBookingDTO = new ServiceProviderBookingDTO();
        serviceProviderBookingDTO.setBookingId(rs.getString("booking_id"));
        serviceProviderBookingDTO.setBookingStatus(rs.getString("booking_status"));
        serviceProviderBookingDTO.setBookingDate(rs.getString("booking_date"));
        serviceProviderBookingDTO.setCustomerName(rs.getString("customer_name"));
        serviceProviderBookingDTO.setSkill(rs.getString("skill"));
        return serviceProviderBookingDTO;
    }
}
