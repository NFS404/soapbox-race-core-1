package com.soapboxrace.core.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.soapboxrace.core.dao.util.BaseDAO;
import com.soapboxrace.core.jpa.BadgePersonaEntity;
import com.soapboxrace.core.jpa.PersonaEntity;

@Stateless
public class BadgePersonaDAO extends BaseDAO<BadgePersonaEntity> {
	@PersistenceContext
	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public BadgePersonaEntity findById(Long id) {
		return entityManager.find(BadgePersonaEntity.class, id);
	}

	public List<BadgePersonaEntity> getAll() {
		return entityManager.createQuery("SELECT obj FROM BadgePersonaEntity obj", BadgePersonaEntity.class).getResultList();
	}

	public void deleteByPersona(PersonaEntity persona) {
		Query query = entityManager.createNamedQuery("BadgePersonaEntity.deleteByPersona");
		query.setParameter("persona", persona);
		query.executeUpdate();
	}
}
