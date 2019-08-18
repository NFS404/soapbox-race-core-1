package com.soapboxrace.core.bo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;

import com.soapboxrace.core.api.util.GeoIp2;
import com.soapboxrace.core.api.util.UUIDGen;
import com.soapboxrace.core.dao.TokenSessionDAO;
import com.soapboxrace.core.dao.UserDAO;
import com.soapboxrace.core.jpa.TokenSessionEntity;
import com.soapboxrace.core.jpa.UserEntity;
import com.soapboxrace.jaxb.login.LoginStatusVO;

@Stateless
public class TokenSessionBO {
	@EJB
	private TokenSessionDAO tokenDAO;

	@EJB
	private UserDAO userDAO;

	@EJB
	private ParameterBO parameterBO;

	@EJB
	private GetServerInformationBO serverInfoBO;

	@EJB
	private OnlineUsersBO onlineUsersBO;

	private static Map<String, TokenSessionEntity> activePersonas = new HashMap<>(300);

	public boolean verifyToken(Long userId, String securityToken) {
		TokenSessionEntity tokenSessionEntity = activePersonas.get(securityToken);
		if (tokenSessionEntity == null) {
			tokenSessionEntity = tokenDAO.findBySecurityToken(securityToken);
		}
		if (tokenSessionEntity == null || !tokenSessionEntity.getUserId().equals(userId)) {
			return false;
		}
		long time = new Date().getTime();
		long tokenTime = tokenSessionEntity.getExpirationDate().getTime();
		if (time > tokenTime) {
			return false;
		}
		tokenSessionEntity.setExpirationDate(getMinutes(6));
		tokenDAO.update(tokenSessionEntity);
		// activePersonas.put(securityToken, tokenSessionEntity);
		return true;
	}

	public String createToken(Long userId, String clientHostName) {
		TokenSessionEntity tokenSessionEntity = null;
		try {
			tokenSessionEntity = tokenDAO.findByUserId(userId);
		} catch (Exception e) {
			// not found
		}
		String randomUUID = UUIDGen.getRandomUUID();
		UserEntity userEntity = userDAO.findById(userId);
		Date expirationDate = getMinutes(15);
		if (tokenSessionEntity == null) {
			tokenSessionEntity = new TokenSessionEntity();
			tokenSessionEntity.setExpirationDate(expirationDate);
			tokenSessionEntity.setSecurityToken(randomUUID);
			tokenSessionEntity.setUserId(userId);
			tokenSessionEntity.setPremium(userEntity.isPremium());
			tokenSessionEntity.setClientHostIp(clientHostName);
			tokenDAO.insert(tokenSessionEntity);
		} else {
			tokenSessionEntity.setExpirationDate(expirationDate);
			tokenSessionEntity.setSecurityToken(randomUUID);
			tokenSessionEntity.setUserId(userId);
			tokenSessionEntity.setPremium(userEntity.isPremium());
			tokenSessionEntity.setClientHostIp(clientHostName);
			tokenDAO.update(tokenSessionEntity);
		}
		return randomUUID;
	}

	public boolean verifyPersona(String securityToken, Long personaId) {
		TokenSessionEntity tokenSession = tokenDAO.findBySecurityToken(securityToken);
		if (tokenSession == null) {
			throw new NotAuthorizedException("Invalid session...");
		}

		UserEntity user = userDAO.findById(tokenSession.getUserId());
		if (!user.ownsPersona(personaId)) {
			throw new NotAuthorizedException("Persona is not owned by user");
		}
		return true;
	}

	private Date getMinutes(int minutes) {
		long time = new Date().getTime();
		time = time + (minutes * 60000);
		Date date = new Date(time);
		return date;
	}

	public LoginStatusVO checkGeoIp(String ip) {
		LoginStatusVO loginStatusVO = new LoginStatusVO(0L, "", false);
		String allowedCountries = serverInfoBO.getServerInformation().getAllowedCountries();
		if (allowedCountries != null && !allowedCountries.isEmpty()) {
			String geoip2DbFilePath = parameterBO.getStrParam("GEOIP2_DB_FILE_PATH");
			GeoIp2 geoIp2 = GeoIp2.getInstance(geoip2DbFilePath);
			if (geoIp2.isCountryAllowed(ip, allowedCountries)) {
				return new LoginStatusVO(0L, "", true);
			} else {
				loginStatusVO.setDescription("GEOIP BLOCK ACTIVE IN THIS SERVER, ALLOWED COUNTRIES: [" + allowedCountries + "]");
			}
		} else {
			return new LoginStatusVO(0L, "", true);
		}
		return loginStatusVO;
	}

	public LoginStatusVO login(String email, String password, HttpServletRequest httpRequest) {
		LoginStatusVO loginStatusVO = checkGeoIp(httpRequest.getRemoteAddr());
		if (!loginStatusVO.isLoginOk()) {
			return loginStatusVO;
		}
		loginStatusVO = new LoginStatusVO(0L, "", false);

		if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
			UserEntity userEntity = userDAO.findByEmail(email);
			if (userEntity != null) {
				int numberOfUsersOnlineNow = onlineUsersBO.getNumberOfUsersOnlineNow();
				int maxOlinePayers = parameterBO.getIntParam("MAX_ONLINE_PLAYERS");
				
				if (userEntity.isPremium()) {
				    LocalDate premiumDate = userEntity.getPremiumDate();
				    LocalDate nowDate = LocalDate.now();
				
				    if (userEntity.getPremiumDate() != null) {
				    Integer days = (int) ChronoUnit.DAYS.between(nowDate, premiumDate.plusDays(186));
				      if (days <= 0) {
				    	  userEntity.setPremium(false);
				    	  userEntity.setPremiumDate(null);
				  		  userDAO.update(userEntity);
				        }
				      }
				  }
				
				if (numberOfUsersOnlineNow >= maxOlinePayers && !userEntity.isPremium()) {
					loginStatusVO.setDescription("SERVER FULL");
					return loginStatusVO;
				}
				if (password.equals(userEntity.getPassword())) {
					if (userEntity.getHwid() == null || userEntity.getHwid().trim().isEmpty()) {
						userEntity.setHwid(httpRequest.getHeader("X-HWID"));
					}

					if (userEntity.getIpAddress() == null || userEntity.getIpAddress().trim().isEmpty()) {
						String forwardedFor;
						if ((forwardedFor = httpRequest.getHeader("X-Forwarded-For")) != null && parameterBO.getBoolParam("USE_FORWARDED_FOR")) {
							userEntity.setIpAddress(parameterBO.getBoolParam("GOOGLE_LB_ENABLED") ? forwardedFor.split(",")[0] : forwardedFor);
						} else {
							userEntity.setIpAddress(httpRequest.getRemoteAddr());
						}
					}

					userEntity.setLastLogin(LocalDateTime.now());
					userDAO.update(userEntity);
					Long userId = userEntity.getId();
					String randomUUID = createToken(userId, null);
					loginStatusVO = new LoginStatusVO(userId, randomUUID, true);
					loginStatusVO.setDescription("");

					return loginStatusVO;
				}
			}
		}
		loginStatusVO.setDescription("LOGIN ERROR");
		return loginStatusVO;
	}

	public Long getActivePersonaId(String securityToken) {
		TokenSessionEntity tokenSessionEntity = activePersonas.get(securityToken);
		if (tokenSessionEntity == null) {
			tokenSessionEntity = tokenDAO.findBySecurityToken(securityToken);
		}
		return tokenSessionEntity.getActivePersonaId();
	}

	public void setActivePersonaId(String securityToken, Long personaId, Boolean isLogout) {
		TokenSessionEntity tokenSessionEntity = tokenDAO.findBySecurityToken(securityToken);

		if (!isLogout) {
			if (!userDAO.findById(tokenSessionEntity.getUserId()).ownsPersona(personaId)) {
				throw new NotAuthorizedException("Persona not owned by user");
			}
		}

		tokenSessionEntity.setActivePersonaId(personaId);
		tokenSessionEntity.setIsLoggedIn(!isLogout);
		tokenDAO.updatePersonaPresence(personaId, 1);
		// activePersonas.put(securityToken, tokenSessionEntity);
		tokenDAO.update(tokenSessionEntity);
	}

	public String getActiveRelayCryptoTicket(String securityToken) {
		TokenSessionEntity tokenSessionEntity = tokenDAO.findBySecurityToken(securityToken);
		return tokenSessionEntity.getRelayCryptoTicket();
	}

	public Long getActiveLobbyId(String securityToken) {
		TokenSessionEntity tokenSessionEntity = tokenDAO.findBySecurityToken(securityToken);
		return tokenSessionEntity.getActiveLobbyId();
	}

	public void setActiveLobbyId(String securityToken, Long lobbyId) {
		TokenSessionEntity tokenSessionEntity = tokenDAO.findBySecurityToken(securityToken);
		tokenSessionEntity.setActiveLobbyId(lobbyId);
		// activePersonas.put(securityToken, tokenSessionEntity);
		tokenDAO.update(tokenSessionEntity);
	}

	public boolean isPremium(String securityToken) {
		return tokenDAO.findBySecurityToken(securityToken).isPremium();
	}

	public boolean isAdmin(String securityToken) {
		return getUser(securityToken).isAdmin();
	}

	public UserEntity getUser(String securityToken) {
		return userDAO.findById(tokenDAO.findBySecurityToken(securityToken).getUserId());
	}

	public void updatePersonaPresence(Long personaId, Integer personaPresence) {
		tokenDAO.updatePersonaPresence(personaId, personaPresence);
	}
}
