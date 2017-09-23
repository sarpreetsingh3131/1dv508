package baseClasses;

import java.util.ArrayList;

/**
 * This class represent a product.
 * 
 * @author SarpreetSingh
 *
 */
public class Product implements Comparable {

	// Fields
	private String name;
	private String category;
	private double price;
	private String image;
	private String description;
	private int quantity;
	private int id;
	private ArrayList<Rating> ratings;
	

	public int getId() {
		return id;
	}


	/**
	 * Empty Constructor
	 */
	public Product() {
		name = "";
		category = "";
		price = 0;
		description = "";
		image = null;
		quantity = 0;
		id = -99;
		ratings = new ArrayList<Rating>();
	}
	
	/**
	 * Full Constructor
	 */
	public Product(String pname, String cat, double pric, String descr,String img, int amount, int id, ArrayList<Rating> ratings) {
		if(price < 0 || amount < 0) throw new IllegalArgumentException("Price and quantity must be greater or equal than 0.");

		name = pname;
		category = cat;
		price = pric;
		description = descr;
		image = img;
		quantity = amount;
		this.id = id;
		this.ratings = ratings;
		
	}

	/**
	 * This method returns product name
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set product name
	 * 
	 * @param productName
	 */
	public void setName(String productName) {
		this.name = productName;
	}

	/**
	 * This method returns product category
	 * 
	 * @return String
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Set product category
	 * 
	 * @param category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * This method returns product price
	 * 
	 * @return double
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Set product price, throw error if price is less than or equal to zero
	 * 
	 * @param price
	 */
	public void setPrice(double price) {
		if (price < 0.0)
			throw new IllegalArgumentException(
					"Please check the product price. Product name: " + this.name + "  Price: " + price);
		this.price = price;
	}

	/**
	 * This method returns product description
	 * 
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set product description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * This method returns product image
	 * 
	 * @return Image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Set product image
	 * 
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * This method returns product quantity
	 * 
	 * @return int
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Set product quantity, throw error if quantity is less than zero
	 * 
	 * @param quantity
	 */
	public void setQuantity(int quantity) {
		if (quantity < 0)
			throw new IllegalArgumentException("Please check the product quantity.  Product name: " + this.name
					+ "  Quantity: " + quantity);
		this.quantity = quantity;
	}

	/**
	 * Return true if object is equals to product else false.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Product) {
			Product product = (Product) o;
			return this.name.equals(product.name) && this.price == product.price
					&& this.category.equals(product.category);
		}
		return false;
	}

	/**
	 * Represent a nice print out of product
	 */
	@Override
	public String toString() {
		return name + "  " + category + "  " + price + "\n" + description;
	}
	
	/** Checks if the Product is complete and valid for the DB
	 * 
	 * @return
	 */
	public boolean isComplete(){
		if (name == null || category == null || image == null || description == null) return false;
		return ((!name.isEmpty()) && (!category.isEmpty()) && (!image.isEmpty()) && (!description.isEmpty()) && (quantity >= 0) && (price >= 0));
	}
	
	public Product copy(){
		return new Product(this.name,this.category, this.price, this.description, this.image, this.quantity, this.id, this.ratings);
	}

	public ArrayList<Rating> getRatings() {
		return ratings;
	}


	public void setRatings(ArrayList<Rating> ratings) {
		this.ratings = ratings;
	}
	
	public double getAverageRating(){
		double result = 0;
		for (int i=0;i<ratings.size();i++){
			result+= ratings.get(i).getStars();
		}
		result /= ratings.size();
		return result;
	}
	
	public int getAverageRatingAsInt(){
		return (int) getAverageRating();
	}


	@Override
	public int compareTo(Object o) {
		if (o instanceof Product) return (int) (Double.compare(this.getAverageRating(), ((Product)o).getAverageRating()));
		else {
			System.err.println("This is not a compareable product rating");
			return -99;
		}
	}

}