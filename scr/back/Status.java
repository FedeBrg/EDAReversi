package back;

public enum Status{
	
	BLACK(1),
	WHITE(2),
	FREE(0);
	
	private int color;

	Status(int col){
		this.color = col;
	}
	
	Status paint() {
		if(color == 1) {
			return Status.WHITE;
		}
		else if (color == 2) {
			return Status.BLACK;
		}
		else {
			return Status.FREE;
		}
	}
}