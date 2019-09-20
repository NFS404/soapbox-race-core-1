package com.soapboxrace.core.xmpp;

public class XmppChat {
	public static String createSystemMessage(String message) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<response status='1' ticket='0'>");
		stringBuilder.append("<ChatBroadcast>");
		stringBuilder.append("<ChatBlob>");
		stringBuilder.append("<FromName>System</FromName>");
		stringBuilder.append("<FromPersonaId>0</FromPersonaId>");
		stringBuilder.append("<FromUserId>0</FromUserId>");
		stringBuilder.append("<Message>%s</Message>");
		stringBuilder.append("<ToId>0</ToId>");
		stringBuilder.append("<Type>2</Type>");
		stringBuilder.append("</ChatBlob>");
		stringBuilder.append("</ChatBroadcast>");
		stringBuilder.append("</response>");
		return String.format(stringBuilder.toString(), message);
	}
}
