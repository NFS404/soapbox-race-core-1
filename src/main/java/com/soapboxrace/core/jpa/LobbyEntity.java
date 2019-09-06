package com.soapboxrace.core.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "LOBBY")
@NamedQueries({ //
		@NamedQuery(name = "LobbyEntity.findAll", query = "SELECT obj FROM UserEntity obj"), //
		@NamedQuery(name = "LobbyEntity.findAllOpen", //
				query = "SELECT obj FROM LobbyEntity obj WHERE obj.lobbyDateTimeStart between :dateTime1 and :dateTime2 "), //
		@NamedQuery(name = "LobbyEntity.findAllOpenByCarClass", //
				query = "SELECT obj FROM LobbyEntity obj " //
						+ "WHERE obj.lobbyDateTimeStart between :dateTime1 and :dateTime2 " //
						+ "and (obj.event.carClassHash = 607077938 or obj.event.carClassHash = :carClassHash ) AND obj.isPrivate = false "
						+ "AND obj.event.searchAvailable = true "),
		
		@NamedQuery(name = "LobbyEntity.findAllMPLobbiesClasses", // All Races (Classes)
		query = "SELECT obj FROM LobbyEntity obj " //
				+ "WHERE obj.lobbyDateTimeStart between :dateTime1 and :dateTime2 " //
				+ "AND obj.isPrivate = false "
				+ "AND obj.event.searchAvailable = true AND obj.event.carClassHash = :carClassHash "),
		@NamedQuery(name = "LobbyEntity.findAllMPLobbiesOpen", // All Races (Open)
		query = "SELECT obj FROM LobbyEntity obj " //
				+ "WHERE obj.lobbyDateTimeStart between :dateTime1 and :dateTime2 " //
				+ "and (obj.carDivision = 'bas' or obj.carDivision = :carDivision) AND obj.isPrivate = false "
				+ "AND obj.event.searchAvailable = true AND obj.event.carClassHash = 607077938 "),
		
		@NamedQuery(name = "LobbyEntity.findMPLobbiesP2PClasses", // Circuits and Sprints (Classes)
		query = "SELECT obj FROM LobbyEntity obj " //
				+ "WHERE obj.lobbyDateTimeStart between :dateTime1 and :dateTime2 " //
				+ "AND obj.isPrivate = false "
				+ "AND obj.event.searchAvailable = true AND obj.event.carClassHash = :carClassHash AND (obj.event.eventModeId = 4 or obj.event.eventModeId = 9)"),
		@NamedQuery(name = "LobbyEntity.findMPLobbiesP2POpen", // Circuits and Sprints (Open)
		query = "SELECT obj FROM LobbyEntity obj " //
				+ "WHERE obj.lobbyDateTimeStart between :dateTime1 and :dateTime2 " //
				+ "and (obj.carDivision = 'bas' or obj.carDivision = :carDivision) AND obj.isPrivate = false "
				+ "AND obj.event.searchAvailable = true AND obj.event.carClassHash = 607077938 AND (obj.event.eventModeId = 4 or obj.event.eventModeId = 9)"),
		
		@NamedQuery(name = "LobbyEntity.findMPLobbiesDragClasses", // Drags (Classes)
		query = "SELECT obj FROM LobbyEntity obj " //
				+ "WHERE obj.lobbyDateTimeStart between :dateTime1 and :dateTime2 " //
				+ "AND obj.isPrivate = false "
				+ "AND obj.event.searchAvailable = true AND obj.event.carClassHash = :carClassHash AND obj.event.eventModeId = 19"),
		@NamedQuery(name = "LobbyEntity.findMPLobbiesDragOpen", // Drags (Open)
		query = "SELECT obj FROM LobbyEntity obj " //
				+ "WHERE obj.lobbyDateTimeStart between :dateTime1 and :dateTime2 " //
				+ "and (obj.carDivision = 'bas' or obj.carDivision = :carDivision) AND obj.isPrivate = false "
				+ "AND obj.event.searchAvailable = true AND obj.event.carClassHash = 607077938 AND obj.event.eventModeId = 19"),
		
		@NamedQuery(name = "LobbyEntity.findMPLobbiesRaceClasses", // Circuits, Sprints, Drags (Classes)
		query = "SELECT obj FROM LobbyEntity obj " //
				+ "WHERE obj.lobbyDateTimeStart between :dateTime1 and :dateTime2 " //
				+ "AND obj.isPrivate = false "
				+ "AND obj.event.searchAvailable = true AND obj.event.carClassHash = :carClassHash AND (obj.event.eventModeId = 4 or obj.event.eventModeId = 9 or obj.event.eventModeId = 19)"),
		@NamedQuery(name = "LobbyEntity.findMPLobbiesRaceOpen", // Circuits, Sprints, Drags (Open)
		query = "SELECT obj FROM LobbyEntity obj " //
				+ "WHERE obj.lobbyDateTimeStart between :dateTime1 and :dateTime2 " //
				+ "and (obj.carDivision = 'bas' or obj.carDivision = :carDivision) AND obj.isPrivate = false "
				+ "AND obj.event.searchAvailable = true AND obj.event.carClassHash = 607077938 AND (obj.event.eventModeId = 4 or obj.event.eventModeId = 9 or obj.event.eventModeId = 19)"),
		
		@NamedQuery(name = "LobbyEntity.findMPLobbiesPursuitClasses", // Team Escapes (Classes)
		query = "SELECT obj FROM LobbyEntity obj " //
				+ "WHERE obj.lobbyDateTimeStart between :dateTime1 and :dateTime2 " //
				+ "AND obj.isPrivate = false "
				+ "AND obj.event.searchAvailable = true AND obj.event.carClassHash = :carClassHash AND obj.event.eventModeId = 24"),
		@NamedQuery(name = "LobbyEntity.findMPLobbiesPursuitOpen", // Team Escapes (Open)
		query = "SELECT obj FROM LobbyEntity obj " //
				+ "WHERE obj.lobbyDateTimeStart between :dateTime1 and :dateTime2 " //
				+ "and (obj.carDivision = 'bas' or obj.carDivision = :carDivision) AND obj.isPrivate = false "
				+ "AND obj.event.searchAvailable = true AND obj.event.carClassHash = 607077938 AND obj.event.eventModeId = 24"),
		
		@NamedQuery(name = "LobbyEntity.findByEventStarted", query = "SELECT obj FROM LobbyEntity obj WHERE obj.event = :event AND obj.lobbyDateTimeStart between :dateTime1 AND :dateTime2 AND obj.isPrivate = false"), //
		@NamedQuery(name = "LobbyEntity.findByEventAndPersona", query = "SELECT obj FROM LobbyEntity obj WHERE obj.event = :event AND obj.lobbyDateTimeStart between :dateTime1 AND :dateTime2 AND obj.isPrivate = true AND obj.personaId = :personaId") //
})
public class LobbyEntity {

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "EVENTID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_LOBBY_EVENT"))
	private EventEntity event;

	@OneToMany(mappedBy = "lobby", targetEntity = LobbyEntrantEntity.class, cascade = CascadeType.MERGE)
	private List<LobbyEntrantEntity> entrants;

	private Date lobbyDateTimeStart = new Date();

	private Boolean isPrivate;

	private Long personaId;
	
	private String carDivision;

	@Transient
	private Long lobbyCountdownInMilliseconds = 60000L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EventEntity getEvent() {
		return event;
	}

	public void setEvent(EventEntity event) {
		this.event = event;
	}

	public List<LobbyEntrantEntity> getEntrants() {
		return entrants;
	}

	public void setEntrants(List<LobbyEntrantEntity> entrants) {
		this.entrants = entrants;
	}

	public Date getLobbyDateTimeStart() {
		return lobbyDateTimeStart;
	}

	public void setLobbyDateTimeStart(Date lobbyDateTimeStart) {
		this.lobbyDateTimeStart = lobbyDateTimeStart;
	}

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	public String getCarDivision() {
		return carDivision;
	}
	
	public void setCarDivision(String carDivision) {
		this.carDivision = carDivision;
	}

	public Long getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Long personaId) {
		this.personaId = personaId;
	}

	public boolean add(LobbyEntrantEntity e) {
		if (entrants == null) {
			entrants = new ArrayList<>();
		}
		return entrants.add(e);
	}

	public int getLobbyCountdownInMilliseconds() {
		if (lobbyDateTimeStart != null) {
			Date now = new Date();
			Long time = now.getTime() - lobbyDateTimeStart.getTime();
			time = 60000L - time;
			return time.intValue();
		}
		return lobbyCountdownInMilliseconds.intValue();
	}

}
