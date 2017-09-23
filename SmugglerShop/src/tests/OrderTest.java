package tests;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import baseClasses.Order;
import baseClasses.Product;
import pages.Mainpage;
public class OrderTest {

	private static int test_count = 0;

	/* Is executed before every test method (not test case). */
	@Before
	public void setUp() {
		test_count++;
		System.out.println("Test " + test_count);
	}

	/* Is executed after every test method (not test case). */
	@After
	public void tearDown() {
	}

	/*
	@Test
	public void toOrdersTest(){
		Mainpage test = new Mainpage();
		try {
			ArrayList<Order> testList = test.testOrder();
			Iterator<Order> it = testList.iterator();
			while (it.hasNext()) {
				Iterator<Product> it1 = it.next().getIterator();
				while (it1.hasNext()) {
					Product n = it1.next();
					System.out.println(n.getName() + " : " + n.getQuantity());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	*/

	
	
}
