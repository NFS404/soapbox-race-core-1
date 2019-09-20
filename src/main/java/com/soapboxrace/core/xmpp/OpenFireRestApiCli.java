package com.soapboxrace.core.xmpp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.igniterealtime.restclient.entity.MUCRoomEntities;
import org.igniterealtime.restclient.entity.MUCRoomEntity;
import org.igniterealtime.restclient.entity.SessionEntities;
import org.igniterealtime.restclient.entity.SessionEntity;
import org.igniterealtime.restclient.entity.UserEntity;

import com.soapboxrace.core.bo.ParameterBO;
import com.soapboxrace.core.dao.TokenSessionDAO;

@Startup
@Singleton
public class OpenFireRestApiCli {
	private String openFireToken;
	private String openFireAddress;
	private boolean restApiEnabled = false;

	@EJB
	private ParameterBO parameterBO;
	
	@EJB
	private TokenSessionDAO tokenDAO;

	@PostConstruct
	public void init() {
		openFireToken = parameterBO.getStrParam("OPENFIRE_TOKEN");
		openFireAddress = parameterBO.getStrParam("OPENFIRE_ADDRESS");
		if (openFireToken != null && openFireAddress != null) {
			restApiEnabled = true;
		}
		createUpdatePersona("sbrw.engine.engine", openFireToken);
	}

	private Builder getBuilder(String path) {
		return getBuilder(path, null);
	}

	private Builder getBuilder(String path, HashMap<String, String> queryString) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(openFireAddress).path(path);

		if (queryString != null) {
			Iterator<Entry<String, String>> iterator = queryString.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> next = iterator.next();
				String key = next.getKey();
				String value = next.getValue();
				target = target.queryParam(key, value);
			}
		}
		Builder request = target.request(MediaType.APPLICATION_XML);
		request.header("Authorization", openFireToken);
		return request;
	}
    
	@AccessTimeout(value=20000)
	@Lock(LockType.WRITE)
	public void createUpdatePersona(String user, String password) {
//		if (!restApiEnabled) {
//			return;
//		}
		Builder builder = getBuilder("users/" + user);
		Response response = builder.get();
		if (response.getStatus() == 200) {
			response.close();
			UserEntity userEntity = builder.get(UserEntity.class);
			userEntity.setPassword(password);
			builder = getBuilder("users/" + user);
			builder.put(Entity.entity(userEntity, MediaType.APPLICATION_XML));
		} else {
			response.close();
			builder = getBuilder("users");
			UserEntity userEntity = new UserEntity(user, null, null, password);
			builder.post(Entity.entity(userEntity, MediaType.APPLICATION_XML));
		}
		response.close();
	}
  
	@AccessTimeout(value=20000)
	public void createUpdatePersona(Long personaId, String password) {
		String user = "sbrw." + personaId.toString();
		createUpdatePersona(user, password);
	}

	@AccessTimeout(value=20000)
    public int getTotalOnlineUsers() {
		return tokenDAO.getUsersOnlineCount();
	}

	// http://192.168.0.9:9090/plugins/restapi/v1/chatrooms?type=all&search=group
	@AccessTimeout(value=20000)
	public List<Long> getAllPersonaByGroup(Long personaId) {
//		if (!restApiEnabled) {
//			return new ArrayList<>();
//		}
		HashMap<String, String> queryString = new HashMap<>();
		queryString.put("type", "all");
		queryString.put("search", "group");
		Builder builder = getBuilder("chatrooms", queryString);

		MUCRoomEntities roomEntities = builder.get(MUCRoomEntities.class);
		List<MUCRoomEntity> listRoomEntity = roomEntities.getMucRooms();
		if (listRoomEntity != null) {
			String roomName = findPersonaRoomName(personaId, listRoomEntity);
			if (roomName != null) {
				return getAllOccupantInGroup(roomName);
			}
		}
		return new ArrayList<>();
	}
    
	@AccessTimeout(value=20000)
	private String findPersonaRoomName(Long personaId, List<MUCRoomEntity> listRoomEntity) {
		for (MUCRoomEntity entity : listRoomEntity) {
			String roomName = entity.getRoomName();
			Long idOwner = Long.parseLong(roomName.substring(roomName.lastIndexOf('.') + 1));
			if (idOwner.equals(personaId)) {
				return roomName;
			} else {
				List<Long> allOccupantInGroup = getAllOccupantInGroup(roomName);
				for (Long idOccupant : allOccupantInGroup) {
					if (idOccupant.equals(personaId)) {
						return roomName;
					}
				}
			}
		}
		return null;
	}

	// http://192.168.0.9:9090/plugins/restapi/v1/chatrooms/group.channel.5122493.100/participants
	private List<Long> getAllOccupantInGroup(String roomName) {
		Builder builder = getBuilder("chatrooms/" + roomName + "/occupants");
		OccupantEntities occupantEntities = builder.get(OccupantEntities.class);
		if (occupantEntities == null) {
			return new ArrayList<>();
		}
		List<Long> listOfPersona = new ArrayList<Long>();
		for (OccupantEntity entity : occupantEntities.getOccupants()) {
			String jid = entity.getJid();
			Long personaId = Long.parseLong(jid.substring(jid.lastIndexOf('.') + 1));
			listOfPersona.add(personaId);
		}
		return listOfPersona;
	}

	@Lock(LockType.READ)
	@AccessTimeout(value=20000)
	public boolean isOnline(Long personaId) {
//		if (!restApiEnabled) {
//			return false;
//		}
		Builder builder = getBuilder("sessions/sbrw." + personaId.toString());
		Response response = builder.get();
		int length = response.getLength();
		if (length > 100) {
			return true;
		}
		return false;
	}

	@Lock(LockType.READ)
	@AccessTimeout(value=20000)
	public List<SessionEntity> getAllSessions() {
		Builder builder = getBuilder("sessions");
		SessionEntities occupantEntities = builder.get(SessionEntities.class);
		List<SessionEntity> sessions = occupantEntities.getSessions();
		return sessions;
	}

	@Lock(LockType.READ)
	@AccessTimeout(value=20000)
	public boolean isRestApiEnabled() {
		return restApiEnabled;
	}

}

