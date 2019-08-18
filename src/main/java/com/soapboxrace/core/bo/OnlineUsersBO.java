package com.soapboxrace.core.bo;

import java.util.List;

import javax.ejb.AccessTimeout;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.LockType;

import com.soapboxrace.core.bo.ParameterBO;
import com.soapboxrace.core.xmpp.OpenFireRestApiCli;
import com.soapboxrace.core.xmpp.OpenFireSoapBoxCli;
import com.soapboxrace.core.xmpp.XmppChat;

import org.igniterealtime.restclient.entity.SessionEntity;

@Singleton
public class OnlineUsersBO {

	@EJB
	OpenFireRestApiCli openFireRestApiCli;
	
	@EJB
	private OpenFireSoapBoxCli openFireSoapBoxCli;
	
	private int onlineUsers;
	
	@EJB
	private ParameterBO parameterBO;
	
	public int getNumberOfUsersOnlineNow() {
		return onlineUsers;
	}
    
	@Schedule(minute = "*", hour = "*", persistent = false)
	@Lock(LockType.READ)
	public void updateOnlineUsers() {
		onlineUsers = openFireRestApiCli.getTotalOnlineUsers();
	}
	
	// OnlineCount Chat Announcer - Hypercycle
	@Schedule(minute = "*/10", hour = "*", persistent = false)
	@AccessTimeout(value=20000)
	public void OnlineCountForChat() {
		if (parameterBO.getBoolParam("CHAT_ONLINECOUNT")) {
		  List<SessionEntity> allSessions = openFireRestApiCli.getAllSessions();
		  String message = XmppChat.createSystemMessage("### Players Online: " + Integer.toString(openFireRestApiCli.getTotalOnlineUsers()));
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