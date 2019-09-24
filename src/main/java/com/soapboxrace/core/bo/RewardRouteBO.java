package com.soapboxrace.core.bo;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.soapboxrace.core.bo.util.RewardVO;
import com.soapboxrace.core.dao.PersonaDAO;
import com.soapboxrace.core.jpa.EventEntity;
import com.soapboxrace.core.jpa.EventSessionEntity;
import com.soapboxrace.core.jpa.PersonaEntity;
import com.soapboxrace.core.jpa.SkillModRewardType;
import com.soapboxrace.jaxb.http.Accolades;
import com.soapboxrace.jaxb.http.RouteArbitrationPacket;
import com.soapboxrace.jaxb.http.ArrayOfRouteEntrantResult;

@Stateless
public class RewardRouteBO extends RewardBO {

	@EJB
	private PersonaDAO personaDao;

	@EJB
	private LegitRaceBO legitRaceBO;

	public Accolades getRouteAccolades(Long activePersonaId, RouteArbitrationPacket routeArbitrationPacket, EventSessionEntity eventSessionEntity, ArrayOfRouteEntrantResult arrayOfRouteEntrantResult) {
		if (!legitRaceBO.isLegit(activePersonaId, routeArbitrationPacket, eventSessionEntity)) {
			return new Accolades();
		}
		EventEntity eventEntity = eventSessionEntity.getEvent();
		PersonaEntity personaEntity = personaDao.findById(activePersonaId);
		RewardVO rewardVO = getRewardVO(personaEntity);

		setBaseRewardRace(personaEntity, eventEntity, routeArbitrationPacket, rewardVO, arrayOfRouteEntrantResult);
		setRankReward(eventEntity, routeArbitrationPacket, rewardVO);
		setPerfectStartReward(eventEntity, routeArbitrationPacket.getPerfectStart(), rewardVO);
		setTopSpeedReward(eventEntity, routeArbitrationPacket.getTopSpeed(), rewardVO);
		setSkillMultiplierReward(personaEntity, rewardVO, SkillModRewardType.SOCIALITE);
		setMultiplierReward(eventEntity, rewardVO);

		applyRaceReward(rewardVO.getRep(), rewardVO.getCash(), personaEntity);
		return getAccolades(personaEntity, routeArbitrationPacket, rewardVO);
	}

}
