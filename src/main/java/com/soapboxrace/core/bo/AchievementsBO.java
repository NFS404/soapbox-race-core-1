package com.soapboxrace.core.bo;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.soapboxrace.core.bo.util.AchievementType;
import com.soapboxrace.core.bo.util.OwnedCarConverter;
import com.soapboxrace.core.bo.util.RewardDestinyType;
import com.soapboxrace.core.bo.util.RewardType;
import com.soapboxrace.core.dao.AchievementDAO;
import com.soapboxrace.core.dao.AchievementPersonaDAO;
import com.soapboxrace.core.dao.AchievementRankDAO;
import com.soapboxrace.core.dao.AchievementStateDAO;
import com.soapboxrace.core.dao.BadgeDefinitionDAO;
import com.soapboxrace.core.dao.BasketDefinitionDAO;
import com.soapboxrace.core.dao.CarSlotDAO;
import com.soapboxrace.core.dao.LobbyDAO;
import com.soapboxrace.core.dao.PersonaDAO;
import com.soapboxrace.core.dao.RewardDropDAO;
import com.soapboxrace.core.dao.TokenSessionDAO;
import com.soapboxrace.core.jpa.AchievementDefinitionEntity;
import com.soapboxrace.core.jpa.AchievementPersonaEntity;
import com.soapboxrace.core.jpa.AchievementRankEntity;
import com.soapboxrace.core.jpa.AchievementStateEntity;
import com.soapboxrace.core.jpa.BadgeDefinitionEntity;
import com.soapboxrace.core.jpa.BasketDefinitionEntity;
import com.soapboxrace.core.jpa.CarSlotEntity;
import com.soapboxrace.core.jpa.CustomCarEntity;
import com.soapboxrace.core.jpa.EventDataEntity;
import com.soapboxrace.core.jpa.LobbyEntity;
import com.soapboxrace.core.jpa.OwnedCarEntity;
import com.soapboxrace.core.jpa.PersonaEntity;
import com.soapboxrace.core.jpa.ProductEntity;
import com.soapboxrace.core.jpa.RewardDropEntity;
import com.soapboxrace.core.jpa.TokenSessionEntity;
import com.soapboxrace.core.jpa.TreasureHuntEntity;
import com.soapboxrace.core.xmpp.OpenFireSoapBoxCli;
import com.soapboxrace.jaxb.http.AchievementDefinitionPacket;
import com.soapboxrace.jaxb.http.AchievementRankPacket;
import com.soapboxrace.jaxb.http.AchievementRewards;
import com.soapboxrace.jaxb.http.AchievementState;
import com.soapboxrace.jaxb.http.AchievementsPacket;
import com.soapboxrace.jaxb.http.ArbitrationPacket;
import com.soapboxrace.jaxb.http.ArrayOfAchievementDefinitionPacket;
import com.soapboxrace.jaxb.http.ArrayOfAchievementRankPacket;
import com.soapboxrace.jaxb.http.ArrayOfBadgeDefinitionPacket;
import com.soapboxrace.jaxb.http.ArrayOfCommerceItemTrans;
import com.soapboxrace.jaxb.http.ArrayOfInventoryItemTrans;
import com.soapboxrace.jaxb.http.ArrayOfOwnedCarTrans;
import com.soapboxrace.jaxb.http.ArrayOfWalletTrans;
import com.soapboxrace.jaxb.http.BadgeDefinitionPacket;
import com.soapboxrace.jaxb.http.CommerceItemTrans;
import com.soapboxrace.jaxb.http.CommerceResultStatus;
import com.soapboxrace.jaxb.http.DragArbitrationPacket;
import com.soapboxrace.jaxb.http.InvalidBasketTrans;
import com.soapboxrace.jaxb.http.OwnedCarTrans;
import com.soapboxrace.jaxb.http.PursuitArbitrationPacket;
import com.soapboxrace.jaxb.http.RouteArbitrationPacket;
import com.soapboxrace.jaxb.http.StatConversion;
import com.soapboxrace.jaxb.http.TeamEscapeArbitrationPacket;
import com.soapboxrace.jaxb.http.WalletTrans;
import com.soapboxrace.jaxb.util.UnmarshalXML;
import com.soapboxrace.jaxb.xmpp.AchievementAwarded;
import com.soapboxrace.jaxb.xmpp.AchievementProgress;
import com.soapboxrace.jaxb.xmpp.AchievementsAwarded;

@Stateless
public class AchievementsBO {

	@EJB
	private AchievementDAO achievementDAO;

	@EJB
	private AchievementRankDAO achievementRankDAO;

	@EJB
	private BadgeDefinitionDAO badgeDefinitionDAO;

	@EJB
	private AchievementPersonaDAO achievementPersonaDAO;

	@EJB
	private OpenFireSoapBoxCli openFireSoapBoxCli;

	@EJB
	private PersonaDAO personaDAO;

	@EJB
	private AchievementStateDAO achievementStateDAO;

	@EJB
	private RewardDropDAO rewardDropDAO;

	@EJB
	private DropBO dropBO;

	@EJB
	private InventoryBO inventoryBO;

	@EJB
	private BasketDefinitionDAO basketDefinitionsDAO;

	@EJB
	private CarSlotDAO carSlotDAO;

	@EJB
	private TokenSessionDAO tokenSessionDAO;

	@EJB
	private LobbyDAO lobbyDAO;

	public AchievementsPacket loadall(Long personaId) {
		PersonaEntity personaEntity = new PersonaEntity();
		personaEntity.setPersonaId(personaId);
		AchievementsPacket achievementsPacket = new AchievementsPacket();

		ArrayOfBadgeDefinitionPacket arrayOfBadgeDefinitionPacket = new ArrayOfBadgeDefinitionPacket();
		List<BadgeDefinitionPacket> badgeDefinitionPacketList = arrayOfBadgeDefinitionPacket.getBadgeDefinitionPacket();

		List<BadgeDefinitionEntity> allBadges = badgeDefinitionDAO.getAll();
		for (BadgeDefinitionEntity badgeDefinitionEntity : allBadges) {
			BadgeDefinitionPacket badgeDefinitionPacket = new BadgeDefinitionPacket();
			badgeDefinitionPacket.setBackground(badgeDefinitionEntity.getBackground());
			badgeDefinitionPacket.setBadgeDefinitionId(badgeDefinitionEntity.getId().intValue());
			badgeDefinitionPacket.setBorder(badgeDefinitionEntity.getBorder());
			badgeDefinitionPacket.setDescription(badgeDefinitionEntity.getDescription());
			badgeDefinitionPacket.setIcon(badgeDefinitionEntity.getIcon());
			badgeDefinitionPacket.setName(badgeDefinitionEntity.getName());
			badgeDefinitionPacketList.add(badgeDefinitionPacket);
		}

		achievementsPacket.setBadges(arrayOfBadgeDefinitionPacket);

		ArrayOfAchievementDefinitionPacket arrayOfAchievementDefinitionPacket = new ArrayOfAchievementDefinitionPacket();
		List<AchievementDefinitionPacket> achievementDefinitionPacketList = arrayOfAchievementDefinitionPacket.getAchievementDefinitionPacket();
		List<AchievementDefinitionEntity> allAchievements = achievementDAO.getAll();

		AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);

		for (AchievementDefinitionEntity achievementDefinitionEntity : allAchievements) {
			Long currentValue = getCurrentValue(achievementDefinitionEntity, achievementPersonaEntity);
			AchievementDefinitionPacket achievementDefinitionPacket = new AchievementDefinitionPacket();
			achievementDefinitionPacket.setAchievementDefinitionId(achievementDefinitionEntity.getId().intValue());

			ArrayOfAchievementRankPacket arrayOfAchievementRankPacket = new ArrayOfAchievementRankPacket();
			List<AchievementRankPacket> achievementRankPacketList = arrayOfAchievementRankPacket.getAchievementRankPacket();

			List<AchievementRankEntity> ranks = achievementDefinitionEntity.getRanks();
			long tmpRankValue = 0;
			for (AchievementRankEntity achievementRankEntity : ranks) {
				AchievementRankPacket achievementRankPacket = new AchievementRankPacket();
				achievementRankPacket.setAchievementRankId(achievementRankEntity.getId().intValue());
				achievementRankPacket.setIsRare(achievementRankEntity.isRare());
				achievementRankPacket.setPoints(achievementRankEntity.getPoints());
				achievementRankPacket.setRank(achievementRankEntity.getRank());
				achievementRankPacket.setRarity(achievementRankEntity.getRarity());
				achievementRankPacket.setRewardDescription(achievementRankEntity.getRewardDescription());
				achievementRankPacket.setRewardType(achievementRankEntity.getRewardType());
				// achievementRankPacket.setRewardDescription("GM_ACHIEVEMENT_00000165");
				achievementRankPacket.setRewardVisualStyle(achievementRankEntity.getRewardVisualStyle());
				achievementRankPacket.setThresholdValue(achievementRankEntity.getThresholdValue());

				AchievementStateEntity personaAchievementRankState = achievementStateDAO.findByPersonaAchievementRank(personaEntity, achievementRankEntity);
				if (personaAchievementRankState != null) {
					try {
						LocalDateTime achievedOn = personaAchievementRankState.getAchievedOn();
						GregorianCalendar gcal = GregorianCalendar.from(achievedOn.atZone(ZoneId.systemDefault()));
						XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
						achievementRankPacket.setAchievedOn(xmlCalendar);
					} catch (Exception e) {
						System.err.println("xml calendar str error");
					}
					achievementRankPacket.setState(personaAchievementRankState.getAchievementState());
				} else {
					if (currentValue.longValue() > tmpRankValue || tmpRankValue == 0l) {
						achievementRankPacket.setState(AchievementState.IN_PROGRESS);
					} else {
						achievementRankPacket.setState(AchievementState.LOCKED);
					}
					tmpRankValue = achievementRankEntity.getThresholdValue().longValue();
				}
				achievementRankPacketList.add(achievementRankPacket);
			}

			achievementDefinitionPacket.setAchievementRanks(arrayOfAchievementRankPacket);

			achievementDefinitionPacket.setBadgeDefinitionId(achievementDefinitionEntity.getBadgeDefinition().getId().intValue());
			achievementDefinitionPacket.setCanProgress(achievementDefinitionEntity.isCanProgress());
			achievementDefinitionPacket.setCurrentValue(currentValue);
			achievementDefinitionPacket.setIsVisible(achievementDefinitionEntity.isVisible());
			achievementDefinitionPacket.setProgressText(achievementDefinitionEntity.getProgressText());
			achievementDefinitionPacket.setStatConversion(StatConversion.valueOf(achievementDefinitionEntity.getStatConversion()));
			achievementDefinitionPacketList.add(achievementDefinitionPacket);
		}
		achievementsPacket.setDefinitions(arrayOfAchievementDefinitionPacket);
		return achievementsPacket;
	}

	private Long getCurrentValue(AchievementDefinitionEntity achievementDefinitionEntity, AchievementPersonaEntity achievementPersonaEntity) {
		int intValue = achievementDefinitionEntity.getId().intValue();
		AchievementType achievementType = AchievementType.valueOf(intValue);
		switch (achievementType) {
		case AFTERMARKET_SPECIALIST:
			return 0l;
		case AIRTIME:
			return achievementPersonaEntity.getEventAerialTime();
		case ALFA_ROMEO_COLLECTOR:
			return 0l;
		case ASTON_MARTIN_COLLECTOR:
			return 0l;
		case AUDI_COLLECTOR:
			return 0l;
		case A_CLASS_CHAMPION:
			int restrictedClassAWins = achievementPersonaEntity.getRestrictedClassAWins();
			return Integer.valueOf(restrictedClassAWins).longValue();
		case BENTLEY_COLLECTOR:
			return 0l;
		case BMW_COLLECTOR:
			return 0l;
		case B_CLASS_CHAMPION:
			int restrictedClassBWins = achievementPersonaEntity.getRestrictedClassBWins();
			return Integer.valueOf(restrictedClassBWins).longValue();
		case CADILLAC_COLLECTOR:
			return 0l;
		case CAR_ARTIST:
			return 0l;
		case CATERHAM_COLLECTOR:
			return 0l;
		case CHEVROLET_COLLECTOR:
			return 0l;
		case CHRYSLER_COLLECTOR:
			return 0l;
		case COLLECTOR:
			return 0l;
		case CREW_RACER:
			int privateRaces = achievementPersonaEntity.getPrivateRaces();
			return Integer.valueOf(privateRaces).longValue();
		case C_CLASS_CHAMPION:
			int restrictedClassCWins = achievementPersonaEntity.getRestrictedClassCWins();
			return Integer.valueOf(restrictedClassCWins).longValue();
		case DAILY_HUNTER:
			int dailyTreasureHunts = achievementPersonaEntity.getDailyTreasureHunts();
			return Integer.valueOf(dailyTreasureHunts).longValue();
		case DEVELOPER:
			return 0l;
		case DODGE_COLLECTOR:
			return 0l;
		case DRAG_RACER:
			int mpDragWins = achievementPersonaEntity.getMpDragWins();
			return Integer.valueOf(mpDragWins).longValue();
		case D_CLASS_CHAMPION:
			int restrictedClassDWins = achievementPersonaEntity.getRestrictedClassDWins();
			return Integer.valueOf(restrictedClassDWins).longValue();
		case ENEMY_OF_THE_STATE:
			return achievementPersonaEntity.getPursuitCostToState();
		case EXPLORE_MODDER:
			return 0l;
		case E_CLASS_CHAMPION:
			int restrictedClassEWins = achievementPersonaEntity.getRestrictedClassEWins();
			return Integer.valueOf(restrictedClassEWins).longValue();
		case FORD_COLLECTOR:
			return 0l;
		case FORD_SHELBY_COLLECTOR:
			return 0l;
		case FRESH_COAT:
			return 0l;
		case GETAWAY_DRIVER:
			int teamScapeWins = achievementPersonaEntity.getTeamScapeWins();
			return Integer.valueOf(teamScapeWins).longValue();
		case HEAVY_HITTER:
			int teamScapePoliceDown = achievementPersonaEntity.getTeamScapePoliceDown();
			return Integer.valueOf(teamScapePoliceDown).longValue();
		case HUMMER_COLLECTOR:
			return 0l;
		case INFINITI_COLLECTOR:
			return 0l;
		case JAGUAR_COLLECTOR:
			return 0l;
		case JEEP_COLLECTOR:
			return 0l;
		case KOENIGSEGG_COLLECTOR:
			return 0l;
		case LAMBORGHINI_COLLECTOR:
			return 0l;
		case LANCIA_COLLECTOR:
			return 0l;
		case LEGENDARY_DRIVER:
			int score = achievementPersonaEntity.getPersona().getScore();
			return Integer.valueOf(score).longValue();
		case LEVEL_UP:
			int level = achievementPersonaEntity.getPersona().getLevel();
			return Integer.valueOf(level).longValue();
		case LEXUS_COLLECTOR:
			return 0l;
		case LONG_HAUL:
			return 0l;
		case LOTUS_COLLECTOR:
			return 0l;
		case MARUSSIA_COLLECTOR:
			return 0l;
		case MAZDA_COLLECTOR:
			return 0l;
		case MCLAREN_COLLECTOR:
			return 0l;
		case MERCEDES_BENZ_COLLECTOR:
			return 0l;
		case MITSUBISHI_COLLECTOR:
			return 0l;
		case NISSAN_COLLECTOR:
			return 0l;
		case OPEN_BETA:
			return 0l;
		case OUTLAW:
			int pursuitWins = achievementPersonaEntity.getPursuitWins();
			return Integer.valueOf(pursuitWins).longValue();
		case PAGANI_COLLECTOR:
			return 0l;
		case PAYDAY:
			int totalIncomeCash = achievementPersonaEntity.getTotalIncomeCash();
			return Integer.valueOf(totalIncomeCash).longValue();
		case PLYMOUTH_COLLECTOR:
			return 0l;
		case PONTIAC_COLLECTOR:
			return 0l;
		case PORSCHE_COLLECTOR:
			return 0l;
		case POWERING_UP:
			return Integer.valueOf(achievementPersonaEntity.getUsedPowerups()).longValue();
		case PRO_TUNER:
			return 0l;
		case PURSUIT_MODDER:
			return 0l;
		case RACE_MODDER:
			return 0l;
		case RENAULT_COLLECTOR:
			return 0l;
		case SCION_COLLECTOR:
			return 0l;
		case SHELBY_COLLECTOR:
			return 0l;
		case SOLO_RACER:
			int spRaces = achievementPersonaEntity.getSpRaces();
			return Integer.valueOf(spRaces).longValue();
		case SUBARU_COLLECTOR:
			return 0l;
		case S_CLASS_CHAMPION:
			int restrictedClassSWins = achievementPersonaEntity.getRestrictedClassSWins();
			return Integer.valueOf(restrictedClassSWins).longValue();
		case THREADING_THE_NEEDLE:
			int teamScapeBlocks = achievementPersonaEntity.getTeamScapeBlocks();
			return Integer.valueOf(teamScapeBlocks).longValue();
		case TOYOTA_COLLECTOR:
			return 0l;
		case TREASURE_HUNTER:
			int treasureHunts = achievementPersonaEntity.getTreasureHunts();
			return Integer.valueOf(treasureHunts).longValue();
		case VAUXHALL_COLLECTOR:
			return 0l;
		case VOLKSWAGEN_COLLECTOR:
			return 0l;
		case WORLD_RACER:
			int mpRaces = achievementPersonaEntity.getMpRaces();
			return Integer.valueOf(mpRaces).longValue();
		case XKR_SPEED_HUNTER:
			return 0l;
		default:
			break;
		}

		return 0l;
	}

	public void applyCommerceAchievement(PersonaEntity personaEntity) {
		//
	}

	public void applyEventAchievement(PersonaEntity personaEntity) {
		//
	}

	public void applyPowerupAchievement(PersonaEntity personaEntity) {
		AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);
		Integer usedPowerups = achievementPersonaEntity.getUsedPowerups() + 1;
		achievementPersonaEntity.setUsedPowerups(usedPowerups);
		processAchievementByThresholdValue(achievementPersonaEntity, AchievementType.POWERING_UP, usedPowerups.longValue());
	}

	public void applyTreasureHuntAchievement(TreasureHuntEntity treasureHuntEntity) {
		PersonaEntity personaEntity = personaDAO.findById(treasureHuntEntity.getPersonaId());
		AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);
		int treasureHunts = achievementPersonaEntity.getTreasureHunts() + 1;
		achievementPersonaEntity.setTreasureHunts(treasureHunts);
		processAchievementByThresholdValue(achievementPersonaEntity, AchievementType.TREASURE_HUNTER, Integer.valueOf(treasureHunts).longValue());
	}

	public void applyDailyTreasureHuntAchievement(TreasureHuntEntity treasureHuntEntity) {
		PersonaEntity personaEntity = personaDAO.findById(treasureHuntEntity.getPersonaId());
		AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);
		Integer streak = treasureHuntEntity.getStreak();
		if (treasureHuntEntity.getStreak() > achievementPersonaEntity.getDailyTreasureHunts()) {
		   achievementPersonaEntity.setDailyTreasureHunts(streak); 
		   processAchievementByThresholdValue(achievementPersonaEntity, AchievementType.DAILY_HUNTER, streak.longValue());
		}
	}

	private void processAchievementByThresholdValue(AchievementPersonaEntity achievementPersonaEntity, AchievementType achievementType, Long thresholdValue) {
		PersonaEntity personaEntity = achievementPersonaEntity.getPersona();
		broadcastProgress(personaEntity, achievementType, thresholdValue);
		achievementPersonaDAO.update(achievementPersonaEntity);
		AchievementRankEntity achievementRankEntity = achievementRankDAO.findByAchievementDefinitionIdThresholdValue( //
				achievementType.getId(), thresholdValue);
		if (achievementRankEntity != null && achievementRankEntity.getAchievementDefinition().isVisible()) {
			AchievementStateEntity achievementStateEntity = new AchievementStateEntity();
			achievementStateEntity.setAchievedOn(LocalDateTime.now());
			achievementStateEntity.setAchievementRank(achievementRankEntity);
			achievementStateEntity.setAchievementState(AchievementState.REWARD_WAITING);
			achievementStateEntity.setPersona(personaEntity);
			achievementStateDAO.insert(achievementStateEntity);
			personaEntity.setScore(personaEntity.getScore() + achievementRankEntity.getPoints());
			personaDAO.update(personaEntity);
			broadcastAchievement(personaEntity, achievementRankEntity);
			processAchievementByThresholdRange(achievementPersonaEntity, AchievementType.LEGENDARY_DRIVER,
					Integer.valueOf(personaEntity.getScore()).longValue());
		}
	}

	private void processAchievementByThresholdRange(AchievementPersonaEntity achievementPersonaEntity, AchievementType achievementType, Long thresholdValue) {
		PersonaEntity personaEntity = achievementPersonaEntity.getPersona();
		broadcastProgress(personaEntity, achievementType, thresholdValue);
		achievementPersonaDAO.update(achievementPersonaEntity);
		AchievementRankEntity achievementRankEntity = achievementRankDAO.findByAchievementDefinitionIdThresholdPersona(achievementType.getId(), thresholdValue,
				personaEntity);
		if (achievementRankEntity != null && achievementRankEntity.getAchievementDefinition().isVisible()) {
			AchievementStateEntity achievementStateEntity = new AchievementStateEntity();
			achievementStateEntity.setAchievedOn(LocalDateTime.now());
			achievementStateEntity.setAchievementRank(achievementRankEntity);
			achievementStateEntity.setAchievementState(AchievementState.REWARD_WAITING);
			achievementStateEntity.setPersona(personaEntity);
			achievementStateDAO.insert(achievementStateEntity);
			personaEntity.setScore(personaEntity.getScore() + achievementRankEntity.getPoints());
			personaDAO.update(personaEntity);
			broadcastAchievement(personaEntity, achievementRankEntity);
			processAchievementByThresholdRange(achievementPersonaEntity, AchievementType.LEGENDARY_DRIVER,
					Integer.valueOf(personaEntity.getScore()).longValue());
		}
	}

	public void applyPayDayAchievement(PersonaEntity personaEntity, double incomeCash) {
		if (incomeCash > 0) {
			AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);
			int totalIncomeCash = achievementPersonaEntity.getTotalIncomeCash() + (int) incomeCash;
			achievementPersonaEntity.setTotalIncomeCash(totalIncomeCash);
			processAchievementByThresholdRange(achievementPersonaEntity, AchievementType.PAYDAY, Integer.valueOf(totalIncomeCash).longValue());
		}
	}

	public void broadcastProgress(PersonaEntity personaEntity, AchievementType achievementType, Long currentValue) {
		AchievementsAwarded achievementsAwarded = new AchievementsAwarded();
		AchievementProgress achievementProgress = new AchievementProgress();
		achievementProgress.setAchievementDefinitionId(achievementType.getId());
		achievementProgress.setCurrentValue(currentValue);
		List<AchievementProgress> progressedList = new ArrayList<>();
		progressedList.add(achievementProgress);
		achievementsAwarded.setProgressed(progressedList);
		openFireSoapBoxCli.send(achievementsAwarded, personaEntity.getPersonaId());
	}

	public void broadcastAchievement(PersonaEntity personaEntity, AchievementRankEntity achievementRankEntity) {
		AchievementsAwarded achievementsAwarded = new AchievementsAwarded();
		achievementsAwarded.setPersonaId(personaEntity.getPersonaId());
		achievementsAwarded.setScore(personaEntity.getScore());
		AchievementAwarded achievementAwarded = new AchievementAwarded();

		String achievedOnStr = "0001-01-01T00:00:00";
		try {
			LocalDate date = LocalDate.now();
			GregorianCalendar gcal = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
			XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			xmlCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
			achievedOnStr = xmlCalendar.toXMLFormat();
		} catch (Exception e) {
			System.err.println("xml calendar str error");
		}
		achievementAwarded.setAchievedOn(achievedOnStr);
		achievementAwarded.setAchievementDefinitionId(achievementRankEntity.getAchievementDefinition().getId());
		achievementAwarded.setAchievementRankId(achievementRankEntity.getId());
		achievementAwarded.setClip("AchievementFlasherBase");
		achievementAwarded.setClipLengthInSeconds(5);
		achievementAwarded.setDescription(achievementRankEntity.getAchievementDefinition().getBadgeDefinition().getDescription());
		achievementAwarded.setIcon(achievementRankEntity.getAchievementDefinition().getBadgeDefinition().getIcon());
		achievementAwarded.setName(achievementRankEntity.getAchievementDefinition().getBadgeDefinition().getName());
		achievementAwarded.setPoints(achievementRankEntity.getPoints());
		achievementAwarded.setRare(achievementRankEntity.isRare());
		achievementAwarded.setRarity(achievementRankEntity.getRarity());

		ArrayList<AchievementAwarded> achievements = new ArrayList<>();
		achievements.add(achievementAwarded);

		achievementsAwarded.setAchievements(achievements);
		achievementsAwarded.setScore(personaEntity.getScore());
		openFireSoapBoxCli.send(achievementsAwarded, personaEntity.getPersonaId());
	}

	public AchievementRewards redeemReward(Long personaId, Long achievementRankId) {
		PersonaEntity personaEntity = personaDAO.findById(personaId);

		AchievementRankEntity achievementRankEntity = achievementRankDAO.findById(achievementRankId);
		AchievementStateEntity personaAchievementRank = achievementStateDAO.findByPersonaAchievementRank(personaEntity, achievementRankEntity);
		if (personaAchievementRank != null) {
			personaAchievementRank.setAchievementState(AchievementState.COMPLETED);
			achievementStateDAO.update(personaAchievementRank);
		}

		String rewardTypeStr = achievementRankEntity.getRewardType();
		RewardType rewardType = RewardType.valueOf(rewardTypeStr);
		boolean isCardPack = RewardType.cardpack.equals(rewardType);

		AchievementRewards achievementRewards = new AchievementRewards();
		List<CommerceItemTrans> commerceItems = new ArrayList<>();

		List<RewardDropEntity> rewardDrops = rewardDropDAO.getRewardDrops(achievementRankEntity, isCardPack);
		if (isCardPack && rewardDrops.size() < 5) {
			for (int i = 0; i < 5; i++) {
				RewardDropEntity rewardDropEntity = new RewardDropEntity();
				rewardDropEntity.setAmount(1);
				rewardDropEntity.setProduct(dropBO.getRandomProductItem());
				rewardDropEntity.setRewardDestiny(RewardDestinyType.INVENTORY);
				rewardDrops.add(rewardDropEntity);
			}
		}
		Collections.shuffle(rewardDrops);
		for (RewardDropEntity rewardDropEntity : rewardDrops) {
			CommerceItemTrans item = new CommerceItemTrans();
			ProductEntity product = rewardDropEntity.getProduct();

			RewardDestinyType rewardDestiny = rewardDropEntity.getRewardDestiny();
			switch (rewardDestiny) {
			case CASH:
				personaEntity.setCash(personaEntity.getCash() + rewardDropEntity.getAmount().doubleValue());
				personaDAO.update(personaEntity);
				item.setHash(-429893590);
				String moneyFormat = NumberFormat.getNumberInstance(Locale.US).format(rewardDropEntity.getAmount());
				item.setTitle("$" + moneyFormat);
				break;
			case GARAGE:
				// FIXME pog copiada do basketbo
				BasketDefinitionEntity basketDefinitonEntity = basketDefinitionsDAO.findById(product.getProductId());
				if (basketDefinitonEntity == null) {
					throw new IllegalArgumentException(String.format("No basket definition for %s", product.getProductId()));
				}
				String ownedCarTransStr = basketDefinitonEntity.getOwnedCarTrans();
				OwnedCarTrans ownedCarTrans = UnmarshalXML.unMarshal(ownedCarTransStr, OwnedCarTrans.class);
				ownedCarTrans.setId(0L);
				ownedCarTrans.getCustomCar().setId(0);
				CarSlotEntity carSlotEntity = new CarSlotEntity();
				carSlotEntity.setPersona(personaEntity);

				OwnedCarEntity ownedCarEntity = new OwnedCarEntity();
				ownedCarEntity.setCarSlot(carSlotEntity);
				CustomCarEntity customCarEntity = new CustomCarEntity();
				customCarEntity.setOwnedCar(ownedCarEntity);
				ownedCarEntity.setCustomCar(customCarEntity);
				carSlotEntity.setOwnedCar(ownedCarEntity);
				OwnedCarConverter.trans2Entity(ownedCarTrans, ownedCarEntity);
				OwnedCarConverter.details2NewEntity(ownedCarTrans, ownedCarEntity);

				carSlotDAO.insert(carSlotEntity);

				// FIXME get better icon hash, like gift
				item.setHash(product.getHash());
				item.setTitle(rewardDropEntity.getAchievementRank().getRewardText());

				break;
			case INVENTORY:

				String productTitle = product.getProductTitle();
				String title = productTitle.replace(" x15", "");
				if (rewardDropEntity.getAmount().intValue() > 1) {
					title = title + " x" + rewardDropEntity.getAmount().toString();
				}
				item.setHash(product.getHash());
				item.setTitle(title);

				product.setUseCount(rewardDropEntity.getAmount().intValue());
				inventoryBO.addDroppedItem(product, personaEntity);
				break;
			default:
				break;
			}
			commerceItems.add(item);
		}

		ArrayOfInventoryItemTrans arrayOfInventoryItemTrans = new ArrayOfInventoryItemTrans();

		ArrayOfCommerceItemTrans arrayOfCommerceItemTrans = new ArrayOfCommerceItemTrans();
		arrayOfCommerceItemTrans.getCommerceItemTrans().addAll(commerceItems);
		achievementRewards.setCommerceItems(arrayOfCommerceItemTrans);
		WalletTrans walletTrans = new WalletTrans();
		walletTrans.setBalance(personaEntity.getCash());
		walletTrans.setCurrency("CASH");

		ArrayOfWalletTrans arrayOfWalletTrans = new ArrayOfWalletTrans();
		arrayOfWalletTrans.getWalletTrans().add(walletTrans);

		achievementRewards.setWallets(arrayOfWalletTrans);
		achievementRewards.setStatus(CommerceResultStatus.SUCCESS);
		achievementRewards.setInventoryItems(arrayOfInventoryItemTrans);
		achievementRewards.setInvalidBasket(new InvalidBasketTrans());
		achievementRewards.setPurchasedCars(new ArrayOfOwnedCarTrans());

		achievementRewards.setVisualStyle(achievementRankEntity.getRewardVisualStyle());

		achievementRewards.setAchievementRankId(achievementRankId);
		// to debug inside game:
		// achievementRewards.setAchievementRankId(0L);

		return achievementRewards;
	}

	public void applyRaceAchievements(EventDataEntity eventDataEntity, RouteArbitrationPacket routeArbitrationPacket, PersonaEntity personaEntity) {
		// FIXME this code is a mess
		TokenSessionEntity tokenSessionEntity = tokenSessionDAO.findByUserId(personaEntity.getUser().getId());
		Long activeLobbyId = tokenSessionEntity.getActiveLobbyId();
		Boolean isPrivate = false;
		if (!activeLobbyId.equals(0l)) {
			LobbyEntity lobbyEntity = lobbyDAO.findById(activeLobbyId);
			isPrivate = lobbyEntity.getIsPrivate();
		}
		AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);
		if (isPrivate) {
			int privateRaces = achievementPersonaEntity.getPrivateRaces();
			privateRaces = privateRaces + 1;
			achievementPersonaEntity.setPrivateRaces(privateRaces);
			processAchievementByThresholdValue(achievementPersonaEntity, AchievementType.CREW_RACER, Integer.valueOf(privateRaces).longValue());
		} else if (!activeLobbyId.equals(0l)) {
			int mpRaces = achievementPersonaEntity.getMpRaces();
			mpRaces = mpRaces + 1;
			achievementPersonaEntity.setMpRaces(mpRaces);
			processAchievementByThresholdValue(achievementPersonaEntity, AchievementType.WORLD_RACER, Integer.valueOf(mpRaces).longValue());
			if (routeArbitrationPacket.getRank() == 1) {
				AchievementType achievementType = null;
				Long thresholdValue = null;
				switch (eventDataEntity.getEvent().getCarClassHash()) {
				case -405837480: {
					achievementType = AchievementType.A_CLASS_CHAMPION;
					int restrictedClassAWins = achievementPersonaEntity.getRestrictedClassAWins();
					restrictedClassAWins++;
					thresholdValue = Integer.valueOf(restrictedClassAWins).longValue();
					achievementPersonaEntity.setRestrictedClassAWins(restrictedClassAWins);
					break;
				}
				case -406473455: {
					achievementType = AchievementType.B_CLASS_CHAMPION;
					int restrictedClassBWins = achievementPersonaEntity.getRestrictedClassBWins();
					restrictedClassBWins++;
					achievementPersonaEntity.setRestrictedClassBWins(restrictedClassBWins);
					thresholdValue = Integer.valueOf(restrictedClassBWins).longValue();
					break;
				}
				case 1866825865: {
					achievementType = AchievementType.C_CLASS_CHAMPION;
					int restrictedClassCWins = achievementPersonaEntity.getRestrictedClassCWins();
					restrictedClassCWins++;
					achievementPersonaEntity.setRestrictedClassCWins(restrictedClassCWins);
					thresholdValue = Integer.valueOf(restrictedClassCWins).longValue();
					break;
				}
				case 415909161: {
					achievementType = AchievementType.D_CLASS_CHAMPION;
					int restrictedClassDWins = achievementPersonaEntity.getRestrictedClassDWins();
					restrictedClassDWins++;
					achievementPersonaEntity.setRestrictedClassDWins(restrictedClassDWins);
					thresholdValue = Integer.valueOf(restrictedClassDWins).longValue();
					break;
				}
				case 872416321: {
					achievementType = AchievementType.E_CLASS_CHAMPION;
					int restrictedClassEWins = achievementPersonaEntity.getRestrictedClassEWins();
					restrictedClassEWins++;
					achievementPersonaEntity.setRestrictedClassEWins(restrictedClassEWins);
					thresholdValue = Integer.valueOf(restrictedClassEWins).longValue();
					break;
				}
				case -2142411446: {
					achievementType = AchievementType.S_CLASS_CHAMPION;
					int restrictedClassSWins = achievementPersonaEntity.getRestrictedClassSWins();
					restrictedClassSWins++;
					achievementPersonaEntity.setRestrictedClassSWins(restrictedClassSWins);
					thresholdValue = Integer.valueOf(restrictedClassSWins).longValue();
					break;
				}
				default:
					break;
				}
				if (achievementType != null) {
					processAchievementByThresholdValue(achievementPersonaEntity, achievementType, thresholdValue);
				}
			}
		} else {
			int spRaces = achievementPersonaEntity.getSpRaces();
			spRaces = spRaces + 1;
			achievementPersonaEntity.setSpRaces(spRaces);
			processAchievementByThresholdValue(achievementPersonaEntity, AchievementType.SOLO_RACER, Integer.valueOf(spRaces).longValue());
		}
	}

	public void applyOutlawAchievement(PersonaEntity personaEntity) {
		AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);
		int pursuitWins = achievementPersonaEntity.getPursuitWins();
		pursuitWins = pursuitWins + 1;
		achievementPersonaEntity.setPursuitWins(pursuitWins);
		processAchievementByThresholdValue(achievementPersonaEntity, AchievementType.OUTLAW, Integer.valueOf(pursuitWins).longValue());
	}

	public void applyDragAchievement(EventDataEntity eventDataEntity, DragArbitrationPacket dragArbitrationPacket, Long activePersonaId) {
		PersonaEntity personaEntity = personaDAO.findById(activePersonaId);
		TokenSessionEntity tokenSessionEntity = tokenSessionDAO.findByUserId(personaEntity.getUser().getId());
		Long activeLobbyId = tokenSessionEntity.getActiveLobbyId();
		if (!activeLobbyId.equals(0l)) {
			LobbyEntity lobbyEntity = lobbyDAO.findById(activeLobbyId);
			if (!lobbyEntity.getIsPrivate()) {
				if (dragArbitrationPacket.getRank() == 1) {
					AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);
					int mpDragWins = achievementPersonaEntity.getMpDragWins();
					mpDragWins = mpDragWins + 1;
					achievementPersonaEntity.setMpDragWins(mpDragWins);
					processAchievementByThresholdValue(achievementPersonaEntity, AchievementType.DRAG_RACER, Integer.valueOf(mpDragWins).longValue());
				}
			}
		}
	}

	public void applyLevelUpAchievement(PersonaEntity personaEntity) {
		AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);
		processAchievementByThresholdValue(achievementPersonaEntity, AchievementType.LEVEL_UP, Integer.valueOf(personaEntity.getLevel()).longValue());
	}

	public void applyAirTimeAchievement(ArbitrationPacket arbitrationPacket, PersonaEntity personaEntity) {
		long specificJumpsDurationInMilliseconds = 0l;
		if (arbitrationPacket instanceof PursuitArbitrationPacket) {
			PursuitArbitrationPacket specificArbitrationPacket = (PursuitArbitrationPacket) arbitrationPacket;
			specificJumpsDurationInMilliseconds = specificArbitrationPacket.getSumOfJumpsDurationInMilliseconds();
		} else if (arbitrationPacket instanceof TeamEscapeArbitrationPacket) {
			TeamEscapeArbitrationPacket specificArbitrationPacket = (TeamEscapeArbitrationPacket) arbitrationPacket;
			specificJumpsDurationInMilliseconds = specificArbitrationPacket.getSumOfJumpsDurationInMilliseconds();
		} else if (arbitrationPacket instanceof RouteArbitrationPacket) {
			RouteArbitrationPacket specificArbitrationPacket = (RouteArbitrationPacket) arbitrationPacket;
			specificJumpsDurationInMilliseconds = specificArbitrationPacket.getSumOfJumpsDurationInMilliseconds();
		} else if (arbitrationPacket instanceof DragArbitrationPacket) {
			DragArbitrationPacket specificArbitrationPacket = (DragArbitrationPacket) arbitrationPacket;
			specificJumpsDurationInMilliseconds = specificArbitrationPacket.getSumOfJumpsDurationInMilliseconds();
		}
		if (specificJumpsDurationInMilliseconds > 0) {
			AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);
			long sumOfJumpsDurationInMilliseconds = achievementPersonaEntity.getEventAerialTime();
			sumOfJumpsDurationInMilliseconds = sumOfJumpsDurationInMilliseconds + specificJumpsDurationInMilliseconds;
			achievementPersonaEntity.setEventAerialTime(sumOfJumpsDurationInMilliseconds);
			processAchievementByThresholdRange(achievementPersonaEntity, AchievementType.AIRTIME, sumOfJumpsDurationInMilliseconds);
		}
	}

	public void applyPursuitCostToState(ArbitrationPacket arbitrationPacket, PersonaEntity personaEntity) {
		AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);
		long pursuitCostToState = achievementPersonaEntity.getPursuitCostToState();
		if (arbitrationPacket instanceof PursuitArbitrationPacket) {
			PursuitArbitrationPacket specificArbitrationPacket = (PursuitArbitrationPacket) arbitrationPacket;
			pursuitCostToState = pursuitCostToState + Integer.valueOf(specificArbitrationPacket.getCostToState()).longValue();
		} else if (arbitrationPacket instanceof TeamEscapeArbitrationPacket) {
			TeamEscapeArbitrationPacket specificArbitrationPacket = (TeamEscapeArbitrationPacket) arbitrationPacket;
			pursuitCostToState = pursuitCostToState + Integer.valueOf(specificArbitrationPacket.getCostToState()).longValue();
		}
		if (pursuitCostToState > 0) {
			achievementPersonaEntity.setPursuitCostToState(pursuitCostToState);
			processAchievementByThresholdRange(achievementPersonaEntity, AchievementType.ENEMY_OF_THE_STATE, pursuitCostToState);
		}
	}

	public void applyTeamEscape(TeamEscapeArbitrationPacket teamEscapeArbitrationPacket, PersonaEntity personaEntity) {
		AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);
		if (teamEscapeArbitrationPacket.getCopsDisabled() > 0) {
			int copsDisabled = teamEscapeArbitrationPacket.getCopsDisabled() + achievementPersonaEntity.getTeamScapePoliceDown();
			achievementPersonaEntity.setTeamScapePoliceDown(copsDisabled);
			long copsDisabledLong = Integer.valueOf(copsDisabled).longValue();
			processAchievementByThresholdRange(achievementPersonaEntity, AchievementType.HEAVY_HITTER, copsDisabledLong);
		}

		if (teamEscapeArbitrationPacket.getRoadBlocksDodged() > 0) {
			int roadBlocksDodged = teamEscapeArbitrationPacket.getRoadBlocksDodged() + achievementPersonaEntity.getTeamScapeBlocks();
			achievementPersonaEntity.setTeamScapeBlocks(roadBlocksDodged);
			long roadBlocksDodgedLong = Integer.valueOf(roadBlocksDodged).longValue();
			processAchievementByThresholdRange(achievementPersonaEntity, AchievementType.THREADING_THE_NEEDLE, roadBlocksDodgedLong);
		}
	}

	public void applyTeamEscapeGetAway(PersonaEntity personaEntity) {
		AchievementPersonaEntity achievementPersonaEntity = achievementPersonaDAO.findByPersona(personaEntity);
		int teamScapeWins = achievementPersonaEntity.getTeamScapeWins();
		teamScapeWins = teamScapeWins + 1;
		achievementPersonaEntity.setTeamScapeWins(teamScapeWins);
		long teamScapeWinsLong = Integer.valueOf(teamScapeWins).longValue();
		processAchievementByThresholdValue(achievementPersonaEntity, AchievementType.GETAWAY_DRIVER, teamScapeWinsLong);
	}
}
