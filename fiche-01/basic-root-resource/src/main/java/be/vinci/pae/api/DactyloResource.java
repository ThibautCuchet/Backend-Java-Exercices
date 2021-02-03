package be.vinci.pae.api;

import java.util.List;

import be.vinci.pae.domain.Dactylo;
import be.vinci.pae.services.DataServiceDactyloCollection;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/texts")
public class DactyloResource {	

    
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Dactylo create(Dactylo dact) {
		if (dact == null || dact.getContent() == null || dact.getContent().isEmpty() || !Dactylo.levels.contains(dact.getLevel()))
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info or unauthorized text level").type("text/plain").build());
		DataServiceDactyloCollection.addDactylo(dact);

		return dact;
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Dactylo getFilm(@PathParam("id") int id) {
		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
					.type("text/plain").build());
		Dactylo dactyloFound = DataServiceDactyloCollection.getDactylo(id);

		if (dactyloFound == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return dactyloFound;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Dactylo updateFilm(Dactylo dact, @PathParam("id") int id) {

		if (dact == null || dact.getContent() == null || dact.getContent().isEmpty() || !Dactylo.levels.contains(dact.getLevel()))
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info or unauthorized text level").type("text/plain").build());

		dact.setId(id);
		Dactylo updateDactylo = DataServiceDactyloCollection.updateDactylo(dact);

		if (updateDactylo == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return updateDactylo;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Dactylo deleteFilm(@PathParam("id") int id) {
		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
					.type("text/plain").build());

		Dactylo deletedDactylo = DataServiceDactyloCollection.deleteDactylo(id);

		if (deletedDactylo == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return deletedDactylo;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Dactylo> getAllFilms(@DefaultValue("-1") @QueryParam("level") String level) {
		if (!level.equals("-1"))
			return DataServiceDactyloCollection.getDactylos(level);
		return DataServiceDactyloCollection.getDactylos();

	}

}
