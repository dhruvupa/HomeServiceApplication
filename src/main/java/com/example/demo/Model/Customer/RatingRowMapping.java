package com.example.demo.Model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.Model.Booking.Booking;
import com.example.demo.Model.Booking.ServiceProviderBookingDTO;

public class RatingRowMapping implements RowMapper<Booking> {
    @Override
    public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
        Booking booking = new Booking();
        booking.setBooking_id(rs.getString("booking_id"));
        booking.setStatus(rs.getString("booking_status"));
        booking.setBooking_date(rs.getString("booking_date"));
        booking.setService_id(rs.getString("service_id"));
        booking.setSkill(rs.getString("skill"));;
        return booking;
    }
}
