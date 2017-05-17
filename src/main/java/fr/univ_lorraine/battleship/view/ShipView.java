package fr.univ_lorraine.battleship.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import fr.univ_lorraine.battleship.model.Ship;



/**
 * Vue d'un bateau.
 * N'hérite pas de JComponent afin de pouvoir être dessiné à la position souhaitée sur la grille.
 * 
 * Observer de Ship.
 */
public class ShipView implements Observer {
	
	/**
	 * Le bateau modèle de cette vue.
	 */
	protected Ship ship;
	
	/**
	 * L'image du bateau.
	 */
	protected BufferedImage shipImage;
	
	/**
	 * Booléen indiquant si le bateau doit être visible seulement quand le bateau est détruit.
	 */
	protected boolean shipOnlyVisibleWhenDead;
	
	/**
	 * Construit la vue d'un bateau
	 * en prenant son image selon son époque et ses caractéristiques
	 * et se place en tant qu'observer du bateau.
	 * @param ship Le bateau modèle.
	 */
	public ShipView(Ship ship) {
		this.ship = ship;
		ship.addObserver(this);
		attachImage();
		this.shipOnlyVisibleWhenDead = false;
	}
	
	/**
	 * Prend l'image du bateau correspondant à son époque et ses caractéristiques.
	 * Place l'image à null si le bateau ne doit pas être affiché.
	 */
	protected void attachImage() {
		if (ship == null || (shipOnlyVisibleWhenDead && !ship.isDead()) ) {
			shipImage = null;
			return;
		}
		ImageFactory imgFac = ImageFactory.getInstance();
		switch(ship.getEpoch().getEpochName()) {
			case X_SIECLE:
				switch(ship.getSize()) {
				case 5: 
					if (ship.isDead()) {
						shipImage = imgFac.getShip5RedImage();
					}
					else {
						shipImage = imgFac.getShip5Image();
					}
					break;
				case 4: 
					if (ship.isDead()) {
						shipImage = imgFac.getShip4RedImage();
					}
					else {
						shipImage = imgFac.getShip4Image();
					}
					break;
				case 3: 
					if (ship.isDead()) {
						shipImage = imgFac.getShip3RedImage();
					}
					else {
						shipImage = imgFac.getShip3Image();
					}
					break;
				case 2: 
					if (ship.isDead()) {
						shipImage = imgFac.getShip2RedImage();
					}
					else {
						shipImage = imgFac.getShip2Image();
					}
					break;
				default:
			}
			break;
			case XVI_SIECLE:
				switch(ship.getSize()) {
					case 5: 
						if (ship.isDead()) {
							shipImage = imgFac.getOldShip5RedImage();
						}
						else {
							shipImage = imgFac.getOldShip5Image();
						}
						break;
					case 4: 
						if (ship.isDead()) {
							shipImage = imgFac.getOldShip4RedImage();
						}
						else {
							shipImage = imgFac.getOldShip4Image();
						}
						break;
					case 3: 
						if (ship.isDead()) {
							shipImage = imgFac.getOldShip3RedImage();
						}
						else {
							shipImage = imgFac.getOldShip3Image();
						}
						break;
					case 2: 
						if (ship.isDead()) {
							shipImage = imgFac.getOldShip2RedImage();
						}
						else {
							shipImage = imgFac.getOldShip2Image();
						}
						break;
					default:
				}
				break;
			case XX_SIECLE:
				switch(ship.getSize()) {
					case 5: 
						if (ship.isDead()) {
							shipImage = imgFac.getShipCarrierRedImage();
						}
						else {
							shipImage = imgFac.getShipCarrierImage();
						}
						break;
					case 4: 
						if (ship.isDead()) {
							shipImage = imgFac.getShipBattleshipRedImage();
						}
						else {
							shipImage = imgFac.getShipBattleshipImage();
						}
						break;
					case 3: 
						if (ship.isDead()) {
							shipImage = imgFac.getShipSubmarineRedImage();
						}
						else {
							shipImage = imgFac.getShipSubmarineImage();
						}
						break;
					case 2: 
						if (ship.isDead()) {
							shipImage = imgFac.getShipPtBoatRedImage();
						}
						else {
							shipImage = imgFac.getShipPtBoatImage();
						}
						break;
					default:
				}
				break;
			
			default:
				throw new AssertionError("Epoque inconnu " + ship.getEpoch().getEpochName());
		}
	}
	
	/**
	 * Change la valeur du booléen indiquant
	 * si le bateau doit être visible seulement quand le bateau est détruit.
	 * @param b Le booléen indiquant si le bateau doit être visible seulement quand le bateau est détruit.
	 */
	public void setShipVisibleOnlyWhenDead(boolean b) {
		shipOnlyVisibleWhenDead = b;
		attachImage();
	}
	
	/**
	 * Dessine le bateau.
     * @param g L'objet Graphics.
	 * @param seaTileViewSize La taille d'une case de la grille.
	 */
	public void draw(Graphics g, int seaTileViewSize) {
		if (shipImage != null) {
			int xPos = ship.getPosition().getX() * seaTileViewSize;
			int yPos = ship.getPosition().getY() * seaTileViewSize;
			Graphics2D g2d = (Graphics2D) g;
			// Si il faut faire une rotation
			if (ship.getOrientation() == Ship.Orientation.VERTICAL) {
			    double sin = Math.abs(Math.sin(Math.toRadians(90))), cos = Math.abs(Math.cos(Math.toRadians(90)));
			    int w = shipImage.getWidth(), h = shipImage.getHeight();
			    int neww = (int)Math.floor(w*cos+h*sin), newh = (int) Math.floor(h * cos + w * sin);
		        AffineTransform old = g2d.getTransform();
		        g2d.translate(xPos+2, yPos);
		        g2d.translate((neww - w) / 2, (newh - h) / 2);
		        g2d.rotate(Math.toRadians(90), w / 2, h / 2);
				g2d.drawRenderedImage(shipImage, null);
				g2d.setTransform(old);
			}
			else
				g2d.drawImage(shipImage, xPos, yPos, null);
		}
	}

	/**
	 * Met à jour l'image du bateau.
	 */
	@Override
	public void update(Observable o, Object arg) {
		attachImage();
	}
	
}
