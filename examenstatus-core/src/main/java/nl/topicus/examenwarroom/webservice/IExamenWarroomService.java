package nl.topicus.examenwarroom.webservice;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import nl.topicus.examenwarroom.data.State;
import nl.topicus.examenwarroom.data.Taak;

public interface IExamenWarroomService
{
	@GET
	@Path("/taken/{straatId}")
	@Produces("application/json")
	public List<Taak> getTaken(@PathParam("straatId") long straatId,
			@QueryParam("token") String token);

	@GET
	@Path("/taken/aantallen/totaal")
	@Produces("application/json")
	public int getAantalTakenTeDraaien(@QueryParam("token") String token);

	@GET
	@Path("/taken/aantallen/perstate")
	@Produces("application/json")
	public Map<State, Integer> getTaakAantallen(@QueryParam("token") String token);

	@GET
	@Path("/taken/niveau")
	@Produces("application/json")
	public String getNiveauBerekeningen(@QueryParam("token") String token);
}
