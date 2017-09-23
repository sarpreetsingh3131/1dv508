package baseClasses;

import java.util.ArrayList;

public class User implements Comparable {

	private int id;	// generated
	private String name;	// required
	private ArrayList<Order> orders = new ArrayList<Order>(); // default
	private String email; // required
	private String password; // required
	private boolean admin = false; // default
	private String image = ""; // default
	private String address; // required
	private int postcode; // required
	private String city; // required
	private String phone; // NOT required
	
	public User(){
		orders = new ArrayList<Order>();
	};

	public User(int id, String userName, ArrayList<Order> orders, String email, String password, boolean b, String image, String address, String city, int postcode, String phone) {
		this.id = id;
		this.name = userName;
		this.orders = orders;
		this.email = email;
		this.password = password;
		this.admin = b;
		this.image = image;
		this.address = address;
		this.city = city;
		this.postcode = postcode;
		this.phone = phone;
	}
	
	public User(String userName, ArrayList<Order> orders, String email, String password, boolean b, String image,String address, String city, int postcode) {
		this.name = userName;
		this.orders = orders;
		this.email = email;
		this.password = password;
		this.admin = b;
		this.image = image;
		this.address = address;
		this.city = city;
		this.postcode = postcode;
	}
	
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public boolean getAdmin() {
		return admin;
	}
	
	public void setAdmin(boolean x){
		admin = x;
	}

	public String getName() {
		return name;
	}

	public void setName(String Name) {
		this.name = Name;
	}

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserName: " + name + " Order: " + orders.toString() + "Email: " + email + "Password: " + password;
	}
	
	/**
	 * This method returns admin password
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the admin password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/** Checks if the User is complete and valid for the DB
	 * 
	 * @return
	 */
	public boolean isComplete(){
		if (name == null || password == null || email == null || image == null || address == null || city == null) return false;
		return ((!name.isEmpty()) && (!password.isEmpty()) && (!email.isEmpty() && !address.isEmpty() && !city.isEmpty() ));
	}

	public User copy(){
		return new User(this.id,this.name,this.getOrders(),this.email,this.password,this.admin, this.image, this.address, this.city, this.postcode, this.phone);
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPostcode() {
		return postcode;
	}

	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public int compareTo(Object o) {
		if (!(o instanceof User)) return -99;
		return ((User)o).getName().compareTo(this.name);
	}
	
	
}
