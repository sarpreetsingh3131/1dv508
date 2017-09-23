package baseClasses;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents an order.
 *
 * @author Paulius
 *
 */
public class Order {

	// Fields
	public enum OrderStatus {
		IN_PROCESS, SHIPPED, DELAYED
	}

	protected OrderStatus orderStatus;
	private String orderId;
	private List<Product> orderList = new ArrayList<Product>();
	private Date orderDate;

	/**
	 * Empty constructor.
	 */
	public Order() {
	}

	/**
	 * Order constructor.
	 *
	 * @param orderId
	 * @param orderList
	 * @param orderDate
	 * @param orderStatus
	 */
	public Order(String orderId, List<Product> orderList, Date orderDate, OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
		this.orderId = orderId;
		this.orderList = orderList;
		this.orderDate = orderDate;
	}
	
	/**
	 * Order constructor.
	 *
	 * @param orderList
	 * @param orderDate
	 * @param orderStatus
	 */
	public Order(List<Product> orderList, Date orderDate, OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
		this.orderList = orderList;
		this.orderDate = orderDate;
	}

	// Getters and setters for fields below.

	/**
	 * Method returning order status.
	 * 
	 * @return OrderStatus
	 */
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	/**
	 * Method to set the order status.
	 * 
	 * @param orderStatus
	 */
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * Method returning order id.
	 * 
	 * @return long
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * Method setting order id.
	 * 
	 * @param orderId
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * Method returning list of products.
	 * 
	 * @return List<Product>
	 */
	public List<Product> getOrderList() {
		return orderList;
	}

	/**
	 * Method to set the list of products.
	 * 
	 * @param orderList
	 */
	public void setOrderList(List<Product> orderList) {
		this.orderList = orderList;
	}

	/**
	 * Method returning the order date.
	 * 
	 * @return Date
	 */
	public Date getOrderDate() {
		return orderDate;
	}
	

	/**
	 * Method to set the date of the order.
	 * 
	 * @param orderDate
	 */
	public void setOrderDate(Date orderDate){
		this.orderDate = orderDate;
	}
	

	/**
	 * Method to get the product by its position in the list.
	 * 
	 * @param n
	 * @return Product
	 */
	public Product getProduct(int n) {
		return orderList.get(n);
	}

	/**
	 * Method returning the string output of the order.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return orderId + " " + " " + orderDate + " " + "\n" + orderList;
	}

	public Iterator<Product> getIterator() {
		return orderList.iterator();
	}
	
	public int getTotalPrice(){
		int total = 0;
		System.out.println("Invoked size: " + orderList.size());
		for (int i=0;i<orderList.size();i++) total+=orderList.get(i).getQuantity()*orderList.get(i).getPrice();
		return total;
	}
	
	/** Checks if the Order is complete and valid for the DB
	 * 
	 * @return
	 */
	public boolean isComplete(){
		return ((orderList.size()>0) && (orderDate != null) && (orderStatus != null));
	}
	
	public Order copy(){
		return new Order(this.orderId, this.orderList, this.orderDate, this.orderStatus);
	}

}