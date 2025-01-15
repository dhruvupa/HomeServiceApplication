package com.example.demo.DAORepo;

import com.example.demo.Service.ServiceProvider.GlobalContext;

import com.example.demo.Model.SearchServices.SearchService;
import com.example.demo.Model.SearchServices.SearchServiceRowMapper;
import com.example.demo.Model.ServiceProvider.ServiceProviderRowMapper;

import com.example.demo.Model.Booking.BookingRowMapper;


import com.example.demo.Model.ServiceProvider.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import com.example.demo.Model.Booking.ServiceProviderBookingDTO;


@Repository
public class ServiceProvider_Repository {

    @Autowired
    private GlobalContext globalcontext;

    @Autowired
    private JdbcTemplate jdbctemplate;

    @Autowired 
    private GlobalContext globalContext;
    
   
    private final ServiceProviderRowMapper serviceProviderRowMapper; 
    
    public ServiceProvider_Repository(ServiceProviderRowMapper serviceProviderRowMapper) {
    	this.serviceProviderRowMapper =serviceProviderRowMapper;
    }
    
    // getter for template
    public JdbcTemplate getJdbctemplate() {
        return jdbctemplate;
    }

    //setter for template
    public void setJdbctemplate(JdbcTemplate jdbctemplate) {
        this.jdbctemplate = jdbctemplate;
    }

    // method to find if srviceProvider exist while registering
    public List<Map<String, Object>> findServiceProvider(String email) {
        String query = "SELECT email FROM ServiceProvider WHERE LOWER(email) = LOWER(?)";
        return jdbctemplate.queryForList(query, new Object[]{email});
    }

    // Method to save service provider details
    public int AddServiceProvider(ServiceProvider serviceProvider) {
        // Encrypt the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(serviceProvider.getPassword());

        // Insert into ServiceProvider table
        String query1 = "INSERT INTO ServiceProvider (name, email, password, phone, city) VALUES (?, ?, ?, ?, ?)";
        jdbctemplate.update(query1, serviceProvider.getName(), serviceProvider.getEmail(), hashedPassword, serviceProvider.getPhoneno(), serviceProvider.getCity());

        // Fetch the newly inserted ServiceProvider ID
        String query2 = "SELECT LAST_INSERT_ID()";
        int serviceProviderId = jdbctemplate.queryForObject(query2, Integer.class);

//	        // Insert into Wallet table for the new ServiceProvider
//	        String query3 = "INSERT INTO Wallet (user_id, user_type, balance) VALUES (?, ?, ?)";
//	        jdbctemplate.update(query3, serviceProviderId, "SERVICE_PROVIDER", 0.0f);

        // Return the new ServiceProvider ID
        return serviceProviderId;
    }


    // Method to validate login
    public boolean ValidateLogin(String email, String password) {
        String query = "SELECT password, provider_id FROM ServiceProvider WHERE email = LOWER(?)";
        List<Map<String, Object>> results = jdbctemplate.queryForList(query, new Object[] {email});

        if (results.isEmpty())
        {
            return false;
        }

        String storedHashedPassword = (String) results.get(0).get("password");
        String serviceProviderId =   results.get(0).get("provider_id").toString();
        

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean passwordMatches = passwordEncoder.matches(password, storedHashedPassword);
        if (passwordMatches) {
            // Set serviceProviderId in GlobalContext on successful login
            globalcontext.setServiceProviderId(serviceProviderId);
        }
        return passwordMatches;
    }



    public int addServices(SearchService searchService) {
        String query = """
            INSERT INTO searchservice 
            ( provider_id, name, price, status, skill, category, rating) 
            VALUES 
            ( 
             (SELECT provider_id FROM serviceprovider WHERE name = ? LIMIT 1), 
             ?, ?, ?, ?, ?, ?)
        """;
        
        return jdbctemplate.update(query, 
            searchService.getProviderName(), // This is used in the subquery for matching provider name
            searchService.getProviderName(),          
            searchService.getPrice(), 
            searchService.getStatus(), 
            searchService.getSkill(), 
            searchService.getCategory(), 
            searchService.getRating());
    }
   
    @SuppressWarnings("deprecation")
	public List<SearchService> getServices() {
    	String query = "SELECT ss.service_id, ss.provider_id, ss.skill, ss.rating, ss.price, ss.status, " +
                "sp.name AS provider_name, sp.city, ss.category " +
                "FROM searchservice ss " +
                "JOIN serviceprovider sp ON ss.provider_id = sp.provider_id " +
                "WHERE ss.provider_id = ?";

        //String query = "SELECT price, name, service_id description FROM searchservice WHERE provider_id = ?";
        //List<Map<String, Object>> results = jdbctemplate.queryForList(query, globalContext.getServiceProviderId(), new SearchServiceRowMapper());
        return jdbctemplate.query(query, new Object[]{globalContext.getServiceProviderId()}, new SearchServiceRowMapper());

    }
    
    @SuppressWarnings("deprecation")
	public List<ServiceProvider> getServiceProviderServices() {
    	String query = "SELECT  ss.provider_id, ss.status, " +
                "sp.name AS provider_name, sp.city, sp.email " +
                "FROM searchservice ss " +
                "JOIN serviceprovider sp ON ss.provider_id = sp.provider_id " +
                "WHERE ss.provider_id = ?";

       // return jdbctemplate.query(query, new Object[]{globalContext.getServiceProviderId()}, new ServiceProviderRowMapper());
        return jdbctemplate.query(
                query, 
                new Object[]{globalContext.getServiceProviderId()}, 
                serviceProviderRowMapper // Use injected row mapper
            );

    }


    public int deleteSelectedServiceByID(String providerName, String ServiceId, String ServiceProviderId) {
    	String query = "DELETE FROM `searchservice` WHERE (`service_id` = ?);\r\n"
    			+ "";

        //String query = "SELECT price, name, service_id description FROM searchservice WHERE provider_id = ?";
    	int rowsAffected = jdbctemplate.update(query, ServiceId);
        return rowsAffected;
    }


    public int updateAndSave(SearchService searchService, String ServiceProviderId) {
    	 String query = "UPDATE searchservice " +
                 "SET price = ?,  category = ?, rating = ? " +
                 "WHERE service_id = ? AND provider_id = ?";
    	 String provider_id = globalContext.getServiceProviderId();
    	return jdbctemplate.update(query, 		
    			searchService.getPrice(),   			
    			searchService.getCategory(), 
    			searchService.getRating(),
    			searchService.getServiceId(), 
    			provider_id
    			);
    }
    
    public List<SearchService>  getServiceForModify( String providerName, String ServiceId, String ServiceProviderId) {
    	
    	String query = "SELECT ss.service_id, ss.provider_id, ss.skill, ss.rating, ss.price, ss.status, " +
                "sp.name AS provider_name, sp.city, ss.category " +
                "FROM searchservice ss " +
                "JOIN serviceprovider sp ON ss.provider_id = sp.provider_id " +
                "WHERE ss.provider_id = ? and ss.service_id = ?";

    	 return jdbctemplate.query(query, new Object[]{globalContext.getServiceProviderId() ,ServiceId}, new SearchServiceRowMapper());

    	
    	/*String query = "SELECT ss.service_id, ss.provider_id, ss.skill, ss.rating, ss.price, ss.status, " +
                "sp.name AS provider_name, sp.city, ss.category " +
                "FROM searchservice ss " +
                "JOIN serviceprovider sp ON ss.provider_id = sp.provider_id " +
                "WHERE ss.provider_id = ? and ss.service_id = ?";
*/
    	
    }

    public void updateBookingStatus(String bookingId, String status) {
        String query = "UPDATE booking SET status = ? WHERE booking_id = ?";
        jdbctemplate.update(query, status, bookingId);
    }
    
    public void updateServicsStatus(ServiceProvider provider) {
        String query = "UPDATE searchservice SET status = ? WHERE provider_id = ?";
        jdbctemplate.update(query, provider.getStateName(), provider.getProviderId());
    }

    public List<ServiceProviderBookingDTO> findBookedServices(String serviceProviderId) {
        String query = """
                SELECT 
                    b.booking_id AS booking_id,
                    b.status AS booking_status,
                    b.booking_date,
                    c.name AS customer_name,
                    ss.skill
                FROM 
                    booking b
                JOIN 
                    customer c ON b.customer_id = c.customer_id
                JOIN 
                    searchservice ss ON b.service_id = ss.service_id
                WHERE
                    ss.provider_id = ?
                    AND b.status IN ('Accepted', 'Pending');
                """;

        return jdbctemplate.query(query, new Object[]{serviceProviderId}, new BookingRowMapper());
    }

    public List<ServiceProviderBookingDTO> getPastBookings(String providerId) {
        String sql = "SELECT b.booking_id, c.name AS customer_name, ss.name AS skill, " +
                "b.booking_date, b.status AS booking_status " +
                "FROM Booking b " +
                "JOIN Customer c ON b.customer_id = c.customer_id " +
                "JOIN SearchService ss ON b.service_id = ss.service_id " +
                "WHERE ss.provider_id = ? AND b.status IN ('Completed', 'Rejected') " +
                "ORDER BY b.booking_date DESC";

        return jdbctemplate.query(sql, new Object[]{providerId}, new BookingRowMapper());
    }


    public List<ServiceProvider> getServiceProvider(String serviceId) {
        String query = "SELECT sp.provider_id, sp.city, sp.email, sp.name AS provider_name, ss.status " +
                "FROM ServiceProvider sp " +
                "JOIN SearchService ss ON sp.provider_id = ss.provider_id " +
                "WHERE ss.service_id = ?;";

        return jdbctemplate.query(query, new Object[]{serviceId}, new ServiceProviderRowMapper());
    }
}