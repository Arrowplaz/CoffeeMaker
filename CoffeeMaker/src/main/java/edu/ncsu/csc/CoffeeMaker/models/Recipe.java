package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long    id;

    /** Recipe name */
    private String  name;

    /** List of ingredients present in the recipe*/
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ingredient> ingredients;
    
    /** Recipe price */
    @Min ( 0 )
    private Integer price;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {
       if (ingredients == null)
    	   return true;
       for (Ingredient i : ingredients) {
    	   if (i.getAmount() != 0)
    		   return false;
       }
       return true;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Takes an ingredient name and updates its amount to a provided amount
     * @param name the name of the ingredient
     * @param amount the amount being set
     */
    public void setIngredient(String name, int amount) {
    	if (getIngredient(name).equals(null) || amount < 0)
    		throw new IllegalArgumentException("No ingredient to edit or negative amount");
    	else {
    		getIngredient(name).setAmount(amount);
    	}
    }
    
    /**
     * Sets a new list as the recipe
     * @param newIngredients the new recipe
     */
    public void setIngredients(List<Ingredient> newIngredients) {
    	for(int i = 0; i < newIngredients.size(); i++) {
    		if(newIngredients.get(i).getAmount() < 1) throw new IllegalArgumentException("Ingredient amount cannot be less than 1");
    	}
    	this.ingredients = newIngredients;
    }
    /**
     * Deletes an ingredient from the recipe.
     * 
     * @param name the ingredient name
     * @return boolean true if the ingredient was deleted, false if the ingredient was not found in the array list
     */
    public boolean deleteIngredient(String name) {
    	for (Ingredient i : ingredients) {
    		if (i.getName().equals(name)) {
    			ingredients.remove(i);
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Adds a new ingredient to the recipe
     * @param inName the name 
     * @param inAmount the amount
     */
    public void addIngredient(String inName, int inAmount) {
    	Ingredient toAdd = new Ingredient(inName, inAmount);
    	if (inAmount <= 0) {
    		throw new IllegalArgumentException();
    	}
    	ingredients.add(toAdd);
    	
    }
    
    /**
     * Returns the ingredients of a recipe
     * @return the ingredients in the recipe
     */
    public List<Ingredient> getIngredients() {
    	return ingredients;
    	
    }
    
    /**
     * Gets an ingredient that corresponds to the name provided
     * @param name the name of the ingredient object you want
     * @return The ingredient with the name
     */
    public Ingredient getIngredient(String name) {
    	for (Ingredient i : ingredients) {
    		if (i.getName().equals(name))
    			return i;
    	}
    	return null; //the ingredient was not found
    }
    
    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
    	if (name == null)
    		throw new IllegalArgumentException("Name cannot be null");
    	else {
    		this.name = name;
    	} 
    	this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }
    
    /**
     * Updates the recipe to the name, price, and ingredients of a given recipe
     * @param r the recipe object to update to
     */
    public void updateRecipe(Recipe r) {
    	this.setPrice(r.getPrice());
    	List<Ingredient> newIngredients = r.getIngredients();
    	this.setIngredients(newIngredients);
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        return name;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

}