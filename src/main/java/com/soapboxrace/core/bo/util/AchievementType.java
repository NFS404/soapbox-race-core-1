package com.soapboxrace.core.bo.util;

import java.util.Arrays;

public enum AchievementType {

	// GENERAL PLAY
	/**
	 * earn [x] cash total
	 */
	PAYDAY(17), //
	/**
	 * reach driver level [x]
	 */
	LEVEL_UP(23), //
	/**
	 * reach [x] total driver score by completing achievements
	 */
	LEGENDARY_DRIVER(24), //
	/**
	 * activate [x] powerup(s)
	 */
	POWERING_UP(66), //
	/**
	 * accumulate [x] of total airtime in events
	 */
	AIRTIME(13), //
	/**
	 * drive [x] (km/mi) in events
	 */
	LONG_HAUL(14), //

	// DRAG ONLY
	/**
	 * win [x] multiplayer drag races
	 */
	DRAG_RACER(7), //

	// CAR MOD ONLY
	/**
	 * applly [x] vinyls
	 */
	CAR_ARTIST(25), //
	/**
	 * install [x] performance parts (3-star or better)
	 */
	PRO_TUNER(18), //
	/**
	 * install [x] explore skill mods (3-star or better)
	 */
	EXPLORE_MODDER(19), //
	/**
	 * install [x] pursuit skill mods (3-star or better)
	 */
	PURSUIT_MODDER(20), //
	/**
	 * install [x] race skill mods (3-star or better)
	 */
	RACE_MODDER(21), //
	/**
	 * apply [x] paints
	 */
	FRESH_COAT(22), //
	/**
	 * install [x] aftermarket parts
	 */
	AFTERMARKET_SPECIALIST(15), //

	// TREASURE HUNT ONLY
	/**
	 * complete [x] treasure hunts
	 */
	TREASURE_HUNTER(68), //
	/**
	 * complete [x] consecutive treasure hunts
	 */
	DAILY_HUNTER(69), //
	/**
	 * complete a treasure hunt in under [x] using the jaguar xkr
	 */
	XKR_SPEED_HUNTER(72), //

	// TEAM ESCAPE EVENTS ONLY
	/**
	 * disable [x] cops in team escape
	 */
	HEAVY_HITTER(10), //
	/**
	 * dodge [x] roadblocks in team escape
	 */
	THREADING_THE_NEEDLE(11), //
	/**
	 * complete [x] team escapes with at least one teammate evading
	 */
	GETAWAY_DRIVER(12), //

	// SPRINT CIRCUIT ONLY
	/**
	 * win [x] a-class restricted multiplayer sprint & circuits
	 */
	A_CLASS_CHAMPION(1), //
	/**
	 * win [x] b-class restricted multiplayer sprints & circuits
	 */
	B_CLASS_CHAMPION(2), //
	/**
	 * win [x] c-class restricted multiplayer sprints & circuits
	 */
	C_CLASS_CHAMPION(3), //
	/**
	 * win [x] d-class restricted multiplayer sprints & circuits
	 */
	D_CLASS_CHAMPION(4), //
	/**
	 * win [x] e-class restricted multiplayer sprints & circuits
	 */
	E_CLASS_CHAMPION(5), //
	/**
	 * win [x] s-class restricted multiplayer sprints & circuits
	 */
	S_CLASS_CHAMPION(6), //
	/**
	 * play [x] multiplayer sprints & circuits
	 */
	WORLD_RACER(67), //
	/**
	 * play [x] sprints & circuits in private matches
	 */
	CREW_RACER(70), //
	/**
	 * play [x] sprints & circuits in single player
	 */
	SOLO_RACER(71), //

	// PURSUIT EVENTS ONLY
	/**
	 * accumulate [x] cost to state incurred in pursuits
	 */
	ENEMY_OF_THE_STATE(8), //
	/**
	 * successfully evade the cops [x] times in pursuit outrun
	 */
	OUTLAW(9), //

	// HARDCODED
	/**
	 * participated in the need for speed world open beta
	 */
	OPEN_BETA(26), //
	/**
	 * need for speed world developer
	 */
	DEVELOPER(27), //

	// COLLECTORS
	/**
	 * own [x] cars in your garage
	 */
	COLLECTOR(16), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	ALFA_ROMEO_COLLECTOR(28), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	ASTON_MARTIN_COLLECTOR(29), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	AUDI_COLLECTOR(30), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	BENTLEY_COLLECTOR(31), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	BMW_COLLECTOR(32), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	CADILLAC_COLLECTOR(33), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	CATERHAM_COLLECTOR(34), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	CHEVROLET_COLLECTOR(35), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	CHRYSLER_COLLECTOR(36), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	DODGE_COLLECTOR(37), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	FORD_COLLECTOR(38), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	FORD_SHELBY_COLLECTOR(39), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	HUMMER_COLLECTOR(40), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	INFINITI_COLLECTOR(41), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	JAGUAR_COLLECTOR(42), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	JEEP_COLLECTOR(43), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	KOENIGSEGG_COLLECTOR(44), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	LAMBORGHINI_COLLECTOR(45), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	LANCIA_COLLECTOR(46), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	LEXUS_COLLECTOR(47), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	LOTUS_COLLECTOR(48), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	MARUSSIA_COLLECTOR(49), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	MAZDA_COLLECTOR(50), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	MCLAREN_COLLECTOR(51), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	MERCEDES_BENZ_COLLECTOR(52), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	MITSUBISHI_COLLECTOR(53), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	NISSAN_COLLECTOR(54), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	PAGANI_COLLECTOR(55), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	PLYMOUTH_COLLECTOR(56), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	PONTIAC_COLLECTOR(57), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	PORSCHE_COLLECTOR(58), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	RENAULT_COLLECTOR(59), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	SCION_COLLECTOR(60), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	SHELBY_COLLECTOR(61), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	SUBARU_COLLECTOR(62), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	TOYOTA_COLLECTOR(63), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	VAUXHALL_COLLECTOR(64), //
	/**
	 * own [x] [car-brand](s) in your garage
	 */
	VOLKSWAGEN_COLLECTOR(65); //

	private int id;

	private AchievementType(int id) {
		this.id = id;
	}

	public Long getId() {
		return Integer.valueOf(id).longValue();
	}

	public static AchievementType valueOf(int value) {
		return Arrays.stream(values()).filter(legNo -> legNo.id == value).findFirst().get();
	}

}
