package com.soapboxrace.core.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.soapboxrace.core.dao.util.BaseDAO;
import com.soapboxrace.core.jpa.AchievementPersonaEntity;
import com.soapboxrace.core.jpa.PersonaEntity;

@Stateless
public class AchievementPersonaDAO extends BaseDAO<AchievementPersonaEntity> {
	@PersistenceContext
	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public AchievementPersonaEntity findByPersona(PersonaEntity personaEntity) {
		TypedQuery<AchievementPersonaEntity> query = entityManager.createNamedQuery("AchievementPersonaEntity.findByPersona", AchievementPersonaEntity.class);
		query.setParameter("persona", personaEntity);
		List<AchievementPersonaEntity> resultList = query.getResultList();
		if (resultList == null || resultList.isEmpty()) {
			AchievementPersonaEntity achievementPersonaEntity = new AchievementPersonaEntity();
			achievementPersonaEntity.setPersona(personaEntity);
			insert(achievementPersonaEntity);
			return achievementPersonaEntity;
		}
		return resultList.get(0);
	}

	public void deleteByPersona(Long personaId) {
		Query query = entityManager.createNamedQuery("AchievementPersonaEntity.deleteByPersona");
		query.setParameter("personaId", personaId);
		query.executeUpdate();
	}

}
