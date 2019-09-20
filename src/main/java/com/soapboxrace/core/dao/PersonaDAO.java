package com.soapboxrace.core.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.soapboxrace.core.dao.util.BaseDAO;
import com.soapboxrace.core.jpa.PersonaEntity;

@Stateless
public class PersonaDAO extends BaseDAO<PersonaEntity> {

	@PersistenceContext
	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public PersonaEntity findById(Long id) {
		return entityManager.find(PersonaEntity.class, id);
	}

	public PersonaEntity findByName(String name) {
		name = name.toUpperCase();
		TypedQuery<PersonaEntity> query = entityManager.createNamedQuery("PersonaEntity.findByName", PersonaEntity.class);
		query.setParameter("name", name);

		List<PersonaEntity> resultList = query.getResultList();
		return !resultList.isEmpty() ? resultList.get(0) : null;
	}

	public List<PersonaEntity> getAllPaged(int offset, int max) {
		TypedQuery<PersonaEntity> query = entityManager.createQuery("SELECT obj FROM PersonaEntity obj ", PersonaEntity.class);
		query.setMaxResults(max);
		query.setFirstResult(offset);
		return query.getResultList();
	}

}
