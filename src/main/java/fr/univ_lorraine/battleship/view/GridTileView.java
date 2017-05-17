package fr.univ_lorraine.battleship.view;


import java.awt.Graphics;

import javax.swing.JComponent;

import fr.univ_lorraine.battleship.model.Sea;
import fr.univ_lorraine.battleship.model.Game.PlayerId;
import fr.univ_lorraine.battleship.model.Sea.SeaTileState;
import fr.univ_lorraine.battleship.view.graphics.Animation;
import fr.univ_lorraine.battleship.view.graphics.AnimationWithCallback;
import fr.univ_lorraine.battleship.view.graphics.AnimationWithCallback.Callback;



/**
 * Vue d'une case de la grille.
 * Permet d'afficher l'état de la case (si touché ou manqué).
 */
@SuppressWarnings("serial")
public class GridTileView extends JComponent {
		
	/**
	 * L'état de cette case de la grille.
	 */
	private Sea.SeaTileState state;
	
	/**
	 * Le joueur propriétaire de la grille dont cette case provient.
	 */
	private PlayerId playerOwner;
	
	/**
	 * L'image à dessiner pour cette case.
	 */
	private Animation tileImage;
	
	/**
	 * Booléen indiquant si la souris est au-dessus de la case.
	 */
	private boolean isHover;
	
	/**
	 * Indique si le viseur peut-être affiché si une case est hover.
	 */
	private boolean canDisplayHoverImage;
	
	/**
	 * Callback appelé à la fin de l'animation de tir sur cette case.
	 */
	private Callback animationCallback;
	
	public GridTileView(PlayerId playerOwner, SeaTileState state, Callback animationCallback) {
		this.playerOwner = playerOwner;
		this.state = state;
		this.animationCallback = animationCallback;
		this.isHover = false;
		this.canDisplayHoverImage = false;
		attachImage();
	}
	
	/**
	 * Retourne le joueur propriétaire de la grille dont cette case provient.
	 * @return Le joueur propriétaire de la grille dont cette case provient.
	 */
	public PlayerId getPlayerOwner() {
		return playerOwner;
	}
	
	/**
	 * Prend l'image correspondante à l'état de la case.
	 * Place l'image à null si rien ne doit pas être affiché.
	 */
	private void attachImage() {
        switch(state) {
	    	case NORMAL:
	    		tileImage = null;
	    		break;
	    	case TOUCHED:
	    		tileImage = new AnimationWithCallback(
	    				new Animation(ImageFactory.getInstance().getHitAnimation()), 
	    				animationCallback);
	    		break;
	    	case SHOT:
	    		tileImage = new AnimationWithCallback(
	    				new Animation(ImageFactory.getInstance().getMissAnimation()),
	    				animationCallback);
	    		break;
	    	default:
	    		throw new AssertionError("Etat inconnnu " + state);
        }
	}
	
	/**
	 * Met à jour l'état de la case
	 * et change l'image en conséquence.
	 * @param state Le nouvel état de la case.
	 */
	public void setState(SeaTileState state) {
		// Si l'état de la case a changé
		if (this.state != state) {
			this.state = state;
			attachImage();
		}
	}
	
	/**
	 * Place le booléen indiquant si la souris est au-dessus de la case.
	 * @param isHover Le booléen indiquant si la souris est au-dessus de la case.
	 */
	public void setIsHover(boolean isHover) {
		this.isHover = isHover;
	}

	/**
	 * Place le booléen indiquant si le viseur peut-être affiché si une case est hover.
	 * @param displayHoverImage Le booléen indiquant si le viseur peut-être affiché si une case est hover.
	 */
	public void setCanDisplayHoverImage(boolean displayHoverImage) {
		this.canDisplayHoverImage = displayHoverImage;
	}
	
	/**
	 * Dessine l'état de la case si nécessaire
	 * ainsi qu'un viseur si hover et un tir est possible. 
	 */
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    	g.setClip(-100, -100, 200, 200);	// pour pouvoir dessiner en dehors des limites du composant
        if (tileImage != null) {
        	int shift = 25;
        	tileImage.draw(g, -shift, -shift);
        }
        // Si la souris est hover et que les conditions d'affichage sont remplies
        if (canDisplayHoverImage && isHover && state == SeaTileState.NORMAL) {
        	g.drawImage(ImageFactory.getInstance().getSightImage(), 0, 0, this);	// on dessine un viseur
        }
	}

	/**
	 * Termine les animations qui ne bouclent pas.
	 */
	public void setNonLoopingAnimationsToEnd() {
		// Termine l'animation de tir de cette case s'il y en a une
		if (tileImage != null) {
			tileImage.end();
		}
	}
	
}
