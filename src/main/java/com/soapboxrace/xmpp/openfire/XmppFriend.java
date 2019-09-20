package com.soapboxrace.xmpp.openfire;

import com.soapboxrace.core.xmpp.OpenFireSoapBoxCli;
import com.soapboxrace.jaxb.xmpp.XMPP_FriendPersonaType;
import com.soapboxrace.jaxb.xmpp.XMPP_ResponseTypeFriendResult;

public class XmppFriend {

	private long personaId;
	private OpenFireSoapBoxCli openFireSoapBoxCli;

	public XmppFriend(long personaId, OpenFireSoapBoxCli openFireSoapBoxCli) {
		this.personaId = personaId;
		this.openFireSoapBoxCli = openFireSoapBoxCli;
	}

	public void sendFriendRequest(XMPP_FriendPersonaType friendPersonaType) {
		openFireSoapBoxCli.send(friendPersonaType, personaId);
	}

	public void sendResponseFriendRequest(XMPP_ResponseTypeFriendResult responseTypeFriendResult) {
		openFireSoapBoxCli.send(responseTypeFriendResult, personaId);
	}

}
