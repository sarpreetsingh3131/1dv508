/**
 * 
 */
package pages;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import baseClasses.Order.OrderStatus;
import baseClasses.Order;
import baseClasses.Page;
import baseClasses.Product;
import baseClasses.ProductList;

/**
 * @author kaikun
 *
 */

@Named
@SessionScoped
public class AdminPages extends Page implements Serializable {
	
	/**
	 * Default serialVersionID generated from eclipse
	 */
	// ******************************** admin Order
	private List<Order> orders = new ArrayList<Order>();
	private List<OrderStatus> status = new ArrayList<OrderStatus>();
	private Order nOrder =  new Order();
	private String searchOrder = "";
	// for change many states at once (checkboxes)
	private Map<String,Boolean> checked = new HashMap<String,Boolean>();
	private OrderStatus state = OrderStatus.IN_PROCESS;
	
	// ******************************** admin Product
	// all products
	private List<Product> products = new ArrayList<Product>();
	// List of all products to set all products from DB
    @ManagedProperty("#{productList}")
	private ProductList productService = new ProductList();
    // String to search for a product
    String searchProduct = "";
	
	// all categories to have a list to choose from
	private List<String> category;
	private String selectedCat;
	// the product to edit, add or delete
	private Product prod = new Product();
	private Map<Integer,Boolean> checkedProducts = new HashMap<Integer,Boolean>();
	
	// for quantities in adminOrderPage
	private Map<Integer,Integer> quantities = new HashMap<Integer,Integer>();

	
	private static final long serialVersionUID = 1L;
	
	public void test(Integer id){
		System.out.println("ID: " + id);
		System.out.println("Invoked Map Quantity:" + quantities.get(id));
	}
	
	public void init() {
		setOrders();
		for (Order o : orders) checked.put(o.getOrderId(), false);
		for (Product p : products) quantities.put(p.getId(), 1);
		setStatus();
		products = productService.getProducts();
		prod = new Product();
		searchProduct = "";
		for (Product p : products) checkedProducts.put(p.getId(), false);
	}
	
	/* ******************************* admin Products **************************************** */
	
	public void adminAddProduct(){
		System.out.println("image:" + prod.getImage());
		// insert given product into DB
		insertDB(prod);
		// clear all fields
		adminClearInputs();
		// reload list for products so that new item is in list
		products = productService.getProducts();
	}
	public void adminSaveProduct(){
		updateDB(prod);
		products = productService.getProducts();
		prod = new Product();
	}
	
	public void searchProduct(){
		if (searchProduct.isEmpty()) super.notify("Error", "Searching for an empty name sure?");
		else {
			setContent("SELECT * FROM product WHERE UPPER(name) LIKE '%"+searchProduct.toUpperCase()+"%';");
			try {
				products = toProducts(content);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void adminClearInputs(){
		prod = new Product();
	}
	
	public void adminDeleteProduct(){
		for (Order o : orders) {
			for (Product p : o.getOrderList()) {
				if (p.getId() == prod.getId()) {
					super.notify("Error", "You can not delete a product which is currently ordered. This is included in the order: " + o.getOrderId());
					return;
				}
			}
		}
		deleteDB(prod);
		products = productService.getProducts();
		adminClearInputs();
	}
	
	public void adminDeleteProduct(String id){
		for (Order o : orders) {
			for (Product p : o.getOrderList()) {
				if (p.getId() == Integer.parseInt(id)) {
					super.notify("Error", "You can not delete a product which is currently ordered. This is included in the order: " + o.getOrderId());
					return;
				}
			}
		}		
		conn.fetch("DELETE FROM product WHERE id="+id);
		products = productService.getProducts();
	}
	
	public void adminDeleteProducts(){
		for (Product p : products) {
	    	if (checkedProducts.containsKey(p.getId())) {
	    		System.out.println("Checks orderboxes selected ...");
	        if (checkedProducts.get(p.getId())) {
	        	// try to delete product
	        	for (Order o : orders) {
	    			for (Product p1 : o.getOrderList()) {
	    				if (p1.getId() == prod.getId()) {
	    					super.notify("Error", "You can not delete a product which is currently ordered. This is included in the order: " + o.getOrderId());
	    					return;
	    				}
	    			}
	    		}
	    		deleteDB(prod);
	    		products = productService.getProducts();
	        }
	    	}
	    }
	    checked.clear();
	}
	
	
	public Product getProd() {
		return prod;
	}

	public void setProd(Product prod) {
		this.prod = prod;
	}	
	
	public void onRowSelect(SelectEvent event) {
	        FacesMessage msg = new FacesMessage("Product Selected", Integer.toString(((Product) event.getObject()).getId()));
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	 
	public void onRowUnselect(UnselectEvent event) {
	        FacesMessage msg = new FacesMessage("Product Unselected", Integer.toString(((Product) event.getObject()).getId()));
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	
	
	/* *************************************** Admin Order Methods ******************************* */
	
	public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Order Edited", "Selected Order: " + ((Order) event.getObject()).getOrderId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        super.updateDB(((Order) event.getObject()));
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", "Selected Order: " +((Order) event.getObject()).getOrderId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
    public void addNewOrder(){
    	for (int i=0;i<nOrder.getOrderList().size();i++) {
    		if (quantities.containsKey(nOrder.getOrderList().get(i).getId())) {
    			nOrder.getOrderList().get(i).setQuantity(quantities.get(nOrder.getOrderList().get(i).getId()));
    			System.out.println("ID: " + nOrder.getOrderList().get(i).getId());
    			System.out.println("Quantities map: " + quantities.get(nOrder.getOrderList().get(i).getId()));
            	System.out.println("Quantities produt: " + nOrder.getOrderList().get(i).getQuantity());
    		}
    	}
    	super.insertDB(nOrder);
    	init();
    	super.notify("Successful added.", "Order Number: " + orders.get(orders.size()-1).getOrderId());
    }
    
    public void searchOrder(){
    	if (ContactPage.isInteger(searchOrder, 10)) {
    		setContent("SELECT * FROM orders WHERE id="+ searchOrder+";");
    		try {
    			orders = toOrders(content);
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    	else super.notify("Error", "The Order ID needs to be an Integer");
    }
    
    public void deleteOrder(Order o){
    	super.deleteDB(o);
    	init();
    }
    
    
    /* ************************************** Methods for SelectProductsMenu *************************************************** */
    public void setProductList(ProductList service) {
        this.productService = service;
    }
    
    public List<Product> getProducts(){
    	return products;
    }
    
 // Set & Get Methods
	
 	public List<Order> getOrders(){
 		return orders;
 	}
 	
 	public void setOrders(){
 		setContent("SELECT * FROM orders;");
 		try {
 			orders = toOrders(content);
 		} catch (SQLException e) {
 			e.printStackTrace();
 		}
 	}
 	
 	public Order getNewOrder(){
 		return nOrder;
 	}
 	
 	public void setNewOrder(Order o){
 		nOrder = o;
 	}
 	
 	public void setSearchProduct(String s){
 		searchProduct = s;
 	}
 	
 	public String getSearchProduct(){
 		return searchProduct;
 	}

 	public List<String> getCategory() {
 		return category;
 	}

 	public void setCategory(List<String> category) {
 		this.category = category;
 	}

 	public String getSelectedCat() {
 		return selectedCat;
 	}

 	public void setSelectedCat(String selectedCat) {
 		this.selectedCat = selectedCat;
 	}
 	
 	public List<OrderStatus> getStatus(){
 		setStatus();
 		return status;
 	}
 	
 	public void setStatus(){
 		if (status.size() > 0) return;
 		status.add(OrderStatus.IN_PROCESS);
 		status.add(OrderStatus.DELAYED);
 		status.add(OrderStatus.SHIPPED);
 	}
 	
 	public String getSearchOrder(){
 		return searchOrder;
 	}
 	
 	public void setSearchOrder(String s){
 		searchOrder = s;
 	}

	public Map<String,Boolean> getChecked() {
		return checked;
	}
	
	public void setChecked(Map<String,Boolean> checked){
		this.checked = checked;
	}

	public OrderStatus getState() {
		return state;
	}

	public void setState(OrderStatus state) {
		this.state = state;
	}
	
	public void changeCheckedStates(){
	    for (Order o : orders) {
	    	if (checked.containsKey(o.getOrderId())) {
	        if (checked.get(o.getOrderId())) {
	    		System.out.println("change state of order: " + o.getOrderId());
	            o.setOrderStatus(state);
	            updateDB(o);
	        }
	    	}
	    }
	    init();
	}
	
	public void deleteCheckedOrders(){
		for (Order o : orders) {
	    	if (checked.containsKey(o.getOrderId())) {
	        if (checked.get(o.getOrderId())) {
	    		System.out.println("delete order: " + o.getOrderId());
	            deleteDB(o);
	        }
	    	}
	    }
		init();
	    
	}

	public Map<Integer,Boolean> getCheckedProducts() {
		return checkedProducts;
	}

	public void setCheckedProducts(Map<Integer,Boolean> checkedProducts) {
		this.checkedProducts = checkedProducts;
	}
	
	public Map<Integer,Integer> getQuantities(){
		return quantities;
	}
	
	/* ********************************************* admin Categories ********************************************************************* */
	
	public void initCategory(){
		selectedCat = "";
	}
	
	public void deleteCategory(String s){
		setContent("SELECT id FROM category WHERE name='" + s + "';");
		String categoryID = getContent(0, "id");
		System.out.println("The category ID:" + categoryID);
		if (exist("SELECT * FROM product WHERE category=" + categoryID + ";")) super.notify("ERROR", "Can not remove category when product is assigned to.");
		else conn.fetch("DELETE FROM category WHERE name=\"" + s + "\";");
	}
	
	public void addCategory(String s){
		if (selectedCat.isEmpty()) return;
		conn.fetch("INSERT INTO category (name) VALUES (\""+s+"\");");
		selectedCat = "";
	}
	
	public void editCategory(RowEditEvent event){
		if (selectedCat.isEmpty()) return;
		conn.fetch("UPDATE category SET name='" + selectedCat + "' WHERE name='" + event.getObject() + "';");
	}
	
	
}
