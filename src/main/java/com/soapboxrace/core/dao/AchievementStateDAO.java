package com.soapboxrace.core.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.soapboxrace.core.dao.util.BaseDAO;
import com.soapboxrace.core.jpa.AchievementRankEntity;
import com.soapboxrace.core.jpa.AchievementStateEntity;
import com.soapboxrace.core.jpa.PersonaEntity;

@Stateless
public class AchievementStateDAO extends BaseDAO<AchievementStateEntity> {
	@PersistenceContext
	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public AchievementStateEntity findById(Long id) {
		return entityManager.find(AchievementStateEntity.class, id);
	}

	public AchievementStateEntity findByPersonaAchievementRank(PersonaEntity personaEntity, AchievementRankEntity achievementRank) {
		TypedQuery<AchievementStateEntity> query = entityManager.createNamedQuery("AchievementStateEntity.findByPersonaAchievement",
				AchievementStateEntity.class);
		query.setParameter("persona", personaEntity);
		query.setParameter("achievementRank", achievementRank);
		List<AchievementStateEntity> resultList = query.getResultList();
		if (resultList == null || resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}

	public AchievementStateEntity findByPersonaBadge(PersonaEntity personaEntity, Long badgeDefinitionId) {
		TypedQuery<AchievementStateEntity> query = entityManager.createNamedQuery("AchievementStateEntity.findByPersonaBadge", AchievementStateEntity.class);
		query.setParameter("persona", personaEntity);
		query.setParameter("badgeDefinitionId", badgeDefinitionId);
		List<AchievementStateEntity> resultList = query.getResultList();
		if (resultList == null || resultList.isEmpty()) {
			return null;
		}
		return resultList.get(resultList.size() - 1);
	}

	public void deleteByPersona(Long personaId) {
		Query query = entityManager.createNamedQuery("AchievementStateEntity.deleteByPersona");
		query.setParameter("personaId", personaId);
		query.executeUpdate();
	}
}
