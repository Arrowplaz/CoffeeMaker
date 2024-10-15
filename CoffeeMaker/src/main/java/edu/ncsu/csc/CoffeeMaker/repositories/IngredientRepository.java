package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;


import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

/**
 * CRUD operations for ingredient
 * @author anagireddygari
 *
 */
public interface IngredientRepository extends JpaRepository <Ingredient, Long> {
	
	/**
	 * A find my name method for the Ingreident
	 * @param name the name of the ingredient
	 * @return The ingredient with the given name
	 */
	Ingredient findByName(String name);
}
