package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;

import java.util.List;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {

    @Autowired
    private RecipeService recipeService;

    @Test
    @Transactional
    public void testRecipes () {
        recipeService.deleteAll();

        Recipe r = new Recipe();

        r.setName( "Special Drink" );

        

        r.setPrice( 2 );

        recipeService.save( r );

        List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        Recipe dbRecipe = dbRecipes.get( 0 );

        /*
         * Note we are _not_ using the `Recipe.equals(Object)` method here
         * because it only checks the name!
         */
        assertEquals( r.getName(), dbRecipe.getName() );
       
        assertEquals( r.getPrice(), dbRecipe.getPrice() );

        Recipe dbRecipeByName = recipeService.findByName( "Special Drink" );

        

        dbRecipe.setPrice( 15 );
        
        recipeService.save( dbRecipe );
        assertEquals( 1, recipeService.count() );

        assertEquals( 15, (int) ( (Recipe) recipeService.findAll().get( 0 ) ).getPrice() );

    }
    
    @Test
    @Transactional
    public void testDeleteRecipe() {
    	recipeService.deleteAll();

        Recipe r1 = new Recipe();
        r1.setName( "Special Drink" );
        
        r1.setPrice( 2 );
        
        Recipe r2 = new Recipe();
        r2.setName( "Very Special Drink" );
       
        r2.setPrice( 2 );
        
        Recipe r3 = new Recipe();
        r3.setName( "Incredibly Special Drink" );
        
        r3.setPrice( 2 );
        
        Recipe r4 = new Recipe();
        r3.setName("Fake Recipe");
        
        r3.setPrice( 2 );

        
        recipeService.save(r1);
        recipeService.save(r2);
        recipeService.save(r3);
        
        recipeService.delete(r4);
        
        assertEquals(recipeService.count(), 3);
        
        assertEquals(recipeService.findByName("Special Drink"), r1);
        
        recipeService.delete(r1);
        
        assertEquals(recipeService.findByName("Special Drink"), null);
        assertEquals(recipeService.count(), 2);
        
        recipeService.deleteAll();
        
        assertEquals(recipeService.findByName("Very Special Drink"), null);
        assertEquals(recipeService.count(), 0);
    }
}