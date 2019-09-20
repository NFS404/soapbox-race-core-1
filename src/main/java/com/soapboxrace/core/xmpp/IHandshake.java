package com.soapboxrace.core.xmpp;

public interface IHandshake {
	IOpenFireTalk getXmppTalk();

	void init();
}