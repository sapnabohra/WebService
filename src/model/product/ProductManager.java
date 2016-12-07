package model.product;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dao.ProductDAO;
import model.partner.Partners;
import model.product.Product;
import model.customer.Customer;
import model.order.OrderManager;
import model.order.ProductOrder;

public class ProductManager {
	
	ProductDAO  prodDAO = new ProductDAO();
	
	public Set<Product> getAllProducts()
	{
		
		Set<Product> products = new HashSet<Product>();
		products = prodDAO.fetchAllProducts();
		return products;
	}
	

	public Product serachProduct(String productID)
	{
		// Searching Product from DB based on ProductID
		Product product = prodDAO.searchProduct(productID);
		return product;

	}
	
	 /* Adding Products to Database*/
	public String addProduct(int productID,String productDescription, double unitPrice, int availableQuantity)
	{
	
		Product product = new Product();
		product.setProductID(productID);
		product.setAvailableQuantity(availableQuantity);
		product.setProductDescription(productDescription);
		product.setUnitPrice(unitPrice);
		String status = prodDAO.addProduct(product);
		return status;
	}
	
	 /* Adding Products to cart*/
		public void buyProduct(String email,Date orderDate,Map<String,Integer> productQuantity){
			Product product = new Product();
			OrderManager om = new OrderManager();
			 double totalPrice = 0.0;
			 Set<ProductOrder>  productEligible = new HashSet<ProductOrder>();
			 ProductOrder productOrder ;
			//looping through product to check availability and calculate total price
	          for(Map.Entry<String, Integer> entry : productQuantity.entrySet())
	          {
	        	product = serachProduct(entry.getKey());
	        	int orderQuantity = entry.getValue();
			if(product.getAvailableQuantity() >= orderQuantity){
				//adding eligible products
				productOrder = new ProductOrder();
				productOrder.setOrderQuantity(orderQuantity);
				productOrder.setProduct(product);
				productEligible.add(productOrder);
				totalPrice = totalPrice + product.getUnitPrice() * orderQuantity;
			}else{
				System.out.println("The product "+" " + product.getProductDescription()+ " " +" is currently out of stock");
			}
	          }
		        //Palce order
	        //  Date orderDate, double totalPrice, Set<ProductOrder> productOrder,String customerEmail
		        om.orderPlacement(orderDate,totalPrice,  productEligible,email);
		}

}
