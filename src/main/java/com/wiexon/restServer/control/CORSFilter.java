package com.wiexon.restServer.control;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {
    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {

        System.out.println("calling filter");

        response.getHttpHeaders().add("Access-Control-Allow-Origin","*");
        response.getHttpHeaders().add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
        response.getHttpHeaders().add("Access-Control-Allow-Credentials","true");
        response.getHttpHeaders().add("Access-Control-Allow-Methods","POST, PUT, DELETE, OPTIONS, HEAD");

        return response;
    }
}
