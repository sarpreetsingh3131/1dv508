package baseClasses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface PageInterface {
	
	/* ******************************* Change or get DB directly by SQL ********************************/
	
	/** Sets the Content(ResultSet) of a page for every SQL.
	 * @param sql query to the database e.g. "SELECT * FROM product;" 
	 * @return the ResultSet of the requested rows, sets the content of the page
	 */
	void setContent(String sql);
	/**
	 * 
	 * @param column , Name of the column in the ResultSet
	 * @return the content of the item at the index position. Returns an empty String if the column of item dies not exist
	 */
	String getContent(int index, String column);

	
	/** Updates the DB with the given SQL command (INSERT, UPDATE, DELETE)
	 */
	void updateDB(String s);
	
	/* ******************************* Convert DB ResultSets into Objects ********************************/
	
	/**	Converts a ResultSet into a List of products.
	 * 
	 * @param products give a certain content of products
	 * @return gives back an ArrayList<Product> from the given ResultSet.
	 * @throws SQLException when it is now a ResultSet from ordinary products
	 */
	ArrayList<Product> toProducts(ResultSet products) throws SQLException;
	
	/** Converts a ResultSet into a List of orders.
	 * 
	 * @param orders 
	 * @return an ArrayList<Order> of Order
	 * @throws SQLException when it is not a ResultSet of the order table
	 */
	ArrayList<Order> toOrders(ResultSet orders) throws SQLException;
	
	/** Converts a ResultSet into a List of users
	 * 
	 * @param users
	 * @return ArrayList of user
	 * @throws SQLException when it is not a ResultSet of the user table
	 */
	ArrayList<User> toUsers(ResultSet users) throws SQLException;
	
	
	/* ******************************* UPDATE,INSERT and DELETE Object in the DB ********************************/
	
	/** Updates the given Object in the DB, if it doesn't exist nothing happens
	 * @param o an object from type User, Product or Order which should be updated in the DB
	 */
	void updateDB(Object o);
	
	/** Deletes the Object from the Database
	 * @param o Product, Order or User
	 */
	void deleteDB(Object o);
	
	/** Inserts the given Object into the Database (does not check if already exist!) , generates a new ID (auto-increment)
	 * @param o Product, Order or User
	 */
	void insertDB(Object o);
	
	/* ******************************* Useful functions ********************************/
	/** Gives out an notification on a page with the given Strings. (Needs p:growl on the xhtml page)
	 * @param s Headline
	 * @param s1 Text
	 */
	void notify(String s, String s1);
	
	/** Method to get a List of all the category names
	 * 
	 * @return all categories from the database
	 * @throws SQLException if the ResultSet is null
	 */
	List<String> getCategories() throws SQLException;
	
	/** Method for a quick quantity check on the given Product ID
	 * 
	 * @param id from the product you want to check the quantity
	 * @return quantity as integer
	 */
	int getQuantity(String id);

}
