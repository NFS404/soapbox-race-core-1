package com.soapboxrace.core.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.soapboxrace.core.dao.util.BaseDAO;
import com.soapboxrace.core.jpa.AchievementRankEntity;
import com.soapboxrace.core.jpa.PersonaEntity;

@Stateless
public class AchievementRankDAO extends BaseDAO<AchievementRankEntity> {
	@PersistenceContext
	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public AchievementRankEntity findById(Long id) {
		return entityManager.find(AchievementRankEntity.class, id);
	}

	public AchievementRankEntity findByAchievementDefinitionIdThresholdValue(Long achievementDefinitionId, Long thresholdValue) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT obj FROM AchievementRankEntity obj ");
		sql.append("WHERE obj.achievementDefinition.id = :achievementDefinitionId ");
		sql.append("and obj.thresholdValue = :thresholdValue");
		TypedQuery<AchievementRankEntity> query = entityManager.createQuery(sql.toString(), AchievementRankEntity.class);
		query.setParameter("achievementDefinitionId", achievementDefinitionId);
		query.setParameter("thresholdValue", thresholdValue);
		List<AchievementRankEntity> results = query.getResultList();
		return results.isEmpty() ? null : results.get(0);
	}

	public List<AchievementRankEntity> findByAchievementDefinitionId(Long achievementDefinitionId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT obj FROM AchievementRankEntity obj ");
		sql.append("WHERE obj.achievementDefinition.id = :achievementDefinitionId ");
		TypedQuery<AchievementRankEntity> query = entityManager.createQuery(sql.toString(), AchievementRankEntity.class);
		query.setParameter("achievementDefinitionId", achievementDefinitionId);
		return query.getResultList();
	}

	public AchievementRankEntity findByAchievementDefinitionIdThresholdPersona(Long achievementDefinitionId, Long thresholdValue, PersonaEntity personaEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT obj FROM AchievementRankEntity obj ");
		sql.append("WHERE obj.achievementDefinition.id = :achievementDefinitionId ");
		sql.append("and obj.thresholdValue <= :thresholdValue ");
		sql.append("and not exists ");
		sql.append("(select 1 from AchievementStateEntity achstate ");
		sql.append("where achstate.persona = :persona and achstate.achievementRank = obj) ");
		TypedQuery<AchievementRankEntity> query = entityManager.createQuery(sql.toString(), AchievementRankEntity.class);
		query.setParameter("achievementDefinitionId", achievementDefinitionId);
		query.setParameter("thresholdValue", thresholdValue);
		query.setParameter("persona", personaEntity);
		List<AchievementRankEntity> results = query.getResultList();
		return results.isEmpty() ? null : results.get(0);
	}

	public AchievementRankEntity findByRewardDescription(String rewardDescription) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT obj FROM AchievementRankEntity obj ");
		sql.append("WHERE obj.rewardDescription = :rewardDescription ");
		TypedQuery<AchievementRankEntity> query = entityManager.createQuery(sql.toString(), AchievementRankEntity.class);
		query.setParameter("rewardDescription", rewardDescription);
		List<AchievementRankEntity> results = query.getResultList();
		return results.isEmpty() ? null : results.get(0);
	}
}
