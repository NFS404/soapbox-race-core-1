package com.soapboxrace.core.jpa;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.soapboxrace.core.bo.util.RewardDestinyType;

@Entity
@Table(name = "REWARD_DROP")
public class RewardDropEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "productId", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_REWARDDROP_PRODUCT"))
	private ProductEntity product;

	@OneToOne
	@JoinColumn(name = "achievementRankId", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_REWARDDROP_ACHRANK"))
	private AchievementRankEntity achievementRank;

	private Integer amount;

	@Enumerated(EnumType.STRING)
	private RewardDestinyType rewardDestiny;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

	public AchievementRankEntity getAchievementRank() {
		return achievementRank;
	}

	public void setAchievementRank(AchievementRankEntity achievementRank) {
		this.achievementRank = achievementRank;
	}

	public RewardDestinyType getRewardDestiny() {
		return rewardDestiny;
	}

	public void setRewardDestiny(RewardDestinyType rewardDestiny) {
		this.rewardDestiny = rewardDestiny;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}
