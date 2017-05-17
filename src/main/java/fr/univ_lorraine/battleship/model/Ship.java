package fr.univ_lorraine.battleship.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Classe représentant un bateau.
 */
public class Ship extends Observable implements Serializable {
	
	/**
	 * Id pour la serialization.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Position du bateau sur la grille du jeu.
	 */
	private Position position;
	
	/**
	 * Longueur du bateau.
	 */
	private int size;
	
	/**
	 * Orientations possibles d'un bateau.
	 */
	public enum Orientation { HORIZONTAL, VERTICAL }
	
	/**
	 * L'orientation du bateau.
	 */
	private Orientation orientation;

	/**
	 * L'état du bateau, à vrai si il est détruit.
	 */
	private boolean dead;
	
	/**
	 * Les dégâts subis par le bateau ainsi que leurs positions.
	 */
	private boolean[] hits;
	
	/**
	 * L'époque du bateau.
	 */
	private Epoch epoch;
	
	/**
	 * Construit un bateau d'une certaine longueur appartenant à une certaine époque.
	 * @param size Longueur.
	 * @param epoch Epoque.
	 */
	public Ship(int size, Epoch epoch) {
		this.size = size;
		this.orientation = Orientation.HORIZONTAL;
		this.epoch = epoch;
		this.hits = new boolean[size];
	}
	
	/**
	 * Retourne la longueur du bateau.
	 * @return La longueur du bateau.
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Retourne la position du bateau.
	 * @return La position du bateau.
	 */
	public Position getPosition() {
		return this.position;
	}
	
	/**
	 * Positionne le bateau.
	 * @param x L'abscisse.
	 * @param y L'ordonnée.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
	
	/**
	 * Retourne l'orientation du bateau.
	 * @return L'orientation du bateau.
	 */
	public Orientation getOrientation() {
		return orientation;
	}

	
	/**
	 * Met l'orientation du bateau à la valeur passée en paramètre
	 * @param orientation La nouvelle orientaion du bateau.
	 */
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	/**
	 * Change l'orientation du bateau
	 */
	public void changeOrientation() {
		switch(orientation) {
			case HORIZONTAL:
				orientation = Orientation.VERTICAL;
				break;
			case VERTICAL:
				orientation = Orientation.HORIZONTAL;
				break;
			default:
				throw new AssertionError("Orientation inconnu " + orientation);
		}
	}
	
	/**
	 * Renvoie vrai si le bateau est détruit.
	 * @return Vrai si le bateau est détruit, faux sinon.
	 */
	public boolean isDead() {
		return dead;
	}
	
	/**
	 * Retourne l'époque du bateau.
	 * @return L'époque du bateau.
	 */
	public Epoch getEpoch() {
		return epoch;
	}
	
	/**
	 * Compte le nombre de tirs qui ont touchés le bateau.
	 * @return Le nombre de "touchés".
	 */
	public int getHitCount() {
		int count = 0;
		for (int i = 0 ; i < hits.length ; i++) {
			if (hits[i]){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Retourne les positions occupées par ce bateau sur la grille
	 * sous forme de tableau.
	 * @return Les positions occupées.
	 */
	public Position[] getSeaTilesOccupied() {
		if (position == null) {
			return new Position[0];
		}
		
		Position[] tilesOccupied = new Position[size];
		for (int i = 0 ; i < size ; i++) {
			switch(orientation) {
				case HORIZONTAL:
					tilesOccupied[i] = new Position(position.getX()+i, position.getY());
					break;
				case VERTICAL:
					tilesOccupied[i] = new Position(position.getX(), position.getY()+i);
					break;
				default:
					throw new AssertionError("Orientation inconnue " + orientation);
			}
		}
		return tilesOccupied;
	}
	
	/**
	 * Vérifie si le bateau est touché par le tir et agit en conséquence.
	 * @param shotPosition La position du tir.
	 * @return Vrai si le bateau est touché, faux sinon.
	 */
	public boolean checkShot(Position shotPosition) {
		int i = 0;
		boolean touched = false;
		Position[] tilesOccupied = getSeaTilesOccupied();
		while (i < size && !touched) {
			touched = tilesOccupied[i].equals(shotPosition);
			i++;
		}
		if(touched) {			// Si le bateau est touché,
			hits[i-1] = true;	// on enregistre les dégâts
			if (!dead) {		// et si le bateau n'est pas déjà détruit,
				this.dead = epoch.takeDamage(size, getHitCount());
			}					// on délègue la gestion de l'état du bateau à l'époque
		}
		setChanged();
		notifyObservers();
		return touched;
	}

	/** Retourne les positions touchées du bateau
	 * @return Positions touchées du bateau
	 */
	public ArrayList<Position> harmedPositions() {
		Position[] p = getSeaTilesOccupied();
		ArrayList<Position> harmedPositions = new ArrayList<>();

		for(int i = 0; i < size; i++) {
			if(hits[i]) harmedPositions.add(p[i]);
		}

		return harmedPositions;
	}
}
