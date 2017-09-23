package tests;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import baseClasses.Product;
import pages.Mainpage;

public class PageTest {

	@Test
	public void testGetProducts() {
		Mainpage test = new Mainpage();
		List<Product> testList = test.getProducts();
		Iterator<Product> testIterator = testList.iterator();
		while (testIterator.hasNext()) {
			assertNotNull(testIterator.next());
		}

	}

}
