
package com.soapboxrace.core.jpa;

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
import javax.persistence.Table;

@Entity
@Table(name = "BADGE_PERSONA")
@NamedQueries({ @NamedQuery(name = "BadgePersonaEntity.findByPersonaId", //
		query = "SELECT obj FROM BadgePersonaEntity obj WHERE obj.persona = :persona"), //
		@NamedQuery(name = "BadgePersonaEntity.deleteByPersona", //
				query = "DELETE FROM BadgePersonaEntity obj WHERE obj.persona = :persona") //
})
public class BadgePersonaEntity {

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "personaId", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_BADGEPER_PERSONA"))
	private PersonaEntity persona;

	@ManyToOne
	@JoinColumn(name = "achievementRank", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_BADGEPER_ACHRANK"))
	private AchievementRankEntity achievementRank;

	private short slot;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public short getSlot() {
		return slot;
	}

	public void setSlot(short slot) {
		this.slot = slot;
	}

}
