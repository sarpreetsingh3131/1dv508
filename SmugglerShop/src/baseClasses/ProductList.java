/**
 * 
 */
package baseClasses;

import java.sql.SQLException;
import java.util.ArrayList;
/**
 * @author kaikun
 *
 */
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
 
 
@ManagedBean(name="productList", eager = true)
@ApplicationScoped
public class ProductList extends Page{
     
    private List<Product> products = new ArrayList<Product>();
     
    @PostConstruct
    public void init() { 
    	setContent("select * from product");
		try {
			products = toProducts(content);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public List<Product> getProducts(){
    	init();
    	return products;
    }
    
    public int size(){
    	return products.size();
    }
}