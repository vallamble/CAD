package fr.univ_lorraine.battleship.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.univ_lorraine.battleship.model.ComputerController;
import fr.univ_lorraine.battleship.model.Epoch;
import fr.univ_lorraine.battleship.model.EpochXX;
import fr.univ_lorraine.battleship.model.Player;
import fr.univ_lorraine.battleship.model.Sea;

public class TestComputerController {

	private ComputerController computerController;
		
	private Player computer;
	
	@Before
	public void setUp() {
		Epoch epoch = new EpochXX();
		computer = new Player(new Sea(epoch), new Sea(epoch));
		computer.getSelfGrid().putNextShipToPlace();
		computer.getOpponentGrid().putNextShipToPlace();
		computerController = new ComputerController(computer);
	}

	@Test
	public void testPlaceAllShips() {
		computerController.placeAllShips();
		assertTrue("Les bateaux devraient tous être placés", computer.getSelfGrid().areShipsAllPlaced());
	}
	
	/**
	 * On place d'abord tous les bateaux en se servant de placeAllShips pour les deux joueurs
	 * (donc en utilisant deux computerController) puis on effectue un tir et on vérifie
	 * que le tir a bien été effectué.
	 */
	@Test
	public void testPlayShoot() {
		computerController.placeAllShips();
		Player opponent = new Player(computer.getOpponentGrid(), computer.getSelfGrid());
		ComputerController simulatedOpponent = new ComputerController(opponent);
		simulatedOpponent.placeAllShips();
		
		int nbOfNormalTilesBefore = computer.getOpponentGrid().getAllNormalPositions().size();
		computerController.playShoot();
		int nbOfNormalTilesAfter = computer.getOpponentGrid().getAllNormalPositions().size();
		assertTrue("Un tir devrait avoir été effectué", nbOfNormalTilesBefore - 1 == nbOfNormalTilesAfter);
	}

}
