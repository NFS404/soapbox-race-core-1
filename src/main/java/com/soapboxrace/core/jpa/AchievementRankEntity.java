package com.soapboxrace.core.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ACHIEVEMENT_RANK")
public class AchievementRankEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "achievedOn")
	private Date achievedOn;

	@Column(name = "isRare")
	private boolean isRare;

	@Column(name = "points")
	private short points;

	@Column(name = "rank")
	private short rank;

	@Column(name = "rarity")
	private float rarity = 0.000f;

	@Column(name = "rewardDescription")
	private String rewardDescription;

	@Column(name = "rewardType")
	private String rewardType;

	@Column(name = "rewardVisualStyle")
	private String rewardVisualStyle;

	@Column(name = "state")
	private String state;

	@Column(name = "thresholdValue")
	private Long thresholdValue;

	private String rewardText;

	private short numberOfRewards = 1;

	@ManyToOne
	@JoinColumn(name = "achievementId", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_ACHRANK_ACHDEF"))
	private AchievementDefinitionEntity achievementDefinition;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAchievedOn() {
		return achievedOn;
	}

	public void setAchievedOn(Date achievedOn) {
		this.achievedOn = achievedOn;
	}

	public boolean isRare() {
		return isRare;
	}

	public void setRare(boolean isRare) {
		this.isRare = isRare;
	}

	public short getPoints() {
		return points;
	}

	public void setPoints(short points) {
		this.points = points;
	}

	public short getRank() {
		return rank;
	}

	public void setRank(short rank) {
		this.rank = rank;
	}

	public float getRarity() {
		return rarity;
	}

	public void setRarity(float rarity) {
		this.rarity = rarity;
	}

	public String getRewardDescription() {
		return rewardDescription;
	}

	public void setRewardDescription(String rewardDescription) {
		this.rewardDescription = rewardDescription;
	}

	public String getRewardType() {
		return rewardType;
	}

	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}

	public String getRewardVisualStyle() {
		return rewardVisualStyle;
	}

	public void setRewardVisualStyle(String rewardVisualStyle) {
		this.rewardVisualStyle = rewardVisualStyle;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getThresholdValue() {
		return thresholdValue;
	}

	public void setThresholdValue(Long thresholdValue) {
		this.thresholdValue = thresholdValue;
	}

	public AchievementDefinitionEntity getAchievementDefinition() {
		return achievementDefinition;
	}

	public void setAchievementDefinition(AchievementDefinitionEntity achievementDefinition) {
		this.achievementDefinition = achievementDefinition;
	}

	public String getRewardText() {
		return rewardText;
	}

	public void setRewardText(String rewardText) {
		this.rewardText = rewardText;
	}

	public short getNumberOfRewards() {
		return numberOfRewards;
	}

	public void setNumberOfRewards(short numberOfRewards) {
		this.numberOfRewards = numberOfRewards;
	}

}
