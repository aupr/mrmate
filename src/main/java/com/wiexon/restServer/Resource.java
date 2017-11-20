package com.wiexon.restServer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/mrmate")
public class Resource {
    @GET
    public String getData() {
        return "Hello Dhaka";
    }
}
