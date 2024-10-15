package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
public class IngredientTest {
	
	Ingredient testIngredient;
	
	@BeforeEach
	public void setup() {
		testIngredient = new Ingredient("test ingredient", 10);
	}
	
	@Test
	public void setNameTestInvalid() {
		try {
			testIngredient.setName(null);
			Assertions.fail("Setting null should throw an IAE");
		}catch(Exception e) {
			//Expected
		}
		
		try {
			testIngredient.setName("");
			Assertions.fail("Setting an empty string should throw an IAE");
		}catch(Exception e) {
			//Expected
		}
	}
	
	@Test 
	public void setNameTestValid() {
		testIngredient.setName("Recipe 2");
		Assertions.assertEquals("Recipe 2", testIngredient.getName(), "Name should be 'Recipe 2'");
	}
	
	@Test 
	public void setQuantityTestInvalid() {
		try {
			testIngredient.setAmount(-1000);
			Assertions.fail("Setting a negative quanitty should throw an error");
		}catch(Exception e){
			//Expected
		}
		
		try {
			testIngredient.setAmount(-1);
			Assertions.fail("Setting a negative quanitty should throw an error");
		}catch(Exception e){
			//Expected
		}
	}
	
	@Test
	public void setQuantityTestValid() {
		testIngredient.setAmount(0);
		Assertions.assertEquals(0, testIngredient.getAmount(), "Amount should be 0");
		
		testIngredient.setAmount(10);
		Assertions.assertEquals(10, testIngredient.getAmount(), "Amount should be 10");
	}
	
	@Test
	public void testToString() {
		Assertions.assertEquals("test ingredient: 10", testIngredient.toString());
	}
}
