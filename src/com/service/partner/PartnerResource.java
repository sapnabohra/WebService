package com.service.partner;

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
import com.service.product.ProductResource;
import com.service.representation.partner.PartnerRepresentation;
import com.service.representation.partner.PartnerRequest;
import com.service.representation.product.ProductRepresentation;
import com.service.workflow.partner.PartnerActivity;

@WebService(targetNamespace = "http://partner.service.com/", endpointInterface = "com.service.partner.PartnerService", portName = "PartnerResourcePort", serviceName = "PartnerResourceService")
@Path("/partnerservice/")
public class PartnerResource implements PartnerService {

	@GET
	@Produces({"application/xml" , "application/json"})
	@Path("/partner")
	//@Cacheable(cc="public, maxAge=3600") example for caching
	public Set<PartnerRepresentation> getPartner() {
		System.out.println("GET METHOD Request for all customers .............");
		PartnerActivity partActivity = new PartnerActivity();
		return partActivity.getPartner();	
	}
	
 @GET
	@Produces({"application/xml" , "application/json"})
	@Path("/partner/{partnerId}")
	public PartnerRepresentation getPartner(@PathParam("partnerId") String id,@Context UriInfo uriInfo) {
		System.out.println("GET METHOD Request from Client with partnerId String ............." + id);
		PartnerActivity partActivity = new PartnerActivity();
		PartnerRepresentation partRep = partActivity.getPartner(id);
		/*Adding links*/
		partRep.addLink(getUriForSelf(uriInfo,partRep), "self", "Get", "application/json");
		partRep.addLink(getUriForAdd(uriInfo,partRep), "add", "POST", "application/json");
		partRep.addLink(getUriForDelete(uriInfo,partRep), "delete", "DELETE", "application/json");
		partRep.addLink(getUriForProducts(uriInfo,partRep), "products", "Get", "application/json");
		partRep.addLink(getUriForCustomers(uriInfo,partRep), "customers", "Get", "application/json");
		partRep.addLink(getUriForOrders(uriInfo,partRep), "orders", "Get", "application/json");
		return partRep;
		
	} 
	 

	/*Method to generate link for itself*/
	private String getUriForSelf(UriInfo uriInfo,PartnerRepresentation partRep){
		String url = uriInfo.getBaseUriBuilder()
							.path(ProductResource.class)
							.path("product")
							.path("Prod_ID" +partRep.getPartnerID())
							.build()
							.toString();
		return url;
		
	}
	
	/*Add Partner*/
	@POST
	@Produces({"application/xml" , "application/json"})
	@Path("/partner")
	public PartnerRepresentation addPartner(PartnerRequest  partnerRequest) {
		//System.out.println("POST METHOD Request from Client with ............." + employeeRequest.getFirstName() + "  " + employeeRequest.getLastName());
		PartnerActivity partActivity = new PartnerActivity();
		return partActivity.addPartner(partnerRequest.getPartnerName(),partnerRequest.getpartnerType(),partnerRequest.getProduct());
	}
	 /*Method to generate link to add partner*/
	private String getUriForAdd(UriInfo uriInfo, PartnerRepresentation partRep) {
		 String url = uriInfo.getBaseUriBuilder()
					.path(PartnerResource.class)
					.path("partner")
					.build()
					.toString();
	return url;
	}
	
	
	/*Delete Partner*/
	@DELETE
	@Produces({"application/xml" , "application/json"})
	@Path("/partner/{partnerId}")
	public Response deletePartner(@PathParam("partnerId") String id) {
		System.out.println("Delete METHOD Request from Client with partnerId String ............." + id);
		PartnerActivity partActivity = new PartnerActivity();
		String res = partActivity.deletPartner(id);
		if (res.equals("OK")) {
			return Response.status(Status.OK).build();
		}
		return null;
	}
	/*Method to generate link to delete partner*/
	private String getUriForDelete(UriInfo uriInfo, PartnerRepresentation partRep) {
		 String url = uriInfo.getBaseUriBuilder()
					.path(PartnerResource.class)
					.path("partner")
					.path("Partner ID: " +partRep.getPartnerID())
					.build()
					.toString();
		 return url;
	}
	
	/*Method to generate link to products*/
	private String getUriForProducts(UriInfo uriInfo, PartnerRepresentation partRep) {
		 String url = uriInfo.getBaseUriBuilder()
					.path(ProductResource.class)
					.path("product")
					.build()
					.toString();
		 return url;
	}
	/*Method to generate link to customers*/
	private String getUriForCustomers(UriInfo uriInfo, PartnerRepresentation partRep) {
		 String url = uriInfo.getBaseUriBuilder()
					.path(CustomerResource.class)
					.path("customer")
					.build()
					.toString();
		 return url;
	}
	
	/*Method to generate link to orders*/
	private String getUriForOrders(UriInfo uriInfo, PartnerRepresentation partRep) {
		 String url = uriInfo.getBaseUriBuilder()
					.path(OrderResource.class)
					.path("order")
					.build()
					.toString();
		 return url;
	}
	
}