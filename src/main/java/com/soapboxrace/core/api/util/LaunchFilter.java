package com.soapboxrace.core.api.util;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.soapboxrace.core.bo.AuthenticationBO;
import com.soapboxrace.core.bo.ParameterBO;
import com.soapboxrace.jaxb.login.LoginStatusVO;

@LauncherChecks
@Provider
@Priority(Priorities.AUTHORIZATION)
public class LaunchFilter implements ContainerRequestFilter {

	@EJB
	private AuthenticationBO authenticationBO;

	@EJB
	private ParameterBO parameterBO;

	@Context
	private HttpServletRequest sr;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if (!isSpecificLaunhcerHeader(requestContext)) {
			return;
		}
		if (!isValidLauncher(requestContext)) {
			return;
		}
		if (isBanned(requestContext)) {
			return;
		}
	}

	private boolean isSpecificLaunhcerHeader(ContainerRequestContext requestContext) {
		String gameLauncherHeader = parameterBO.getStrParam("GAME_LAUNCHER_HEADER");
		if (!gameLauncherHeader.isEmpty()) {
			String[] split = gameLauncherHeader.split(";");
			String header = split[0];
			String value = split[1];
			String headerString = requestContext.getHeaderString(header);
			if (headerString == null || !headerString.equals(value)) {
				LoginStatusVO loginStatusVO = new LoginStatusVO(0L, "", false);
				loginStatusVO.setDescription("Invalid launcher version.");
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(loginStatusVO).build());
				return false;
			}
		}
		return true;
	}

	private boolean isValidLauncher(ContainerRequestContext requestContext) {
		if (!parameterBO.getBoolParam("ENABLE_METONATOR_LAUNCHER_PROTECTION")) {
			return true;
		}
		String hwid = requestContext.getHeaderString("X-HWID");
		String userAgent = requestContext.getHeaderString("User-Agent");
		String gameLauncherHash = requestContext.getHeaderString("X-GameLauncherHash");

		if ((userAgent == null || !userAgent.equals("GameLauncher (+https://github.com/SoapboxRaceWorld/GameLauncher_NFSW)"))
				|| (hwid == null || hwid.trim().isEmpty()) || (gameLauncherHash == null || gameLauncherHash.trim().isEmpty())) {
			LoginStatusVO loginStatusVO = new LoginStatusVO(0L, "", false);
			loginStatusVO.setDescription("Invalid launcher version.");
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(loginStatusVO).build());
			return false;
		}
		return true;
	}

	private boolean isBanned(ContainerRequestContext requestContext) {
		String hwid = requestContext.getHeaderString("X-HWID");
		LoginStatusVO checkIsBanned = authenticationBO.checkIsBanned(hwid, sr.getParameter("email"));
		if (checkIsBanned != null) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(checkIsBanned).build());
			return true;
		}
		return false;
	}

}
