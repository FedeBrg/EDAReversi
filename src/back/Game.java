package back;
import java.util.Deque;
import java.util.LinkedList;

import MinMax.MinMaxAI;

public class Game {
		public Player p1;
		public Player p2;
		
		public Board board;
		public Deque<UndoNode> undoStack;
		public Player current;
		public boolean podas;
		private int gameMode;
		private int limit;
		public int totalPieces = 4;
		public MinMaxAI ai = null;



		public class UndoNode {
			int[][] board;
			Player current;
			int totalPieces;
			
			public UndoNode(int[][] board, Player current, int totalPieces){
				this.board = board;
				this.current = current;
				this.totalPieces = totalPieces;
			}
		}
		
		public void incrementPieces() {
			this.totalPieces += 1;
		}
		
		public int calculateP1Score(int[][] matrix) {
			int p1Score = 0;
			
			for(int i = 0; i < board.getSize(); i++) {
				for(int j = 0; j < board.getSize(); j++) {
					if(matrix[i][j] == 1) {
						p1Score += 1;
					}
				}
			}

			return p1Score;
		}
		
		public void pushToStack(int[][] matrix) {
			this.undoStack.push(new UndoNode(matrix, this.current, this.totalPieces));
		}
		
		public void undo() {
			if(undoStack.isEmpty()) {
				return;
			}
			
			UndoNode toReplace = undoStack.pop();
			
			if(ai != null) {
				while(toReplace.current != this.current) {
					if(undoStack.isEmpty()) {
						return;
					}
					
					toReplace = undoStack.pop();
				}
				
				this.board.setBoard(toReplace.board);
				this.current = toReplace.current;
				this.totalPieces = toReplace.totalPieces;
			}
			
			else {
				this.board.setBoard(toReplace.board);
				this.current = toReplace.current;
				this.totalPieces = toReplace.totalPieces;
			}	
		}
		
		public void printboard(int[][] b) {
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					System.out.print(b[i][j]);
				}
				System.out.println("\n");
			}
		}
		
		
		public Game(int whoStart, boolean podas, int gameMode, int limit){
			this.p1 = new Player(1);
			this.p2 = new Player(2);
			switch (whoStart){
				default:
				case 1:
					this.current=p1;
					break;
				case 2:
					this.current=p2;
					break;
			}
			this.board = new Board(8);   // CAMBIAR
			this.undoStack = new LinkedList<UndoNode>();
			this.podas = podas;
			if(gameMode != 0) {
				this.ai = new MinMaxAI(p2.colour);
			}
			
			this.gameMode = gameMode;
			this.limit = limit;
		}
		
		public int enemyColor() {
			if(current.colour == 1) {
				return 2;
			}
			
			return 1;
		}
		public int[][] computerTurn(Game game){
			return ai.makeMove(game);
		}
		
		public void switchPlayer() {
			if(current == p1) {
				current = p2;
			}
			
			else {
				current = p1;
			}
		}

		int getScore(){
			return board.score;
		}

		public int getCurrent() {
			return current.getColour();
		}


	public boolean getPodas() {
		return podas;
	}

	public int getGameMode() {
		return gameMode;
	}

	public int getLimit() {
		return limit;
	}
}