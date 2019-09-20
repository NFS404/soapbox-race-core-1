package com.soapboxrace.core.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ACHIEVEMENT_PERSONA")
@NamedQueries({ //
		@NamedQuery(name = "AchievementPersonaEntity.findByPersona", query = "SELECT obj FROM AchievementPersonaEntity obj where obj.persona = :persona"),
		@NamedQuery(name = "AchievementPersonaEntity.deleteByPersona", query = "DELETE FROM AchievementPersonaEntity obj WHERE obj.persona.id = :personaId") })
public class AchievementPersonaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@OneToOne
	@JoinColumn(name = "PersonaId", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_ACHV_PERSONA"))
	private PersonaEntity persona;

	private int commerceAfterMarket;
	private int commercePaints;
	private int commercePerfParts;
	private int commerceRaceSkills;
	private int commerceExplorerSkills;
	private int commercePursuitSkills;

	private long eventKms;
	private long eventAerialTime;

	private long pursuitCostToState;
	private int pursuitWins;

	private int mpDragWins;
	private int mpRaceWins;

	private int mpRaces;
	private int privateRaces;
	private int spRaces;

	private int teamScapePoliceDown;
	private int teamScapeWins;
	private int teamScapeBlocks;
	private int treasureHunts;
	private int dailyTreasureHunts;

	private int usedPowerups;

	private int totalIncomeCash;

	private int restrictedClassAWins;
	private int restrictedClassBWins;
	private int restrictedClassCWins;
	private int restrictedClassDWins;
	private int restrictedClassEWins;
	private int restrictedClassSWins;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PersonaEntity getPersona() {
		return persona;
	}

	public void setPersona(PersonaEntity persona) {
		this.persona = persona;
	}

	public int getCommerceAfterMarket() {
		return commerceAfterMarket;
	}

	public void setCommerceAfterMarket(int commerceAfterMarket) {
		this.commerceAfterMarket = commerceAfterMarket;
	}

	public int getCommercePaints() {
		return commercePaints;
	}

	public void setCommercePaints(int commercePaints) {
		this.commercePaints = commercePaints;
	}

	public int getCommercePerfParts() {
		return commercePerfParts;
	}

	public void setCommercePerfParts(int commercePerfParts) {
		this.commercePerfParts = commercePerfParts;
	}

	public int getCommerceRaceSkills() {
		return commerceRaceSkills;
	}

	public void setCommerceRaceSkills(int commerceRaceSkills) {
		this.commerceRaceSkills = commerceRaceSkills;
	}

	public int getCommerceExplorerSkills() {
		return commerceExplorerSkills;
	}

	public void setCommerceExplorerSkills(int commerceExplorerSkills) {
		this.commerceExplorerSkills = commerceExplorerSkills;
	}

	public int getCommercePursuitSkills() {
		return commercePursuitSkills;
	}

	public void setCommercePursuitSkills(int commercePursuitSkills) {
		this.commercePursuitSkills = commercePursuitSkills;
	}

	public long getEventKms() {
		return eventKms;
	}

	public void setEventKms(long eventKms) {
		this.eventKms = eventKms;
	}

	public long getEventAerialTime() {
		return eventAerialTime;
	}

	public void setEventAerialTime(long eventAerialTime) {
		this.eventAerialTime = eventAerialTime;
	}

	public long getPursuitCostToState() {
		return pursuitCostToState;
	}

	public void setPursuitCostToState(long pursuitCostToState) {
		this.pursuitCostToState = pursuitCostToState;
	}

	public int getPursuitWins() {
		return pursuitWins;
	}

	public void setPursuitWins(int pursuitWins) {
		this.pursuitWins = pursuitWins;
	}

	public int getMpDragWins() {
		return mpDragWins;
	}

	public void setMpDragWins(int mpDragWins) {
		this.mpDragWins = mpDragWins;
	}

	public int getMpRaceWins() {
		return mpRaceWins;
	}

	public void setMpRaceWins(int mpRaceWins) {
		this.mpRaceWins = mpRaceWins;
	}

	public int getPrivateRaces() {
		return privateRaces;
	}

	public void setPrivateRaces(int privateRaces) {
		this.privateRaces = privateRaces;
	}

	public int getTeamScapePoliceDown() {
		return teamScapePoliceDown;
	}

	public void setTeamScapePoliceDown(int teamScapePoliceDown) {
		this.teamScapePoliceDown = teamScapePoliceDown;
	}

	public int getTeamScapeWins() {
		return teamScapeWins;
	}

	public void setTeamScapeWins(int teamScapeWins) {
		this.teamScapeWins = teamScapeWins;
	}

	public int getTeamScapeBlocks() {
		return teamScapeBlocks;
	}

	public void setTeamScapeBlocks(int teamScapeBlocks) {
		this.teamScapeBlocks = teamScapeBlocks;
	}

	public int getTreasureHunts() {
		return treasureHunts;
	}

	public void setTreasureHunts(int treasureHunts) {
		this.treasureHunts = treasureHunts;
	}

	public int getUsedPowerups() {
		return usedPowerups;
	}

	public void setUsedPowerups(int usedPowerups) {
		this.usedPowerups = usedPowerups;
	}

	public int getTotalIncomeCash() {
		return totalIncomeCash;
	}

	public void setTotalIncomeCash(int totalIncomeCash) {
		this.totalIncomeCash = totalIncomeCash;
	}

	public int getDailyTreasureHunts() {
		return dailyTreasureHunts;
	}

	public void setDailyTreasureHunts(int dailyTreasureHunts) {
		this.dailyTreasureHunts = dailyTreasureHunts;
	}

	public int getRestrictedClassAWins() {
		return restrictedClassAWins;
	}

	public void setRestrictedClassAWins(int restrictedClassAWins) {
		this.restrictedClassAWins = restrictedClassAWins;
	}

	public int getRestrictedClassBWins() {
		return restrictedClassBWins;
	}

	public void setRestrictedClassBWins(int restrictedClassBWins) {
		this.restrictedClassBWins = restrictedClassBWins;
	}

	public int getRestrictedClassCWins() {
		return restrictedClassCWins;
	}

	public void setRestrictedClassCWins(int restrictedClassCWins) {
		this.restrictedClassCWins = restrictedClassCWins;
	}

	public int getRestrictedClassDWins() {
		return restrictedClassDWins;
	}

	public void setRestrictedClassDWins(int restrictedClassDWins) {
		this.restrictedClassDWins = restrictedClassDWins;
	}

	public int getRestrictedClassEWins() {
		return restrictedClassEWins;
	}

	public void setRestrictedClassEWins(int restrictedClassEWins) {
		this.restrictedClassEWins = restrictedClassEWins;
	}

	public int getRestrictedClassSWins() {
		return restrictedClassSWins;
	}

	public void setRestrictedClassSWins(int restrictedClassSWins) {
		this.restrictedClassSWins = restrictedClassSWins;
	}

	public int getMpRaces() {
		return mpRaces;
	}

	public void setMpRaces(int mpRaces) {
		this.mpRaces = mpRaces;
	}

	public int getSpRaces() {
		return spRaces;
	}

	public void setSpRaces(int spRaces) {
		this.spRaces = spRaces;
	}

}
