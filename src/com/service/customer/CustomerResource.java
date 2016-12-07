package com.service.customer;


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


import com.service.representation.customer.CustomerRepresentation;
import com.service.representation.customer.CustomerRequest;
import com.service.workflow.customer.CustomerActivity;

/**
 * @author me
 *
 */
@WebService(targetNamespace = "http://customer.service.com/", endpointInterface = "com.service.customer.CustomerService", portName = "CustomerResourcePort", serviceName = "CustomerResourceService")
@Path("/customerservice/")
public class CustomerResource implements CustomerService {

	@GET
	@Produces({"application/json"})
	@Path("/customer")
	//@Cacheable(cc="public, maxAge=3600") example for caching
	public Set<CustomerRepresentation> getCustomer() {
		System.out.println("GET METHOD Request for all customers .............");
		CustomerActivity custActivity = new CustomerActivity();
		return custActivity.getCustomer();	
	}
	
	@GET
	@Produces({"application/json"})
	@Path("/customer/{customerId}")
	public CustomerRepresentation getCustomer(@PathParam("customerId") String id,@Context UriInfo uriInfo) {
		System.out.println("GET METHOD Request from Client with customerId String ............." + id);
		CustomerActivity custActivity = new CustomerActivity();
		CustomerRepresentation custRep = custActivity.getCustomer(id);
		/*Adding links*/
		custRep.addLink(getUriForSelf(uriInfo,custRep), "self", "GET", "application/json");
		custRep.addLink(getUriForAuth(uriInfo,custRep), "Auth", "POST", "application/json");
		custRep.addLink(getUriForDelete(uriInfo,custRep), "Delete", "DELETE", "application/json");
		return custRep;
	}
	
	@POST
	@Produces({"application/json"})
	@Path("/customer")
	public String addCustomer(CustomerRequest  customerRequest) {
		//System.out.println("POST METHOD Request from Client with ............." + employeeRequest.getFirstName() + "  " + employeeRequest.getLastName());
		CustomerActivity custActivity = new CustomerActivity();
		return custActivity.addCustomer(customerRequest.getCustomerName(),customerRequest.getEmail(),customerRequest.getPassword(),customerRequest.getCustomerPhone(),customerRequest.getCustomerAddress());
	}
	@DELETE
	@Produces({"application/json"})
	@Path("/customer/{customerId}")
	public Response deleteCustomer(@PathParam("customerId") String id) {
		System.out.println("Delete METHOD Request from Client with customerId String ............." + id);
		CustomerActivity custActivity = new CustomerActivity();
		String res = custActivity.deleteCustomer(id);
		if (res.equals("OK")) {
			return Response.status(Status.OK).build();
		}
		return null;
	}

	@POST
	@Produces({"application/json"})
	@Path("/customer/auth")
	public String authenticateCustomer(CustomerRequest  customerRequest) {
		CustomerActivity custActivity = new CustomerActivity();
		return	custActivity.authenticateCustomer(customerRequest.getEmail(), customerRequest.getPassword());

	}
	/*Method to generate link for itself*/
	private String getUriForSelf(UriInfo uriInfo,CustomerRepresentation custRep){
		String url = uriInfo.getBaseUriBuilder()
							.path(CustomerResource.class)
							.path("customer")
							.path("Cust_ID" +custRep.getCustomerID())
							.build()
							.toString();
		return url;
		
	}
	private String getUriForAuth(UriInfo uriInfo,CustomerRepresentation custRep){
		String url = uriInfo.getBaseUriBuilder()
							.path(CustomerResource.class)
							.path("customer")
							.path("auth")
							.build()
							.toString();
		return url;
		
	}
	private String getUriForDelete(UriInfo uriInfo,CustomerRepresentation custRep){
		String url = uriInfo.getBaseUriBuilder()
							.path(CustomerResource.class)
							.path("customer")
							.path("Cust_ID" +custRep.getCustomerID())
							.build()
							.toString();
		return url;
		
	}
	
	
}