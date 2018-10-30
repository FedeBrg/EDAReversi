package back;

public class Computer extends Player{
	//pongo gameMode aca para hacer los metodos de Computer dependiendo de si esta en un modo o en el otro
	private int gameMode;
	private int limit;
	
	Computer(int colour, int gameMode, int limit){
		super(colour);
		this.gameMode = gameMode;
		this.limit = limit;
	}
}
