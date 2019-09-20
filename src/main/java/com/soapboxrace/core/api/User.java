package com.soapboxrace.core.api;

import java.net.URI;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.soapboxrace.core.api.util.AuthenticationFilter;
import com.soapboxrace.core.api.util.HwBan;
import com.soapboxrace.core.api.util.LauncherChecks;
import com.soapboxrace.core.api.util.Secured;
import com.soapboxrace.core.bo.AuthenticationBO;
import com.soapboxrace.core.bo.FriendBO;
import com.soapboxrace.core.bo.InviteTicketBO;
import com.soapboxrace.core.bo.PersonaBO;
import com.soapboxrace.core.bo.TokenSessionBO;
import com.soapboxrace.core.bo.UserBO;
import com.soapboxrace.core.jpa.PersonaEntity;
import com.soapboxrace.core.jpa.UserEntity;
import com.soapboxrace.jaxb.http.UserInfo;
import com.soapboxrace.jaxb.login.LoginStatusVO;

@Path("User")
public class User {

	@Context
	UriInfo uri;

	@Context
	private HttpServletRequest sr;

	@EJB
	private AuthenticationBO authenticationBO;

	@EJB
	private UserBO userBO;

	@EJB
	private TokenSessionBO tokenBO;

	@EJB
	private InviteTicketBO inviteTicketBO;

	@EJB
	private FriendBO friendBO;

	@EJB
	private PersonaBO personaBO;

	@POST
	@Secured
	@Path("GetPermanentSession")
	@Produces(MediaType.APPLICATION_XML)
	public Response getPermanentSession(@HeaderParam("securityToken") String securityToken, @HeaderParam("userId") Long userId) {
		URI myUri = uri.getBaseUri();
		String randomUUID = tokenBO.createToken(userId, myUri.getHost());
		UserInfo userInfo = userBO.getUserById(userId);
		userInfo.getUser().setSecurityToken(randomUUID);
		userBO.createXmppUser(userInfo);
		System.out.println("### User logged in (getPermanentSession), ID: " + userId);
		return Response.ok(userInfo).build();
	}

	@POST
	@Secured
	@HwBan
	@Path("SecureLoginPersona")
	@Produces(MediaType.APPLICATION_XML)
	public String secureLoginPersona(@HeaderParam("securityToken") String securityToken, @HeaderParam("userId") Long userId,
			@QueryParam("personaId") Long personaId) {
		tokenBO.setActivePersonaId(securityToken, personaId, false);
		userBO.secureLoginPersona(userId, personaId);
		return "";
	}

	@POST
	@Secured
	@Path("SecureLogoutPersona")
	@Produces(MediaType.APPLICATION_XML)
	public String secureLogoutPersona(@HeaderParam("securityToken") String securityToken, @HeaderParam("userId") Long userId,
			@QueryParam("personaId") Long personaId) {
		PersonaEntity personaEntity = personaBO.getPersonaById(personaId);
		friendBO.sendXmppPresenceToAllFriends(personaEntity, 0);
		tokenBO.setActivePersonaId(securityToken, 0L, true);
		tokenBO.updatePersonaPresence(personaEntity.getPersonaId(), 0);
		return "";
	}

	@POST
	@Secured
	@Path("SecureLogout")
	@Produces(MediaType.APPLICATION_XML)
	public String secureLogout(@HeaderParam("securityToken") String securityToken) {
		Long activePersonaId = tokenBO.getActivePersonaId(securityToken);
		System.out.println("### User logged out (SecureLogout)");
		if (activePersonaId == null || activePersonaId.equals(0l)) {
			return "";
		}
		PersonaEntity personaEntity = personaBO.getPersonaById(activePersonaId);
		friendBO.sendXmppPresenceToAllFriends(personaEntity, 0);
		tokenBO.setActivePersonaId(securityToken, 0L, true);
		tokenBO.updatePersonaPresence(personaEntity.getPersonaId(), 0);
		return "";
	}

	@GET
	@Path("authenticateUser")
	@Produces(MediaType.APPLICATION_XML)
	@LauncherChecks
	public Response authenticateUser(@QueryParam("email") String email, @QueryParam("password") String password) {
		LoginStatusVO loginStatusVO = tokenBO.login(email, password, sr);
		if (loginStatusVO.isLoginOk()) {
			return Response.ok(loginStatusVO).build();
		}
		return Response.serverError().entity(loginStatusVO).build();
	}

	@GET
	@Path("createUser")
	@Produces(MediaType.APPLICATION_XML)
	@LauncherChecks
	public Response createUser(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("inviteTicket") String inviteTicket) {
		LoginStatusVO loginStatusVO = tokenBO.checkGeoIp(sr.getRemoteAddr());
		if (!loginStatusVO.isLoginOk()) {
			return Response.serverError().entity(loginStatusVO).build();
		}
		loginStatusVO = userBO.createUserWithTicket(email, password, inviteTicket);
		if (loginStatusVO != null && loginStatusVO.isLoginOk()) {
			loginStatusVO = tokenBO.login(email, password, sr);
			return Response.ok(loginStatusVO).build();
		}
		return Response.serverError().entity(loginStatusVO).build();
	}

}
