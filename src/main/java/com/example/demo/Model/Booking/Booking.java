package com.example.demo.Model.Booking;

import org.springframework.stereotype.Component;

@Component
public class Booking {

    protected String booking_id;
    protected String customer_id;
    protected String service_id;
    protected String booking_date;
    protected String status;
    protected String payement_status;
    protected String skill;
    
    public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayement_status() {
        return payement_status;
    }

    public void setPayement_status(String payement_status) {
        this.payement_status = payement_status;
    }
}
