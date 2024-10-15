package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.ArrayList;

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
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        
        ingredients.add(new Ingredient("sugar", 500));
        ingredients.add(new Ingredient("coffee", 500));
        ingredients.add(new Ingredient("milk", 500));
        
        ivt.addIngredients(ingredients);
        
        inventoryService.save( ivt );
    }
    
    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory ivt = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Black Coffee" );
        recipe.setPrice( 5 );
        recipe.addIngredient("sugar", 2);
        recipe.addIngredient("coffee", 5);

        ivt.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Ingredient sugar = ivt.getIngredient("sugar");
        Ingredient coffee = ivt.getIngredient("coffee");
        Ingredient milk = ivt.getIngredient("milk");
        
        Assertions.assertEquals( 498, sugar.getAmount());
        Assertions.assertEquals( 495, coffee.getAmount() );
        Assertions.assertEquals( 500, milk.getAmount() );
    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();
        
        ivt.updateIngredient("sugar", 50);
        ivt.updateIngredient("coffee", 50);       
        ivt.updateIngredient("milk", 50); 

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );
        
        Ingredient sugar = ivt.getIngredient("sugar");
        Ingredient coffee = ivt.getIngredient("coffee");
        Ingredient milk = ivt.getIngredient("milk");

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 550, (int) sugar.getAmount(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 550, (int) coffee.getAmount(),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 550, (int) milk.getAmount(),
                "Adding to the inventory should result in correctly-updated values sugar" );

    }
    
    @Test
    @Transactional
    public void testToString() {
    	final Inventory ivt = inventoryService.getInventory();
    	Assertions.assertEquals("[sugar: 500, coffee: 500, milk: 500]", ivt.toString());
    }
    
    @Test
    @Transactional
    public void testSetAndGetID() {
    	final Inventory ivt = inventoryService.getInventory();
    	ivt.setId((long) 543);
    	Assertions.assertEquals((long) 543, ivt.getId());
    }

    @Test
    @Transactional
    public void testNotEnoughForRecipe() {
    	ArrayList<Ingredient> ingredients2 = new ArrayList<Ingredient>();
    	
    	ingredients2.add(new Ingredient("sugar", 1));
    	ingredients2.add(new Ingredient("coffee", 1));
    	
    	final Inventory ivt = new Inventory(ingredients2);
        final Recipe recipe = new Recipe();
        
        recipe.setName( "Black Coffee" );
        recipe.setPrice( 5 );
        recipe.addIngredient("sugar", 2);
        recipe.addIngredient("coffee", 5);
       
        Assertions.assertFalse(ivt.useIngredients(recipe));
        
        
    }


    @Test
    @Transactional
    public void testNullGetInventory() {
    	//clear inventory out 
    	inventoryService.deleteAll();
    	Inventory ivt = inventoryService.getInventory();
    	
    	
    	
    	//getInventory should return an Inventory object with all 0 values
    	Assertions.assertEquals(0, ivt.getIngredients().size());
    }
}