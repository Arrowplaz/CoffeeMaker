
package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;


/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long    id;
    
    /** List of ingredients present in the recipe*/
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ingredient> ingredients = new ArrayList<Ingredient>();
    
    
    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }

    /**
     * A constructor taking in a list of ingredients to populate the inventory
     * @param inIngredients the ingredients being added
     */
    public Inventory ( List<Ingredient>inIngredients ) {
    	this.addIngredients(inIngredients);
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Adds a new ingredient to the inventory
     * @param newIngredient the ingredient being added
     */
    public void addIngredient(Ingredient newIngredient) {
    	if(newIngredient.getName() == null || "".equals(newIngredient.getName())) throw new IllegalArgumentException("Inavlid name");
    	if(newIngredient.getAmount() < 0 || newIngredient.getAmount() == null) throw new IllegalArgumentException("Invalid amount");
    	for(int i = 0; i < this.ingredients.size(); i++) {
    		if(newIngredient.getName().toLowerCase().equals(this.ingredients.get(i).getName().toLowerCase())) {
    			this.updateIngredient(newIngredient.getName(), newIngredient.getAmount());
    			return;
    		}
    	}
    	this.ingredients.add(newIngredient);
    	
    }
    
    /**
     * Adds multiple ingredients to the inventory
     * @param newIngredients the new ingredients being added
     */
    public void addIngredients(List<Ingredient> newIngredients) {
    		for(int i = 0; i < newIngredients.size(); i++) {
    			Ingredient newIngredient = newIngredients.get(i);
    			if(newIngredient.getName() == null || "".equals(newIngredient.getName())) throw new IllegalArgumentException("Inavlid name");
    	    	if(newIngredient.getAmount() < 0 || newIngredient.getAmount() == null) throw new IllegalArgumentException("Invalid amount");
    		}
    		this.ingredients = newIngredients;
    	}
   
    
    /**
     * Deletes an ingredient given a name
     * @param name the name of the ingredient being deleted
     */
    public void deleteIngredient(String name) {
    	for(int i = 0; i < this.ingredients.size(); i++) {
    		if(this.ingredients.get(i).getName().equals(name)) {
    			this.ingredients.remove(i);
    			return;
    		}
    	}
    	throw new IllegalArgumentException("Ingredient with name was not found");
    	
    }
    
    /**
     * Takes an amount and adds that to the inventory of an ingredient
     * @param name the ingredient 
     * @param amount the amount being added
     */
    public void updateIngredient(String name, int amount){
    	for(int i = 0; i < this.ingredients.size(); i++) {
    		if(this.ingredients.get(i).getName().equals(name)) {
    			this.ingredients.get(i).setAmount(this.ingredients.get(i).getAmount() + amount);
    			return;
    		}
    	}
    	throw new IllegalArgumentException("Couldnt find Ingredient with name");
    }
    
    /**
     * A getter for a single Ingredeint object
     * @param name the name of the ingredient
     * @return the ingredient object
     */
    public Ingredient getIngredient(String name){
    	for(int i = 0; i < this.ingredients.size(); i++) {
    		if(this.ingredients.get(i).getName() == name) {
    			return this.ingredients.get(i);
    		}
    	}
		return null;
    }
    
    /**
     * A getter for the total ingredients list
     * @return all the ingredients in the inventory
     */
    public List<Ingredient> getIngredients(){
    	return this.ingredients;
    }
    
    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        List<Ingredient> recipe = r.getIngredients();
        for(int i = 0; i < recipe.size(); i++) {
        	String currentIngredient = recipe.get(i).getName();
        	Ingredient invetoryIngredient = this.getIngredient(currentIngredient);
        	if(invetoryIngredient.getAmount() < recipe.get(i).getAmount()) return false;
        }
        return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
        	List<Ingredient> recipe = r.getIngredients();
        	for(int i = 0; i < recipe.size(); i++) {
        		Ingredient currentIngredient = recipe.get(i);
        		this.updateIngredient(currentIngredient.getName(), 0 - currentIngredient.getAmount());
        		
        	}
        	return true;
        }
        else {
            return false;
        }
    }

    

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        String ivt = "[";
        for(int i = 0; i < this.ingredients.size() - 1; i++) {
        	ivt += this.ingredients.get(i).getName() + ": " + this.ingredients.get(i).getAmount() + ", ";
        }
        ivt += this.ingredients.get(this.ingredients.size() - 1).getName() + ": " + this.ingredients.get(this.ingredients.size() - 1).getAmount();
        ivt += "]";
        return ivt;
    }

}