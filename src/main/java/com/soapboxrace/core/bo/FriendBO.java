package com.soapboxrace.core.bo;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import com.soapboxrace.core.dao.FriendListDAO;
import com.soapboxrace.core.dao.PersonaDAO;
import com.soapboxrace.core.dao.TokenSessionDAO;
import com.soapboxrace.core.jpa.FriendListEntity;
import com.soapboxrace.core.jpa.PersonaEntity;
import com.soapboxrace.core.jpa.TokenSessionEntity;
import com.soapboxrace.core.xmpp.OpenFireRestApiCli;
import com.soapboxrace.core.xmpp.OpenFireSoapBoxCli;
import com.soapboxrace.jaxb.http.ArrayOfBadgePacket;
import com.soapboxrace.jaxb.http.ArrayOfFriendPersona;
import com.soapboxrace.jaxb.http.FriendPersona;
import com.soapboxrace.jaxb.http.FriendResult;
import com.soapboxrace.jaxb.http.PersonaBase;
import com.soapboxrace.jaxb.http.PersonaFriendsList;
import com.soapboxrace.jaxb.xmpp.XMPP_FriendPersonaType;
import com.soapboxrace.jaxb.xmpp.XMPP_FriendResultType;
import com.soapboxrace.jaxb.xmpp.XMPP_ResponseTypeFriendResult;
import com.soapboxrace.jaxb.xmpp.XMPP_ResponseTypePersonaBase;
import com.soapboxrace.xmpp.openfire.XmppFriend;

@Stateless
public class FriendBO {

	@EJB
	private PersonaDAO personaDAO;

	@EJB
	private FriendListDAO friendListDAO;

	@EJB
	private DriverPersonaBO driverPersonaBO;

	@EJB
	private PersonaBO personaBO;

	@EJB
	private OpenFireSoapBoxCli openFireSoapBoxCli;

	@EJB
	private OpenFireRestApiCli openFireRestApiCli;

	@EJB
	private TokenSessionDAO tokenSessionDAO;

	public PersonaFriendsList getFriendListFromUserId(Long userId) {
		ArrayOfFriendPersona arrayOfFriendPersona = new ArrayOfFriendPersona();
		List<FriendPersona> friendPersonaList = arrayOfFriendPersona.getFriendPersona();

		List<FriendListEntity> friendList = friendListDAO.findByOwnerId(userId);
		for (FriendListEntity entity : friendList) {

			PersonaEntity personaEntity = personaDAO.findById(entity.getPersonaId());
			if (personaEntity == null) {
				continue;
			}

			int presence = 3;
			if (entity.getIsAccepted()) {
				presence = driverPersonaBO.getPersonaPresenceByName(personaEntity.getName()).getPresence();
			}
			addPersonaToFriendList(friendPersonaList, personaEntity, presence);
		}

		PersonaFriendsList personaFriendsList = new PersonaFriendsList();
		personaFriendsList.setFriendPersona(arrayOfFriendPersona);
		return personaFriendsList;
	}

	private void addPersonaToFriendList(List<FriendPersona> friendPersonaList, PersonaEntity personaEntity, int presence) {
		FriendPersona friendPersona = new FriendPersona();
		friendPersona.setIconIndex(personaEntity.getIconIndex());
		friendPersona.setLevel(personaEntity.getLevel());
		friendPersona.setName(personaEntity.getName());
		friendPersona.setOriginalName(personaEntity.getName());
		friendPersona.setPersonaId(personaEntity.getPersonaId());
		friendPersona.setPresence(presence);
		friendPersona.setSocialNetwork(0);
		friendPersona.setUserId(personaEntity.getUser().getId());
		friendPersonaList.add(friendPersona);
	}

	public FriendResult sendFriendRequest(Long personaId, String displayName, String reqMessage) {
		PersonaEntity personaSender = personaDAO.findById(personaId);
		PersonaEntity personaInvited = personaDAO.findByName(displayName);

		if (personaSender == null || personaInvited == null) {
			return null;
		}

		if (personaSender.getPersonaId() == personaInvited.getPersonaId()) {
			return null;
		}

		FriendListEntity friendListEntity = friendListDAO.findByOwnerIdAndFriendPersona(personaSender.getUser().getId(), personaInvited.getPersonaId());
		if (friendListEntity != null) {
			return null;
		}

		XMPP_FriendPersonaType friendPersonaType = new XMPP_FriendPersonaType();
		friendPersonaType.setIconIndex(personaSender.getIconIndex());
		friendPersonaType.setLevel(personaSender.getLevel());
		friendPersonaType.setName(personaSender.getName());
		friendPersonaType.setOriginalName(personaSender.getName());
		friendPersonaType.setPersonaId(personaSender.getPersonaId());
		friendPersonaType.setPresence(3);
		friendPersonaType.setUserId(personaSender.getUser().getId());

		XmppFriend xmppFriend = new XmppFriend(personaInvited.getPersonaId(), openFireSoapBoxCli);
		xmppFriend.sendFriendRequest(friendPersonaType);

		// Insert db record for invited player
		FriendListEntity friendListInsert = new FriendListEntity();
		friendListInsert.setUserOwnerId(personaInvited.getUser().getId());
		friendListInsert.setUserId(personaSender.getUser().getId());
		friendListInsert.setPersonaId(personaSender.getPersonaId());
		friendListInsert.setIsAccepted(false);
		friendListDAO.insert(friendListInsert);

		FriendPersona friendPersona = new FriendPersona();
		friendPersona.setIconIndex(personaInvited.getIconIndex());
		friendPersona.setLevel(personaInvited.getLevel());
		friendPersona.setName(personaInvited.getName());
		friendPersona.setOriginalName(personaInvited.getName());
		friendPersona.setPersonaId(personaInvited.getPersonaId());
		friendPersona.setPresence(0);
		friendPersona.setSocialNetwork(0);
		friendPersona.setUserId(personaInvited.getUser().getId());

		FriendResult friendResult = new FriendResult();
		friendResult.setPersona(friendPersona);
		friendResult.setResult(0);
		return friendResult;
	}

	public PersonaBase sendResponseFriendRequest(Long personaId, Long friendPersonaId, int resolution) {
		// Execute some DB things
		PersonaEntity personaInvited = personaDAO.findById(personaId);
		PersonaEntity personaSender = personaDAO.findById(friendPersonaId);
		if (personaInvited == null || personaSender == null) {
			return null;
		}

		if (resolution == 0) {
			removeFriend(personaInvited.getPersonaId(), personaSender.getPersonaId());
			return null;
		}

		FriendListEntity friendListEntity = friendListDAO.findByOwnerIdAndFriendPersona(personaInvited.getUser().getId(), personaSender.getPersonaId());
		if (friendListEntity == null) {
			return null;
		}

		friendListEntity.setIsAccepted(true);
		friendListDAO.update(friendListEntity);

		// Insert db record for sender player
		friendListEntity = friendListDAO.findByOwnerIdAndFriendPersona(personaSender.getUser().getId(), personaInvited.getPersonaId());
		if (friendListEntity == null) {
			FriendListEntity friendListInsert = new FriendListEntity();
			friendListInsert.setUserOwnerId(personaSender.getUser().getId());
			friendListInsert.setUserId(personaInvited.getUser().getId());
			friendListInsert.setPersonaId(personaInvited.getPersonaId());
			friendListInsert.setIsAccepted(true);
			friendListDAO.insert(friendListInsert);
		}

		// Send all info to personaSender
		FriendPersona friendPersona = new FriendPersona();
		friendPersona.setIconIndex(personaInvited.getIconIndex());
		friendPersona.setLevel(personaInvited.getLevel());
		friendPersona.setName(personaInvited.getName());
		friendPersona.setOriginalName(personaInvited.getName());
		friendPersona.setPersonaId(personaInvited.getPersonaId());
		friendPersona.setPresence(3);
		friendPersona.setUserId(personaInvited.getUser().getId());

		XMPP_FriendResultType friendResultType = new XMPP_FriendResultType();
		friendResultType.setFriendPersona(friendPersona);
		friendResultType.setResult(resolution);

		XMPP_ResponseTypeFriendResult responseTypeFriendResult = new XMPP_ResponseTypeFriendResult();
		responseTypeFriendResult.setFriendResult(friendResultType);

		XmppFriend xmppFriend = new XmppFriend(personaSender.getPersonaId(), openFireSoapBoxCli);
		xmppFriend.sendResponseFriendRequest(responseTypeFriendResult);

		PersonaBase personaBase = new PersonaBase();
		personaBase.setBadges(new ArrayOfBadgePacket());
		personaBase.setIconIndex(personaSender.getIconIndex());
		personaBase.setLevel(personaSender.getLevel());
		personaBase.setMotto(personaSender.getMotto());
		personaBase.setName(personaSender.getName());
		personaBase.setPersonaId(personaSender.getPersonaId());
		personaBase.setPresence(0);
		personaBase.setScore(personaSender.getScore());
		personaBase.setUserId(personaSender.getUser().getId());
		return personaBase;
	}

	public void removeFriend(Long personaId, Long friendPersonaId) {
		PersonaEntity personaInvited = personaDAO.findById(personaId);
		PersonaEntity personaSender = personaDAO.findById(friendPersonaId);
		if (personaInvited == null || personaSender == null) {
			return;
		}

		FriendListEntity friendListEntity = friendListDAO.findByOwnerIdAndFriendPersona(personaInvited.getUser().getId(), personaSender.getPersonaId());
		if (friendListEntity != null) {
			friendListDAO.delete(friendListEntity);
		}

		friendListEntity = friendListDAO.findByOwnerIdAndFriendPersona(personaSender.getUser().getId(), personaInvited.getPersonaId());
		if (friendListEntity != null) {
			friendListDAO.delete(friendListEntity);
		}
	}

	public void sendXmppPresence(PersonaEntity personaEntity, int presence, Long to) {
		XMPP_ResponseTypePersonaBase personaPacket = new XMPP_ResponseTypePersonaBase();
		PersonaBase xmppPersonaBase = new PersonaBase();
		xmppPersonaBase.setBadges(new ArrayOfBadgePacket());
		xmppPersonaBase.setIconIndex(personaEntity.getIconIndex());
		xmppPersonaBase.setLevel(personaEntity.getLevel());
		xmppPersonaBase.setMotto(personaEntity.getMotto());
		xmppPersonaBase.setName(personaEntity.getName());
		xmppPersonaBase.setPersonaId(personaEntity.getPersonaId());
		xmppPersonaBase.setPresence(presence);
		xmppPersonaBase.setScore(personaEntity.getScore());
		xmppPersonaBase.setUserId(personaEntity.getUser().getId());
		personaPacket.setPersonaBase(xmppPersonaBase);
		openFireSoapBoxCli.send(personaPacket, to);
	}

	public void sendXmppPresenceToAllFriends(PersonaEntity personaEntity, int presence) {
//		if (!openFireRestApiCli.isRestApiEnabled()) {
//			return;
//		}
		List<FriendListEntity> friends = friendListDAO.findByOwnerId(personaEntity.getUser().getId());
		if (friends != null) {
			for (FriendListEntity friend : friends) {
				sendXmppPresence(personaEntity, presence, friend.getPersonaId());
			}
		}
	}

//	@Schedule(minute = "*", hour = "*", persistent = false)
	public void updateOfflinePresence() {
		List<TokenSessionEntity> findByActive = tokenSessionDAO.findByActive();
		for (TokenSessionEntity tokenSessionEntity : findByActive) {
			Long activePersonaId = tokenSessionEntity.getActivePersonaId();
			if (!openFireRestApiCli.isOnline(activePersonaId)) {
				PersonaEntity personaEntity = personaBO.getPersonaById(activePersonaId);
				sendXmppPresenceToAllFriends(personaEntity, 0);
				tokenSessionDAO.updatePersonaPresence(activePersonaId, 0);
			}
		}
	}
}
