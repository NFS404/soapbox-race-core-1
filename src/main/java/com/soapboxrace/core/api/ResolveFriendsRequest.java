package com.soapboxrace.core.api;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.soapboxrace.core.api.util.Secured;
import com.soapboxrace.core.bo.FriendBO;
import com.soapboxrace.jaxb.http.PersonaBase;
import com.soapboxrace.jaxb.util.MarshalXML;

@Path("/resolvefriendsrequest")
public class ResolveFriendsRequest {

	@EJB
	private FriendBO bo;

	@GET
	@Secured
	@Produces(MediaType.APPLICATION_XML)
	public String resolvefriendsrequest(@QueryParam("personaId") Long personaId, @QueryParam("friendPersonaId") Long friendPersonaId,
			@QueryParam("resolution") int resolution) {
		PersonaBase presonaResult = bo.sendResponseFriendRequest(personaId, friendPersonaId, resolution);
		if (presonaResult == null) {
			return "";
		}
		return MarshalXML.marshal(presonaResult);
//		StringBuilder stringBuilder = new StringBuilder();
//		stringBuilder.append("<PersonaBase xmlns=\"http://schemas.datacontract.org/2004/07/Victory.Service.Objects\" ");
//		stringBuilder.append("xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">");
//		stringBuilder.append("<Badges/>");
//		stringBuilder.append("<IconIndex>26</IconIndex>");
//		stringBuilder.append("<Level>59</Level>");
//		stringBuilder.append("<Motto/>");
//		stringBuilder.append("<Name>NILZAO</Name>");
//		stringBuilder.append("<PersonaId>100</PersonaId>");
//		stringBuilder.append("<Presence>1</Presence>");
//		stringBuilder.append("<Score>0</Score>");
//		stringBuilder.append("<UserId>1</UserId>");
//		stringBuilder.append("</PersonaBase>");
//		return stringBuilder.toString();
	}

}
