package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import baseClasses.ConnectionClass;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ConnectionTest {

	@SuppressWarnings("unused")
	@Test
	public void testConnectionClass() {
		ConnectionClass conn = new ConnectionClass();
	}

	@Test
	public void testFetch() throws SQLException {
		ConnectionClass conn = new ConnectionClass();
		ResultSet test = conn.fetch("select * from sql7117861.product");
		while (test.next()) System.out.println(test.getString("Name"));//assertNotNull(test.getInt(1));
	}
	
	
	@Test
	public void testUpdate() {
		ConnectionClass conn = new ConnectionClass();
		ResultSet test = conn.update("select * from sql7117861.product");
		try {
			test.first();
			test.updateString("Name", "Blaster Pistol");
			test.updateRow();
			assertEquals("Blaster Pistol", test.getString("Name"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Test (expected=SQLException.class)
	public void rowOutOfIndex() throws SQLException{
		ConnectionClass conn = new ConnectionClass();
		ResultSet test = conn.fetch("select * from sql7117861.product");
		test.absolute(-1);
		test.getString(0);
	}
	
	
	

}
