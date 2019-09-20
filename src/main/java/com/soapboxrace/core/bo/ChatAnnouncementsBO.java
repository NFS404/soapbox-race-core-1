package com.soapboxrace.core.bo;

import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.AccessTimeout;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import org.igniterealtime.restclient.entity.SessionEntity;

import com.soapboxrace.core.dao.ChatAnnouncementDAO;
import com.soapboxrace.core.jpa.ChatAnnouncementEntity;
import com.soapboxrace.core.xmpp.OpenFireRestApiCli;
import com.soapboxrace.core.xmpp.OpenFireSoapBoxCli;
import com.soapboxrace.core.xmpp.XmppChat;

@Stateless
public class ChatAnnouncementsBO {
	@EJB
	private ChatAnnouncementDAO chatAnnouncementDAO;

	@EJB
	private OpenFireRestApiCli restApiCli;

	@EJB
	private OpenFireSoapBoxCli openFireSoapBoxCli;

	@AccessTimeout(value=20000)
	@Schedule(minute = "*", hour = "*", persistent = false)
	public void sendMessages() {
		LocalDateTime now = LocalDateTime.now();
		int minute = now.getMinute();
		for (ChatAnnouncementEntity announcementEntity : chatAnnouncementDAO.findAll()) {
			int announceMinute = announcementEntity.getMinute();
			if (minute % announceMinute == 0) {
				List<SessionEntity> allSessions = restApiCli.getAllSessions();
				String message = XmppChat.createSystemMessage(announcementEntity.getMessage());
				for (SessionEntity sessionEntity : allSessions) {
					String username = sessionEntity.getUsername();
					if (!username.contains("engine")) {
						Long member = Long.valueOf(username.replace("sbrw.", ""));
						openFireSoapBoxCli.send(message, member);
					}
				}
			}
		}
	}
}
