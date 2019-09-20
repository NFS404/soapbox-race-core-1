package com.soapboxrace.core.jpa;

import javax.persistence.*;

@Entity
@Table(name = "CHAT_ANNOUNCEMENT")
@NamedQueries({ //
		@NamedQuery(name = "ChatAnnouncementEntity.findAll", query = "SELECT obj FROM ChatAnnouncementEntity obj") //
})
public class ChatAnnouncementEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;

	@Column(name = "MINUTE")
	private Integer minute;

	@Column(name = "MESSAGE")
	private String message;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}