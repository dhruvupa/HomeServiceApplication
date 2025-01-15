package com.example.demo.Controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Model.SearchServices.SearchService;
import com.example.demo.Service.SearchService.SearchServiceService;

@Controller
@RequestMapping("/searchService")
public class SearchServiceController {
	
	@Autowired
    private SearchServiceService searchServiceService;

    // Method to fetch and display all services
    @GetMapping("/all-services")
    public String viewAllServices(Model model) throws SQLException{
        List<SearchService> services = searchServiceService.getAllServices();
        model.addAttribute("services", services);
        return "all-services"; // Thymeleaf template name
    }
}
