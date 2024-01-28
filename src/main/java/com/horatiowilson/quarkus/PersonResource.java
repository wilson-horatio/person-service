package com.horatiowilson.quarkus;

import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.quarkus.panache.common.Sort;

@Path("/person")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    @GET
    public List<Person> getAll() throws Exception {
        return Person.findAll(Sort.ascending("last_name")).list();
    }

    @GET
    @Path("{id}")
    public Person getPerson(@PathParam Long id) throws Exception {
        if (id == null) throw new WebApplicationException("id must not be NULL");
        Person p = Person.findById(id);
        if (p == null) throw new WebApplicationException("There is no person with id = " + id);
        return p;
    }

    @POST
    @Transactional
    public Response create(Person p) {
        if (p == null || p.getId() != null)
            throw new WebApplicationException("id != null");
        p.persist();
        return Response.ok(p).status(200).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Person update(@PathParam Long id, Person p) {
        Person entity = Person.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Person with id of " + id + " does not exist.", 404);
        }
        if (p.getSalutation() != null) entity.setSalutation(p.getSalutation());
        if (p.getFirstName() != null) entity.setFirstName(p.getFirstName());
        if (p.getLastName() != null) entity.setLastName(p.getLastName());
        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
        Person entity = Person.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Person with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }

    @GET
    @Path("count")
    public Long count() {
        return Person.count();
    }

    @GET
    @Path("search/{salutation}")
    public List<Person> getPersonBySalutation(@PathParam String salutation) throws Exception {
        List<Person> lst = Person.find("salutation", salutation).list();
        return lst;
    }

}

