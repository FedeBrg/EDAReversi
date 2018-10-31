package back;

public abstract class Player {
	
	private int score;
	public int colour;
	
	Player(int colour){
		this.score = 0;
		this.colour = colour;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}