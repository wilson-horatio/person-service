package com.horatiowilson.quarkus;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/hello")
public class HelloResource {
    @ConfigProperty(name = "app.greeting")
    String greeting;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return greeting;
    }
}