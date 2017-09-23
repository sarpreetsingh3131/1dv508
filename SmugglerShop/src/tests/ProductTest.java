package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import baseClasses.Product;
import baseClasses.Rating;

/**
 * Simple product test
 * 
 * @author SarpreetSingh
 *
 */
public class ProductTest {

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

	@Test
	public void testOneProduct() {
		Product product = new Product("Iphone", "Mobile", 199.99, "Made in China", null, 1, 0, new ArrayList<Rating>());

		// check
		assertEquals("Iphone", product.getName());
		assertEquals("Mobile", product.getCategory());
		assertEquals("" + 199.99, "" + product.getPrice());
		assertEquals(null, product.getImage());
		assertEquals("Made in China", product.getDescription());
		assertEquals(1, product.getQuantity());
		assertEquals(0, product.getId());

		// ID is not updated, since it should never change -Ben
		// update the product details
		product.setName("Samsung");
		product.setCategory("Mobile");
		product.setPrice(99.99);
		product.setImage(null);
		product.setDescription("Copy of iphone");
		product.setQuantity(1);
		
		// check again
		assertEquals("Samsung", product.getName());
		assertEquals("Mobile", product.getCategory());
		assertEquals("" + 99.99, "" + product.getPrice());
		assertEquals(null, product.getImage());
		assertEquals("Copy of iphone", product.getDescription());
		assertEquals(1, product.getQuantity());

	}

	@Test
	public void testPriceAndQuantity() {
		Product product = new Product();
		boolean price = false;
		boolean quantity = false;

		// set price less than 0.
		try {
			product.setPrice(-1.00);
		} catch (IllegalArgumentException a) {
			price = true;
		}

		// set negative quantity
		try {
			product.setQuantity(-1);
		} catch (IllegalArgumentException a) {
			quantity = true;
		}

		assertTrue(price);
		assertTrue(quantity);

	}

	@Test
	public void testEquals() {
		// same products
		Product product1 = new Product("Iphone", "Mobile", 199.99, "Made in China", null, 1, 0, new ArrayList<Rating>());
		Product product2 = new Product("Iphone", "Mobile", 199.99, "Made in China", null, 1, 0, new ArrayList<Rating>());

		assertEquals(true, product1.equals(product2));

		// change product2 details
		product2.setName("Samsung");
		product2.setPrice(99.99);
		product2.setDescription("Copy of iphone");

		assertEquals(false, product1.equals(product2));

	}
}