package com.soapboxrace.core.bo;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.soapboxrace.core.bo.util.OwnedCarConverter;
import com.soapboxrace.core.dao.AchievementStateDAO;
import com.soapboxrace.core.dao.BadgeDefinitionDAO;
import com.soapboxrace.core.dao.BadgePersonaDAO;
import com.soapboxrace.core.dao.CarSlotDAO;
import com.soapboxrace.core.dao.LevelRepDAO;
import com.soapboxrace.core.dao.OwnedCarDAO;
import com.soapboxrace.core.dao.PersonaDAO;
import com.soapboxrace.core.jpa.AchievementRankEntity;
import com.soapboxrace.core.jpa.AchievementStateEntity;
import com.soapboxrace.core.jpa.BadgePersonaEntity;
import com.soapboxrace.core.jpa.CarSlotEntity;
import com.soapboxrace.core.jpa.CustomCarEntity;
import com.soapboxrace.core.jpa.LevelRepEntity;
import com.soapboxrace.core.jpa.OwnedCarEntity;
import com.soapboxrace.core.jpa.PersonaEntity;
import com.soapboxrace.jaxb.http.BadgeBundle;
import com.soapboxrace.jaxb.http.BadgeInput;
import com.soapboxrace.jaxb.http.OwnedCarTrans;

@Stateless
public class PersonaBO {

	@EJB
	private PersonaDAO personaDAO;

	@EJB
	private CarSlotDAO carSlotDAO;

	@EJB
	private LevelRepDAO levelRepDAO;

	@EJB
	private OwnedCarDAO ownedCarDAO;

	@EJB
	private BadgeDefinitionDAO badgeDefinitionDAO;

	@EJB
	private BadgePersonaDAO badgePersonaDAO;

	@EJB
	private AchievementStateDAO achievementStateDAO;

	public void changeDefaultCar(Long personaId, Long defaultCarId) {
		PersonaEntity personaEntity = personaDAO.findById(personaId);
		List<CarSlotEntity> carSlotList = carSlotDAO.findByPersonaId(personaId);
		int i = 0;
		for (CarSlotEntity carSlotEntity : carSlotList) {
			if (carSlotEntity.getOwnedCar().getId().equals(defaultCarId)) {
				break;
			}
			i++;
		}
		personaEntity.setCurCarIndex(i);
		personaDAO.update(personaEntity);
	}

	public PersonaEntity getPersonaById(Long personaId) {
		return personaDAO.findById(personaId);
	}

	public CarSlotEntity getDefaultCarEntity(Long personaId) {
		PersonaEntity personaEntity = personaDAO.findById(personaId);
		// if (personaEntity == null) {
		// personaEntity = personaDAO.findById(100l);
		// }
		List<CarSlotEntity> carSlotList = getPersonasCar(personaId);
		Integer curCarIndex = personaEntity.getCurCarIndex();
		if (!carSlotList.isEmpty()) {
			if (curCarIndex >= carSlotList.size()) {
				curCarIndex = carSlotList.size() - 1;
				CarSlotEntity ownedCarEntity = carSlotList.get(curCarIndex);
				changeDefaultCar(personaId, ownedCarEntity.getId());
			}
			CarSlotEntity carSlotEntity = carSlotList.get(curCarIndex);
			CustomCarEntity customCar = carSlotEntity.getOwnedCar().getCustomCar();
			customCar.getPaints().size();
			customCar.getPerformanceParts().size();
			customCar.getSkillModParts().size();
			customCar.getVisualParts().size();
			customCar.getVinyls().size();
			return carSlotEntity;
		}
		return null;
	}

	public OwnedCarTrans getDefaultCar(Long personaId) {
		CarSlotEntity carSlotEntity = getDefaultCarEntity(personaId);
		if (carSlotEntity == null) {
			return new OwnedCarTrans();
		}
		return OwnedCarConverter.entity2Trans(carSlotEntity.getOwnedCar());
	}

	public List<CarSlotEntity> getPersonasCar(Long personaId) {
		return carSlotDAO.findByPersonaId(personaId);
	}

	public LevelRepEntity getLevelInfoByLevel(Long level) {
		return levelRepDAO.findByLevel(level);
	}

	public OwnedCarEntity getCarByOwnedCarId(Long ownedCarId) {
		OwnedCarEntity ownedCarEntity = ownedCarDAO.findById(ownedCarId);
		CustomCarEntity customCar = ownedCarEntity.getCustomCar();
		customCar.getPaints().size();
		customCar.getPerformanceParts().size();
		customCar.getSkillModParts().size();
		customCar.getVisualParts().size();
		customCar.getVinyls().size();
		return ownedCarEntity;
	}

	public void updateBadges(Long activePersonaId, BadgeBundle badgeBundle) {
		PersonaEntity persona = personaDAO.findById(activePersonaId);
		List<BadgePersonaEntity> listOfBadges = new ArrayList<>();
		List<BadgeInput> badgeInputs = badgeBundle.getBadgeInputs();
		for (BadgeInput badgeInput : badgeInputs) {

			AchievementStateEntity achievementStateEntity = achievementStateDAO.findByPersonaBadge(persona, (long) badgeInput.getBadgeDefinitionId());
			AchievementRankEntity achievementRank = achievementStateEntity.getAchievementRank();

			BadgePersonaEntity badgePersonaEntity = new BadgePersonaEntity();
			badgePersonaEntity.setAchievementRank(achievementRank);
			badgePersonaEntity.setPersona(persona);
			badgePersonaEntity.setSlot(badgeInput.getSlotId());
			listOfBadges.add(badgePersonaEntity);
		}
		badgePersonaDAO.deleteByPersona(persona);
		persona.setListOfBadges(listOfBadges);
		personaDAO.update(persona);
	}

}
