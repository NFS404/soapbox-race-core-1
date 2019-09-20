package com.soapboxrace.core.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.soapboxrace.core.dao.util.BaseDAO;
import com.soapboxrace.core.jpa.TokenSessionEntity;

@Stateless
public class TokenSessionDAO extends BaseDAO<TokenSessionEntity> {

	@PersistenceContext
	protected void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public TokenSessionEntity findBySecurityToken(String securityToken) {
		TypedQuery<TokenSessionEntity> query = entityManager.createNamedQuery("TokenSessionEntity.findBySecurityToken", TokenSessionEntity.class);
		query.setParameter("securityToken", securityToken);
		List<TokenSessionEntity> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}

	public TokenSessionEntity findByUserId(Long userId) {
		TypedQuery<TokenSessionEntity> query = entityManager.createNamedQuery("TokenSessionEntity.findByUserId", TokenSessionEntity.class);
		query.setParameter("userId", userId);
		List<TokenSessionEntity> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}

	public int getUsersOnlineCount() {
		return ((Number)entityManager.createNamedQuery("TokenSessionEntity.getUsersOnlineCount").getSingleResult()).intValue();
	}
	
	public void updateRelayCrytoTicketByPersonaId(Long personaId, String relayCryptoTicket) {
		Query query = entityManager.createNamedQuery("TokenSessionEntity.updateRelayCrytoTicket");
		query.setParameter("personaId", personaId);
		query.setParameter("relayCryptoTicket", relayCryptoTicket);
		query.executeUpdate();
	}

	public void updateLobbyIdByPersonaId(Long personaId, Long lobbyId) {
		Query query = entityManager.createNamedQuery("TokenSessionEntity.updateRelayCrytoTicket");
		query.setParameter("personaId", personaId);
		query.setParameter("activeLobbyId", lobbyId);
		query.executeUpdate();
	}

	public void updatePersonaPresence(Long personaId, Integer personaPresence) {
		Query query = entityManager.createNamedQuery("TokenSessionEntity.updatePersonaPresence");
		query.setParameter("personaId", personaId);
		query.setParameter("personaPresence", personaPresence);
		query.executeUpdate();
	}

	public List<TokenSessionEntity> findByActive() {
		StringBuilder queryStr = new StringBuilder();
		queryStr.append("SELECT obj FROM TokenSessionEntity obj ");
		queryStr.append(" WHERE obj.activePersonaId <> 0 ");
		queryStr.append(" AND obj.personaPresence <> 0 ");
		queryStr.append(" AND obj.expirationDate > current_time()");
		TypedQuery<TokenSessionEntity> query = entityManager.createQuery(queryStr.toString(), TokenSessionEntity.class);
		return query.getResultList();
	}

}
