package pages;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import baseClasses.Page;
import baseClasses.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class Basket extends Page implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static public List<Product> products = new ArrayList<Product>();

    /**
     *
     * @return List<Products>
     */
    public List<Product> getBasket(){
        return products;
    }
    
    /**
     * Remove an Item and get the Item ID from an f:param by clicking the commandButton
     */
    public void remove(){
    	FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
    	String productID = params.get("pID");
    	for (int i=0;i<products.size();i++) if (products.get(i).getId() == Integer.parseInt(productID)) products.remove(i);
    }
    
    public void remove(String id){
    	for (int i=0;i<products.size();i++) if (products.get(i).getId() == Integer.parseInt(id)) products.remove(i);
    }
    
    /** Add an item to the basket with the given quantity
     * 
     * @param p
     */
    public void add(Product pNew){
    	Product p = pNew.copy();
    	if (p.getQuantity() > getQuantity(Integer.toString(p.getId()))) {
    		super.notify("Oups!", "Sorry but this amount is out of stock.");
    		p.setQuantity(getQuantity(Integer.toString(p.getId())));
    		products.add(p);
    	}
    	else products.add(p);
    }
    
    public int getTotalPrice(){
    	int result = 0;
    	for (int i=0;i<products.size();i++) result+=products.get(i).getPrice() * products.get(i).getQuantity();
    	return result;
    }
    
    public int size(){
    	return products.size();
    }
    
    public  static boolean productInBasket(String id){
    	for (int i=0;i<products.size();i++) if (products.get(i).getId() == Integer.parseInt(id)) return true;
    	return false;
    }
    
    public static Product productFromBasket(String id){
    	Product p = new Product();
    	for (int i=0;i<products.size();i++) if (products.get(i).getId() == Integer.parseInt(id)) {
    		System.out.println(products.get(i).getQuantity());
    		p = products.get(i);
    	}
    	// return Dummy productList otherwise
    	return p;
   
    }
    
    public void notifyQuantity(String id){
    	Product p = new Product();
    	for (int i=0;i<products.size();i++) if (products.get(i).getId() == Integer.parseInt(id)) p = products.get(i);
    	if (p.getQuantity() > getQuantity(Integer.toString(p.getId()))) {
    		p.setQuantity(getQuantity(Integer.toString(p.getId())));
    		super.notify("Error","Sorry, we do not have this amount on stock. The quantity got reset to the maximum.");
    	}
	}
    
    
}
