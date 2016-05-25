package com.redhat;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
 
@Path("/")
public class Endpoint {
     
    @GET
    @Path("/test/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAssets(@PathParam("id") String id){
        return null;
    }
 
     
}
