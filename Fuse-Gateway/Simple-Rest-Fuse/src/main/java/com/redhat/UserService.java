package com.redhat;

import org.apache.log4j.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/UserService")
public class UserService {
   
final static Logger logger = Logger.getLogger(UserService.class);

   @GET
   @Path("/get_data")
   @Produces(MediaType.APPLICATION_JSON)
	
   public String getUser() {
      String reponse = "This is standard response from REST";
       logger.info("Service is invoked !!!");
       System.out.println("Service is invoked !!!");
      return reponse;
   }
}
