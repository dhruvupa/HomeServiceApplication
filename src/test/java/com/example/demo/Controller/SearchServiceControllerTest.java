package com.example.demo.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.example.demo.Model.SearchServices.SearchService;
import com.example.demo.Service.SearchService.SearchServiceService;

class SearchServiceControllerTest {

	@InjectMocks
	private SearchServiceController searchServiceController;

	@Mock
	private Model model;

	@Mock
	private SearchServiceService searchServiceService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testViewAllServices() throws SQLException {
		SearchService searchService = new SearchService();

		List<SearchService> services = new ArrayList<>();

		when(searchServiceService.getAllServices()).thenReturn(services);
		String View = searchServiceController.viewAllServices(model);
		assertEquals("all-services", View);
	}

}
