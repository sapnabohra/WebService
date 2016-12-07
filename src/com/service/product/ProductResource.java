package com.service.product;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.jws.WebService;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;

import com.service.customer.CustomerResource;
import com.service.order.OrderResource;
import com.service.representation.customer.CustomerRepresentation;
import com.service.representation.order.OrderRepresentation;
import com.service.representation.partner.PartnerRepresentation;
import com.service.representation.product.ProductRepresentation;
import com.service.representation.product.ProductRequest;
import com.service.workflow.product.ProductActivity;


@Path("/productservice/")
public class ProductResource implements ProductService {

	@GET
	@Produces({"application/json"})
	@Path("/product")
	//@Cacheable(cc="public, maxAge=3600") example for caching
	public Set<ProductRepresentation> getProduct() {
		System.out.println("GET METHOD Request for all customers .............");
		ProductActivity proActivity = new ProductActivity();
		return proActivity.getProduct();	
	}
	
	/*Product*/
	@GET
	@Produces({"application/json"})
	@Path("/product/{productId}")
	public ProductRepresentation getProduct(@PathParam("productId") String id,@Context UriInfo uriInfo) {
		System.out.println("GET METHOD Request from Client with productId String ............." + id);
		ProductActivity proActivity = new ProductActivity();
		ProductRepresentation prdRep = proActivity.getProduct(id);
		/*Adding links*/
		prdRep.addLink(getUriForSelf(uriInfo,prdRep), "self", "Get", "application/json");
		prdRep.addLink(getUriForBuy(uriInfo,prdRep),"buy", "POST", "application/json");
		prdRep.addLink(getUriForAdd(uriInfo,prdRep), "add", "POST", "application/json");
		prdRep.addLink(getUriForDelete(uriInfo,prdRep), "delete", "Delete", "application/json");
		prdRep.addLink(getUriForProductOrder(uriInfo,prdRep), "Order", "GET", "application/json");
		prdRep.addLink(getUriForCustomers(uriInfo,prdRep), "customer", "Get", "application/json");
		prdRep.addLink(getUriForOrders(uriInfo,prdRep), "orders", "Get", "application/json");
		return prdRep;
	}
	/*Method to generate link for itself*/
	private String getUriForSelf(UriInfo uriInfo,ProductRepresentation prdRep){
		String url = uriInfo.getBaseUriBuilder()
							.path(ProductResource.class)
							.path("product")
							.path("Prod_ID" +prdRep.getProductID())
							.build()
							.toString();
		return url;
		
	}
	
	
	
	/*Adding Product*/
	@POST
	@Produces({"application/json"})
	@Path("/product")
	public String addProduct(ProductRequest  productRequest) {
		System.out.println("POST METHOD Request from Client with .............Produst detauls" );
		ProductActivity proActivity = new ProductActivity();
		return proActivity.addProduct(productRequest.getProductID(),productRequest.getProductDescription(),productRequest.getUnitPrice(),productRequest.getAvailableQuantity());
	}
	
	/*Method to generate link to Add product*/
	private String getUriForAdd(UriInfo uriInfo, ProductRepresentation prdRep) {
		String url = uriInfo.getBaseUriBuilder()
				.path(ProductResource.class)
				.path("product")
				.build()
				.toString();
		return url;
	}
	
	
	/* Product Deletion */
	@DELETE
	@Produces({"application/json"})
	@Path("/product/{productId}")
	public Response deleteProduct(@PathParam("productId") String id) {
		System.out.println("Delete METHOD Request from Client with productId String ............." + id);
		ProductActivity proActivity = new ProductActivity();
		String res = proActivity.deleteProduct(id);
		if (res.equals("OK")) {
			return Response.status(Status.OK).build();
		}
		return null;
	}
	/*Method to generate link to Delete product*/
	private String getUriForDelete(UriInfo uriInfo,ProductRepresentation prdRep){
		String url = uriInfo.getBaseUriBuilder()
							.path(ProductResource.class)
							.path("product")
							.path("Prod_ID" +prdRep.getProductID())
							.build()
							.toString();
		return url;
		
	}
	
	
	/*Buy Product*/
	@POST
	@Produces({"application/xml" , "application/json"})
	@Path("/product/buy")
	public void buyProduct(String email,Date orderDate,Map<String,Integer> productQuantity)
	{
		ProductActivity proActivity = new ProductActivity();
		proActivity.buyProduct(email, orderDate, productQuantity);
	}
	/*Method to generate link to Buy product*/
	private String getUriForBuy(UriInfo uriInfo,ProductRepresentation prdRep){
		String url = uriInfo.getBaseUriBuilder()
							.path(ProductResource.class)
							.path("product")
							.path("buy")
							.build()
							.toString();
		return url;
		
	}
	
	
	/*Method to generate link to get product order*/
	private String getUriForProductOrder(UriInfo uriInfo, ProductRepresentation prdRep) {
		String url = uriInfo.getBaseUriBuilder()
				.path(OrderResource.class)
				.path("order")
				.path("Product Order: "+prdRep.getProductOrder())
				.build()
				.toString();
		return url;
	}
	
	/*Method to generate link to customers*/
	private String getUriForCustomers(UriInfo uriInfo, ProductRepresentation prdRep) {
		 String url = uriInfo.getBaseUriBuilder()
					.path(CustomerResource.class)
					.path("customer")
					.build()
					.toString();
		 return url;
	}
	
	
	/*Method to generate link to orders*/
	private String getUriForOrders(UriInfo uriInfo, ProductRepresentation prdRep) {
		 String url = uriInfo.getBaseUriBuilder()
					.path(OrderResource.class)
					.path("order")
					.build()
					.toString();
		 return url;
	}
	
	
}