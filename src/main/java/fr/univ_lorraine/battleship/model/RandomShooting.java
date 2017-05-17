package fr.univ_lorraine.battleship.model;

import java.util.List;
import java.util.Random;

/**
 * Stratégie de tir aléatoire.
 */
public class RandomShooting implements ShootingStrategy {

	/**
	 * Id pour la serialization.
	 * @serial
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Générateur aléatoire pour générer la position du tir.
	 */
    private static final Random RNG = new Random();
    
	@Override
	public ShootingStrategyName getShootingStrategyName() {
		return ShootingStrategyName.RANDOM;
	}
    
	@Override
	public Position playShoot(Sea sea) {
		List<Position> possibleShots = sea.getAllNormalPositions();
		int randomIndex = RNG.nextInt(possibleShots.size());
		return possibleShots.get(randomIndex);
	}
	
}
