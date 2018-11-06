package back;

import java.io.Serializable;

public class Player implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int score;
	public int colour;
	
	Player(int colour){
		this.score = 2;
		this.colour = colour;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}

	public int getColour() {
		return colour;
	}
}