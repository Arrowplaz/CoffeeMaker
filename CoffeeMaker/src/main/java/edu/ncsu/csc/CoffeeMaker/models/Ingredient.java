package edu.ncsu.csc.CoffeeMaker.models;




import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;


/**
 * A class describing ingredients used by the coffee shop 
 * 
 * @author anagireddygari
 *
 */
@Entity
public class Ingredient extends DomainObject {
	
	
	/**
	 * The id of the ingredient 
	 */
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * The name of the ingredient
	 */
	private String name;
	
	
	/**
	 * The amount of the ingredient; either in a recipe or in the inventory
	 */
	@Min(0)
	private Integer amount;
	
	/**
	 * An Empty constructor for hibernate
	 */
	public Ingredient() {
		
	}
	
	/**
	 * An Ingredient constructor
	 * @param inName The name of the ingredient
	 * @param inAmount the amount of the ingredient
	 */
	public Ingredient(String inName, Integer inAmount) {
		this.setName(inName);
		this.setAmount(inAmount);
	}
	
	/**
	 * A getter for the ID
	 */
	@Override
	public Long getId() {
		return this.id;
	}


	/**
	 * A setter for the ingredient name
	 * @param inName the name being set
	 */
	public void setName(String inName) {
		if(inName == null || "".equals(inName)) throw new IllegalArgumentException();
		this.name = inName;
	}

	/**
	 * A getter for the name
	 * @return the name of the ingredient
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * A getter for the amount of the ingredient
	 * @return the amount of the ingredient
	 */
	public Integer getAmount() {
		return this.amount;
	}

	/**
	 * A setter for the amount
	 * @param inAmount the amount being set
	 */
	public void setAmount(Integer inAmount) {
		if(inAmount < 0 || inAmount == null) throw new IllegalArgumentException();
		this.amount = inAmount;
	}
	
	/**
	 * A toString method()
	 * @return the string version of the inventory
	 */
	public String toString() {
		return this.name + ": " + this.amount;
	}
}
