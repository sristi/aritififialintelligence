/**
 * 
 */
package com.skpm.ws;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.skpm.facade.UserFacade;
import com.skpm.model.User;

/**
 * @author suresh
 *
 */
@Stateless
@Path("users")
public class UserService {
	@Inject
	private UserFacade userFacade;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void create(User user) {
		userFacade.create(user);
		System.out.println("User creted named="+user.getFullName());
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> findAll() {
		return userFacade.findAll();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User find(@PathParam("id") Long id) {
		return userFacade.find(id);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(User user) {
		userFacade.update(user);
	}

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") Long id) {
		userFacade.remove(id);
	}
	@GET
	@Path("retrieve/{uuid}")
	public Response retrieveSomething(@PathParam("uuid") Long uuid) {
	    if(uuid == null) {
	        return Response.serverError().entity("UUID cannot be blank").build();
	    }
	    User entity = userFacade.find(uuid);
	    if(entity == null) {
	        return Response.status(Response.Status.NOT_FOUND).entity("NO DATA FOUND for UUID: " + uuid).build();
	    }
	   // String json = //convert entity to json
	    return Response.ok(entity, MediaType.APPLICATION_JSON).build();
	}
	@GET
	@Path("pingServer")
	@Produces(MediaType.APPLICATION_JSON)
	public String ping () {
		return "Server has got your ping request";
		
	}
}
