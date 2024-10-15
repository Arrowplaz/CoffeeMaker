package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.Service;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {
	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST
	 * API
	 */
	private MockMvc mvc;
	
	@Autowired
	private InventoryService service;

	@Autowired
	private WebApplicationContext context;

	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup () {
	    mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	@Transactional
	public void apiCoffeeTest() {
		try {
		String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
				.andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
		/* Figure out if the recipe we want is present */
		if ( !recipe.contains( "Mocha" ) ) {
		    final Recipe r = new Recipe();
		   
		    r.setPrice( 10 );
		    r.setName("Mocha");

		    mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
		    recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
					.andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
		    Assertions.assertTrue(recipe.contains("Mocha"));
		}
		}
		catch (Exception e) {
			
		}
		try {
			Ingredient coffee = new Ingredient("Coffee", 50);
			Ingredient chocolate = new Ingredient ("Chocolate", 50);
			Ingredient sugar = new Ingredient("Sugar", 50);
			Inventory i = new Inventory();
			i.addIngredient(coffee);
			i.addIngredient(chocolate);
			i.addIngredient(sugar);
			
			mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );
			i = service.getInventory();
			String chocolateInventory = i.getIngredient("Chocolate").getAmount().toString();
		    String inventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() )
					.andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
			Assertions.assertTrue(inventory.contains("\"chocolate\":" + chocolateInventory));
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
		
		
	}
	
}
