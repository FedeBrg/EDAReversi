package back;

public class Player {
	
	public int score;
	public int colour;
	
	Player(int colour){
		this.score = 0;
		this.colour = colour;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}
}