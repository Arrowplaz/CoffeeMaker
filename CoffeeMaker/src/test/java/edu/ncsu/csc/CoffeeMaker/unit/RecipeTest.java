package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient("sugar", 1);
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( -1 );
        

        final List<Recipe> recipes = List.of( r1, r2 );

        try {
            service.saveAll( recipes );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        Recipe r1 = createRecipe(name, 5);
        r1.addIngredient("milk", 3);
        r1.addIngredient("caramel", 2);
        r1.addIngredient("coffee", 3);
        

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddNegativeIngredient () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50 );
        

        try {
        	r1.addIngredient("coffee", -1);
            Assertions.fail("can't add ingredient with negative amount");
        }
        catch (IllegalArgumentException e) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50 );
        
        try {
        	r1.addIngredient("milk", 0);

            Assertions.fail("can't add ingredient with 0 amount");
        }
        catch ( IllegalArgumentException e ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50 );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50 );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }
    
    @Test
    @Transactional
    public void testEditIngredient () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50 );
        r1.addIngredient("sugar", 2);
        
        service.save( r1 );

        r1.setIngredient("sugar", 5);
        
        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 5, retrieved.getIngredient("sugar").getAmount() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }
    
    @Test
    @Transactional
    public void testDeleteIngredient() {
    	Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

    	final Recipe r1 = createRecipe( "Coffee", 50 );
    	r1.addIngredient("sugar", 3);
    	
    	Assertions.assertEquals(r1.getIngredient("sugar").getName(), "sugar");
    	
    	Assertions.assertEquals(r1.getIngredient("sugar").getAmount(), 3);
    	
    	r1.deleteIngredient("sugar");
    	
    	Assertions.assertNull(r1.getIngredient("sugar"));
    	
    	boolean delete = r1.deleteIngredient("ingredient not in the recipe");
    	
    	Assertions.assertFalse(delete);
    	
    }
    
    @Test
    @Transactional
    public void testGetIngredients() {
    	final Recipe r1 = createRecipe( "Coffee", 50 );
    	r1.addIngredient("sugar", 5);
    	r1.addIngredient("milk", 3);
    	r1.addIngredient("chocolate", 6);
    	
    	ArrayList<Ingredient> expected = new ArrayList<Ingredient>();
    	expected.add(new Ingredient("sugar", 5));
    	expected.add(new Ingredient("milk", 3));
    	expected.add(new Ingredient("chocolate", 6));
    	
    	
    	//check if string equal each other because ingredient objects are harder to compare
    	Assertions.assertTrue(expected.toString().equals(r1.getIngredients().toString()), "expected: " + expected.toString() + ", r1: " + 
    			r1.getIngredients().toString());
    }

    private Recipe createRecipe ( final String name, final Integer price) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        
        return recipe;
    }
    
    @Test
    @Transactional
    public void testUpdateRecipe() {
    	final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient("sugar", 5);
        
        service.save(r1);
        
        final Recipe r2 = new Recipe();
        r2.setName( "Black Coffee" );
        r2.setPrice( 1 );
        r1.addIngredient("milk", 6);
       
        r1.updateRecipe(r2);

        service.save(r1);

        Recipe retrieved = service.findByName("Black Coffee");
        
        Assertions.assertEquals(retrieved.getPrice(), r2.getPrice());
        Assertions.assertEquals(retrieved.getIngredients().toString(), r2.getIngredients().toString());
       
    }
    
    @Test
    public void testInvalidNameAndIngredientInput() {
    	try {
    		Recipe r1 = createRecipe(null, 3);
    		Assertions.fail("cannot add null name");
    	} catch (IllegalArgumentException e) {
    		//exception caught as expected
    	}
    	
    	try {
    		Recipe r1 = createRecipe("coffee", 5);
    		r1.addIngredient("sugar", 4);
    		r1.setIngredient("sugar", -1);
    	} catch (IllegalArgumentException e) {
    		//exception caught as expected
    	}
    }

    
    
    @Test
    @Transactional
    public void testCheckRecipe() {
    	Recipe r1 = createRecipe("coffee", 1);
    	
    	Assertions.assertTrue(r1.checkRecipe());
    	
    	r1.addIngredient("coffee", 2);
    	
    	Assertions.assertFalse(r1.checkRecipe());
    }
    
    @Test
    @Transactional
    public void testEquals() {
    	final Recipe r1 = new Recipe();
        r1.setName( "recipe" );        
        
        Recipe r2 = null;
        Assertions.assertFalse(r1.equals(r2));
        
        Inventory ivt = new Inventory();
        Assertions.assertFalse(r1.equals(ivt));
        
        r2 = new Recipe ();
        r2.setName("recipe");
        Assertions.assertTrue(r1.equals(r2));
        
        r2.setName("Cool Recipe");
        Assertions.assertFalse(r1.equals(r2));
        
        r1.setName("Even Cooler Recipe");
        Assertions.assertFalse(r1.equals(r2));
    }
}
