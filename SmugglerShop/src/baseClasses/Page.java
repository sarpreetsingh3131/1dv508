/**
 * 
 */
package baseClasses;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import baseClasses.Order;
import baseClasses.Order.OrderStatus;



/**
 * @author kaikun
 * Abstract page class 
 */

public abstract class Page implements PageInterface{
	
	/**
	 * Establishes a connection at creation
	 */
	protected ConnectionClass conn = new ConnectionClass();
	protected ResultSet content;
	
	public boolean exist(String sql){
		ResultSet rs = conn.fetch(sql);
		try {
			if (rs == null) return false;
			if (!rs.isBeforeFirst()) return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void setContent(String sql) {
		content = conn.fetch(sql);
	}
	
	public String getContent(int index, String column){
		if (content == null) {
			System.err.println("Error in getting Content from databasem Content is null");
			return "";
		}
		int i = 0;
		try {
			content.beforeFirst();
			while (content.next()) {
				System.out.println("Round: " + i + "Index: " + index);
				if (i++ == index) 	return content.getString(column);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public ArrayList<Rating> toRatings(ResultSet ratings) throws SQLException{
		if (ratings == null) return new ArrayList<Rating>();
		ratings.beforeFirst();
		if (!ratings.getMetaData().getTableName(1).equals("rating")) throw new SQLException("This is not a rating list");
		ArrayList<Rating> arr = new ArrayList<Rating>();
		
		while (ratings.next()){
			ResultSet rs = conn.fetch("Select * FROM user WHERE id=" + ratings.getString("user"));
			User author = new User();//toUsers(rs).get(0);
			rs.first();
			author.setName(rs.getString("name"));
			author.setImage(rs.getString("image"));
			int id = ratings.getInt("id");
			System.out.println("toRatings ID of Ratings: " + id);
			arr.add(new Rating(id, ratings.getString("comment"), ratings.getInt("rating"), author));
		}
		
		return arr;
	}

	public ArrayList<Product> toProducts(ResultSet products) throws SQLException{
		if (products == null) return new ArrayList<Product>(); 
		products.beforeFirst();
		//ResultSet from wrong type
		if (!products.getMetaData().getTableName(1).equals("product")) throw new SQLException("This is not a product list");
		ArrayList<Product> arr = new ArrayList<Product>();
		
		while (products.next()) {
			ResultSet rs = conn.fetch("SELECT name FROM category WHERE id=" + products.getString("category"));
			rs.first();
			String category = rs.getString("name");
			
			StringBuilder sb = new StringBuilder(products.getString("ratings"));
			ArrayList<Rating> ratings = new ArrayList<Rating>();
			// just if there are ratings
			if (!sb.toString().isEmpty()) {
				StringBuilder sqlRatings = new StringBuilder("SELECT * FROM rating WHERE ");
				int a = 0;
				for (int i=0;i<sb.length();i++) {
					if (Character.compare(sb.charAt(i), ';') == 0) {
						sqlRatings.append("id="+ sb.substring(a, i) +"");
						a=i+1;
						if (i+1<sb.length()) sqlRatings.append(" OR ");
					}
				}
				sqlRatings.append(";");
				System.out.println("toProducts RatingSQL: " + sqlRatings.toString());
				ResultSet ratingsRS = conn.fetch(sqlRatings.toString());
				ratings = toRatings(ratingsRS);
				for (int j=0;j<ratings.size();j++) System.out.println("toProducts Rating ID of Rating Object: " + ratings.get(j).getId());
			}
			
			
			Product p = new Product(products.getString("name"),
					category,
					products.getDouble("price"),
					products.getString("description"),
					products.getString("image"),
					products.getInt("quantity"),
					products.getInt("id"),
					ratings);
			arr.add(p);
		}
		return arr;
	}
	
	public ArrayList<Order> toOrders(ResultSet orders) throws SQLException{
		if (orders == null) return new ArrayList<Order>();
		orders.beforeFirst();
		//ResultSet from wrong type
		if (!orders.getMetaData().getTableName(1).equals("orders")) throw new SQLException("This is not a order list");
		ArrayList<Order> arr = new ArrayList<Order>();
		try {
			while (orders.next()) {
				// get the products from the database by the IDs 
				StringBuilder sb = new StringBuilder(orders.getString("products"));
				StringBuilder sqlProducts = new StringBuilder("SELECT * FROM product WHERE ");
				int a = 0;
				for (int i=0;i<sb.length();i++) {
					if (Character.compare(sb.charAt(i), ':') == 0) sqlProducts.append("id="+ sb.substring(a, i) +"");
					if (Character.compare(sb.charAt(i), ';') == 0) {
						a=i+1;
						if (i+1<sb.length()) sqlProducts.append(" OR ");
					}
				}
				sqlProducts.append(";");
				ResultSet products = conn.fetch(sqlProducts.toString());
				ArrayList<Product> productList = toProducts(products);
				
				// change quantities from ordered productList (because products taken by ID from the database)
				if (productList.size() > 0) { // case there are no products in the order -> shouldn't be valid anyway
					a = 0;
					int nr = 0;
					for (int i=0;i<sb.length();i++) {
						if (Character.compare(sb.charAt(i), ':') == 0) a=i;
						if (Character.compare(sb.charAt(i), ';') == 0) productList.get(nr++).setQuantity(Integer.parseInt(sb.substring(a+1, i)));
					}
				}
				
				OrderStatus status;
				switch (orders.getInt("orderStatus")) {
				case 1 : status = OrderStatus.IN_PROCESS;
						break;
				case 2 : status = OrderStatus.SHIPPED;
						break;
				case 3 : status = OrderStatus.DELAYED;
						break;
				default : status = OrderStatus.IN_PROCESS;
						break;
				}
								
			    Order o = new Order(orders.getString("id"),
			    					productList,
			    					orders.getDate("date"),
			    					status);
			  
			  arr.add(o);    
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}
	
	public ArrayList<User> toUsers(ResultSet users) throws SQLException{
		if (users == null) new ArrayList<User>();
		users.beforeFirst();
		//ResultSet from wrong type
		if (!users.getMetaData().getTableName(1).equals("user")) throw new SQLException("This is not a user list");
		ArrayList<User> arr = new ArrayList<User>();
		try {
			while (users.next()) {
				ArrayList<Order> orders = new ArrayList<Order>();
				
				StringBuilder sqlOrders = new StringBuilder("SELECT * FROM orders WHERE id=\"");
				StringBuilder sb = new StringBuilder(users.getString("orders"));
				int a = 0;
								
				// if the user has orders
				if (!sb.toString().isEmpty() ) {
					for (int i=0;i<sb.length();i++) {
						if (Character.compare(sb.charAt(i), ';') == 0) {
						sqlOrders.append(sb.substring(a, i)+"\"");
						if (i+1<sb.length()) sb.append(" OR id=\"");
						a=i+1;
						}
					}
					sqlOrders.append(";");
					System.out.println(sqlOrders.toString());
					orders.addAll(toOrders(conn.fetch(sqlOrders.toString())));
				}
				
				
			    User u = new User(	users.getInt("id"),
			    					users.getString("name"),
			    					orders,
			    					users.getString("email"),
			    					users.getString("password"),
			    					users.getBoolean("admin"),
			    					users.getString("image"),
			    					users.getString("address"),
			    					users.getString("city"),
			    					users.getInt("postcode"),
			    					users.getString("phone"));
			  arr.add(u);    
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}
	
	public List<String> getCategories() throws SQLException{
		ArrayList<String> categories = new ArrayList<String>();
		ResultSet rs = conn.fetch("SELECT * FROM category");
		while(rs.next()) categories.add(rs.getString("name"));
		return categories;
	}

	public int getQuantity(String id){
		ResultSet rs = conn.fetch("SELECT product.quantity from product WHERE id="+id);
		try {
			rs.first();
			return rs.getInt("quantity");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void updateDB(String s){
		conn.update(s);
	}
	
	public void updateDB(Object o){
		StringBuilder sb = new StringBuilder();
		if (o instanceof Product) {
			sb.append(" UPDATE  product SET name=\"");
			sb.append(((Product) o).getName() + "\",category=");
			ResultSet rs = conn.fetch("SELECT id FROM category WHERE name=\""+((Product) o).getCategory()+"\";");
			int category = 1; // default case
			try {
				while (rs.next()) {
					category = rs.getInt("id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sb.append( category+",price=");
			sb.append(((Product) o).getPrice() +",image=\"");
			sb.append(((Product) o).getImage() +"\", ratings=\"");
			System.out.println("Size ratings: " + ((Product) o).getRatings().size());
			for (int i=0;i<((Product) o).getRatings().size();i++){
				System.out.println("IDs for SQL: " + ((Product) o).getRatings().get(i).getId());
				sb.append(((Product) o).getRatings().get(i).getId() + ";");
			}
			System.out.println("SQL Rating: " +  sb.toString());
			sb.append("\",description=\"");
			sb.append(((Product) o).getDescription() + "\",quantity=");
			sb.append(((Product) o).getQuantity());
			sb.append(" WHERE id=");
			sb.append(((Product) o).getId() +";");
		}
		else if (o instanceof Order){
			sb.append("UPDATE orders SET orderStatus=");
			// orderStatus
			switch (((Order) o).getOrderStatus()) {
			case IN_PROCESS : sb.append("1,");
					break;
			case SHIPPED : sb.append("2,");
					break;
			case DELAYED : sb.append("3,");
					break;
			default : sb.append("1,");
					break;
			}
			// products 
			sb.append("products=\"");
			for (int i=0;i<((Order) o).getOrderList().size();i++) {
				Product p = ((Order) o).getOrderList().get(i);
				sb.append(p.getId() + ":" + p.getQuantity() + ";");
			}
			sb.append("\",price=");

			// price
			int p = 0;
			for (int i=0;i<((Order) o).getOrderList().size();i++) p+= ((Order) o).getOrderList().get(i).getPrice() * ((Order) o).getOrderList().get(i).getQuantity();
			sb.append(p+",date=\"");
			// date
			sb.append(new Date(((Order) o).getOrderDate().getTime()) +"\"");
			// finish request
			sb.append(" WHERE id=\"");
			sb.append(((Order) o).getOrderId() +"\";");
		}
		else if (o instanceof User){
			sb.append("UPDATE user SET name=\"");
			sb.append(((User) o).getName() + "\",orders=\"");
			for (int i=0;i<((User) o).getOrders().size();i++){
				sb.append( ((User) o).getOrders().get(i).getOrderId() + ";");
			}
			sb.append("\",email=\"");
			sb.append(((User) o).getEmail() + "\",password=\"");
			sb.append(((User) o).getPassword() + "\",address=\"");
			sb.append(((User) o).getAddress() + "\", city=\"");
			sb.append(((User) o).getCity() + "\", phone=\"");
			sb.append(((User) o).getPhone() + "\", postcode=");
			sb.append(((User) o).getPostcode() + ",image=\"");
			sb.append(((User) o).getImage() + "\",admin=");
			if (((User) o).getAdmin()) sb.append("1");
			else sb.append("0");
			sb.append(" WHERE id=");
			sb.append(((User) o).getId() + ";");
		}
		else if (o instanceof Rating){
			sb.append("UPDATE rating SET comment=\"");
			sb.append(((Rating) o).getComment() + "\", rating=");
			sb.append(((Rating) o).getStars() + ", user=");
			sb.append(((Rating) o).getAuthor().getId() + " WHERE id=");
			sb.append(((Rating) o).getId() + ";");
		}
		else System.err.println("This is not an updateable Object");
		updateDB(sb.toString());
	}

	public void deleteDB(Object o){
		// update DB function from connection class
		if (o instanceof Product) updateDB("DELETE FROM product WHERE id="+ ((Product) o).getId() + ";");
		else if (o instanceof Order) updateDB("DELETE FROM orders WHERE id=\""+ ((Order) o).getOrderId() + "\";");
		else if (o instanceof User) {
			// delete all comments from the user
			ResultSet rs = conn.fetch("SELECT * FROM rating WHERE user="+ ((User)o).getId() + ";");
			try {
				ArrayList<Rating> ratings = toRatings(rs);
				for (int i=0; i< ratings.size();i++) deleteDB(ratings.get(i));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// delete all their orders
			if (((User)o).getOrders().size()>0) {
			StringBuilder sb = new StringBuilder("SELECT * FROM orders WHERE id=\"");
			for (int i=0;i< ((User)o).getOrders().size();i++) {
				sb.append(((User)o).getOrders().get(i).getOrderId() + "\"");
				if (i+1<((User)o).getOrders().size()) sb.append(" OR id=\"");
			}
			sb.append(";");
			ResultSet rs2 = conn.fetch(sb.toString());
			try {
				ArrayList<Order> orders = toOrders(rs2);
				for (int i=0; i< orders.size();i++) deleteDB(orders.get(i));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
			// delete the user itself
			updateDB("DELETE FROM user WHERE id=" + ((User) o).getId() +";");
		}
		else if (o instanceof Rating) {
			// delete the rating from the product list
				try {
					Product p = toProducts(conn.fetch("SELECT * FROM product WHERE ratings LIKE '%" + ((Rating)o).getId() + "%';")).get(0);
					for (int i=0;i<p.getRatings().size();i++) if (p.getRatings().get(i).getId() == ((Rating)o).getId()) p.getRatings().remove(i);
					updateDB(p);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			// delete the rating from DB
				updateDB("DELETE FROM rating WHERE id=" + ((Rating)o).getId() + ";");
		}
		else System.err.println("No deleteable Object-class");
	}

	public void insertDB(Object o){
		StringBuilder sb = new StringBuilder();
		if (o instanceof Product) {
			sb.append(" INSERT INTO  product (name,category,price,image,description,quantity) VALUES (\"");
			sb.append(((Product) o).getName() + "\",");
			ResultSet rs = conn.fetch("SELECT id FROM category WHERE name=\""+((Product) o).getCategory()+"\";");
			int category = 1; // default case
			try {
				while (rs.next()) {
					category = rs.getInt("id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sb.append(category+",");
			sb.append(((Product) o).getPrice() +",\"");
			sb.append(((Product) o).getImage() +"\",\"");
			sb.append(((Product) o).getDescription() + "\",");
			sb.append(((Product) o).getQuantity());
			sb.append(");");
		}
		else if (o instanceof Order){
			sb.append("INSERT INTO orders (id, orderStatus, products, price, date) VALUES (\"");
			String orderID = UUID.randomUUID().toString();
			orderID = orderID.replaceAll("\"", "'");
			sb.append(orderID + "\",");
			// orderStatus
			switch (((Order) o).getOrderStatus()) {
			case IN_PROCESS : sb.append("1,");
					break;
			case SHIPPED : sb.append("2,");
					break;
			case DELAYED : sb.append("3,");
					break;
			default : sb.append("1,");
					break;
			}
			// products 
			sb.append("\"");
			for (int i=0;i<((Order) o).getOrderList().size();i++) {
				Product p = ((Order) o).getOrderList().get(i);
				sb.append(p.getId() + ":" + p.getQuantity() + ";");
			}
			sb.append("\",");

			// price
			int p = 0;
			for (int i=0;i<((Order) o).getOrderList().size();i++) p+= ((Order) o).getOrderList().get(i).getPrice() * ((Order) o).getOrderList().get(i).getQuantity();
			sb.append(p+",");
			// date
			sb.append("\"");

			sb.append(new Date(((Order) o).getOrderDate().getTime()));
			sb.append("\"");
			// finish request
			sb.append(");");
		}
		else if (o instanceof User){
			sb.append("INSERT INTO user (name,orders,email,password,admin,image,address,city,postcode,phone) VALUES (\"");
			sb.append(((User) o).getName() + "\",\"");
			for (int i=0;i<((User) o).getOrders().size();i++){
				sb.append( ((User) o).getOrders().get(i).getOrderId() + ";");
			}
			sb.append("\",\"");
			sb.append(((User) o).getEmail() + "\",\"");
			sb.append(((User) o).getPassword() + "\",");
			if (((User) o).getAdmin()) sb.append("1,\"");
			else sb.append("0,\"");
			sb.append(((User) o).getImage() +"\",\"");
			sb.append(((User) o).getAddress() + "\", \"");
			sb.append(((User) o).getCity() + "\",");
			sb.append(((User) o).getPostcode() + ",\"");
			sb.append(((User) o).getPhone() + "\");");
		}
		else if (o instanceof Rating){
			sb.append("INSERT INTO rating (comment, rating, user) VALUES (\"");
			sb.append(((Rating) o).getComment() +  "\", ");
			sb.append(((Rating) o).getStars() + ", ");
			sb.append(((Rating) o).getAuthor().getId() + ");");
		}
		else System.err.println("This is not an Object for insertion");
		updateDB(sb.toString());
	}
	
	public void notify(String s, String s1) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(s, s1) );
    }
	

}

