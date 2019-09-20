package com.soapboxrace.core.jpa;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.soapboxrace.jaxb.http.AchievementState;

@Entity
@Table(name = "ACHIEVEMENT_STATE")
@NamedQueries({ //
		@NamedQuery(name = "AchievementStateEntity.findByPersonaAchievement", //
				query = "SELECT obj FROM AchievementStateEntity obj where obj.persona = :persona and obj.achievementRank = :achievementRank"),
		@NamedQuery(name = "AchievementStateEntity.findByPersonaBadge", //
				query = "SELECT obj FROM AchievementStateEntity obj where obj.persona = :persona and obj.achievementRank.achievementDefinition.badgeDefinition.id = :badgeDefinitionId"),
		@NamedQuery(name = "AchievementStateEntity.deleteByPersona", query = "DELETE FROM AchievementStateEntity obj WHERE obj.persona.id = :personaId") //
})
public class AchievementStateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@OneToOne
	@JoinColumn(name = "PersonaId", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_ACHVSTATE_PERSONA"))
	private PersonaEntity persona;

	@OneToOne
	@JoinColumn(name = "achievementRank", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_ACHVSTATE_RANK"))
	private AchievementRankEntity achievementRank;

	@Enumerated(EnumType.STRING)
	private AchievementState achievementState;

	private LocalDateTime achievedOn;

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

	public AchievementRankEntity getAchievementRank() {
		return achievementRank;
	}

	public void setAchievementRank(AchievementRankEntity achievementRank) {
		this.achievementRank = achievementRank;
	}

	public AchievementState getAchievementState() {
		return achievementState;
	}

	public void setAchievementState(AchievementState achievementState) {
		this.achievementState = achievementState;
	}

	public LocalDateTime getAchievedOn() {
		return achievedOn;
	}

	public void setAchievedOn(LocalDateTime achievedOn) {
		this.achievedOn = achievedOn;
	}

}
