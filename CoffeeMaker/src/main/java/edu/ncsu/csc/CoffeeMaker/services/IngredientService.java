package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;

/**
 * Class for spring
 * @author anagireddygari
 *
 */
@Component
@Transactional
public class IngredientService extends Service<Ingredient, Long> {

	/**
     * IngredientRepository, to be autowired in by Spring and provide CRUD
     * operations on Ingredient model.
     */
    @Autowired
    private IngredientRepository ingredientRepository;
	
	@Override
	protected JpaRepository<Ingredient, Long> getRepository() {
		return ingredientRepository;
	}

	/**
     * Find an Ingredient with the provided name
     * 
     * @param name
     *            Name of the ingredient to find
     * @return found recipe, null if none
     */
    public Ingredient findByName ( final String name ) {
        return ingredientRepository.findByName( name );
    }
}
