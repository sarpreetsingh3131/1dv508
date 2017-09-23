/**
 * 
 */
package pages;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import baseClasses.Page;
import baseClasses.Product;
import baseClasses.Rating;
import baseClasses.User;

/**
 * @author kaikun
 *
 */

@Named
@SessionScoped
public class Mainpage extends Page implements Serializable {
	
	private List<Product> products = new ArrayList<Product>();
	private String category = "";
	private String search = "";
	
	/**
	 * Default serialVersionID generated from eclipse
	 */
	private static final long serialVersionUID = 1L;
	
	
	/** b is true if there is no search or category selected -> show all items
	 * sets the content to all available products or the searched/category ones from the web-shop
	 * creates objects for all/selected products in content and puts them into the <products> list.
	 */
	public void setProducts(Boolean b) {
		try {
			// default : display all products
			if (b) setContent("select * from product");
			// category : display products from selected category
			else if (!category.isEmpty()) setContent("select * from product, category WHERE product.category=category.id AND category.name=\""+category+"\";");
			// search : display searched products - not case-sensitive and searches substrings
			else setContent("SELECT * FROM product WHERE UPPER(name) LIKE '%"+search.toUpperCase()+"%' OR UPPER(description) LIKE '%"+search.toUpperCase()+"%';");
			products = toProducts(content);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> instantSearch(String query){
		// <!--<p:autoComplete id="acSimple" completeMethod="#{mainpage.instantSearch}" a:placeholder="Search By Product"></p:autoComplete> -->
		System.out.println("Instant search invoked with query: " + query);
		setContent("SELECT * FROM product WHERE UPPER(name) LIKE '%"+query.toUpperCase()+"%';");
		ArrayList<String> searchResults = new ArrayList<String>();
		try {
			ArrayList<Product> searchedProducts = toProducts(content);
			System.out.println("Size ProductList:" + searchedProducts.size());

			for (int i=0;i<searchedProducts.size();i++) searchResults.add(searchedProducts.get(i).getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Size:" + searchResults.size());
		return searchResults;
	}
	
	/**
	 * Sets the content and the product list.
	 * @return the list of products in the database. 
	 */
	public List<Product> getProducts(){
		// default : display all products
		if (category.isEmpty() && search.isEmpty()) setProducts(true);
		// search or category : display specific
		else setProducts(false);
		// Reset category to show all products by default again!
		category = "";
		search = "";
		return products;
	}	

	/**
	 * returns a List with all the category names
	 */
	@Override
	public List<String> getCategories(){
		List<String> categories = new ArrayList<String>();
		try {
			categories = super.getCategories();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categories;
	}
	
	
	public void setCategory(String s){
		category = s;
	}
	
	public String getCategory(){
		return category;
	}
	
	public void setSearch(String s){
		search = s;
	}
	
	public String getSearch(){
		return search;
	}
	
	public boolean isNotFound(){
		return products.isEmpty();
	}
	
	public void toMainpage(){
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(ec.getApplicationContextPath() + "/mainpage.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void toSearchOrderPage(){
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(ec.getApplicationContextPath() + "/searchOrder.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Product> getMostWanted(){
		setContent("select * from product");
		List<Product> allProducts = new ArrayList<Product>();
		try {
			allProducts = toProducts(content);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<Product> wanted = allProducts;
		Collections.sort(wanted);
		// Quickfix mainpage best products
		while (wanted.size() < 3) {
			Product logo = new Product();
			logo.setImage("http://i.imgur.com/dXnRDSg.jpg");
			logo.setName("Our Logo");
			logo.getRatings().add(new Rating(-99,"Logo", 5, new User()));
			wanted.add(logo);
		}
		return wanted;
	}

	
	
}
