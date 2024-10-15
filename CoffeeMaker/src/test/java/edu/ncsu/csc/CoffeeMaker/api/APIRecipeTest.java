package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    @Autowired
    private RecipeService service;

    @Autowired
    private MockMvc       mvc;

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
       
        r.setPrice( 10 );
        r.setName( "Mocha" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
    }
    
    
    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {
        service.deleteAll();

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        final Recipe r2 = createRecipe( name, 50, 3, 1, 1, 0 );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }

  

    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
       

        return recipe;
    }
    
    @Test
    public void testDeleteRecipe() {
    	try {
			String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
					.andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
			/* Figure out if the recipe we want is present */
			final Recipe r = new Recipe();
			
			r.setPrice( 10 );
			r.setName("My tester");

			mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
					.content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
			recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
					.andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
			Assertions.assertTrue(recipe.contains("My tester"));


			mvc.perform( delete( "/api/v1/recipes/" + r.getName()));
			recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
					.andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
			Assertions.assertFalse(recipe.contains("My tester"));
		}
		catch (Exception e) {
			Assertions.fail("should not have thrown an exception");
		}
    }
    
    @Test
    @Transactional
    public void testGetSpecificRecipe() {
    	service.deleteAll();

        final Recipe r = new Recipe();
        
        r.setPrice( 10 );
        r.setName( "Mocha" );
        r.addIngredient("sugar", 5);
        
    	try {
            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
            
            String recipe = mvc.perform( get( "/api/v1/recipes/Mocha" ) ).andDo( print() )
					.andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
            Assertions.assertTrue(recipe.contains("Mocha"));
    	}
    	catch (Exception e) {
    		Assertions.fail("should not have thrown an exception");
    	}
    }
    
    
    @Test
    @Transactional
    public void testUpdateRecipe() {
    	service.deleteAll();   
    	final Recipe r = new Recipe();
        
        r.setPrice( 10 );
        r.setName( "Mocha" );
        r.addIngredient("sugar", 5);
        
        try {
            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
            
            String recipe = mvc.perform( get( "/api/v1/recipes/Mocha" ) ).andDo( print() )
					.andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
            Assertions.assertTrue(recipe.contains("Mocha"));
            }
        catch (Exception e) {
        		Assertions.fail("should not have thrown an exception");
        	}
        r.setPrice(5);
        r.addIngredient("Coffee", 3);
        
        try {
        	 mvc.perform( put( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                     .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
        	 String recipe2 = mvc.perform( get( "/api/v1/recipes/Mocha" ) ).andDo( print() )
     				.andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        	 Assertions.assertTrue(recipe2.contains("Coffee"));
        }
        catch(Exception e) {
        	Assertions.fail("should not have thrown an exception");
        }
        
       
        
    }
}