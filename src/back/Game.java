package back;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import MinMax.MinMaxAI;

public class Game implements Serializable{
//  ---------- INSTANCE VARIABLES ---------- //
		public Player p1;
		public Player p2;
		public Board board;
		public MinMaxAI ai;
		private static final long serialVersionUID = 1L;
		private Deque<UndoNode> undoStack;
		private Player current;
		private boolean prune;
		private boolean aiType; // aiType = true if aiType = "time"
		private int limit;
		private int totalPieces = 4;
		private boolean gameHasStarted;
		private int whoStart;
//  ---------- END OF INSTANCE VARIABLES ---------- //
		
//  ---------- AUXILIAR CLASS ---------- //
		public class UndoNode implements Serializable {
			private static final long serialVersionUID = 1L;
			int[][] board;
			Player current;
			int totalPieces;
			
			public UndoNode(int[][] board, Player current, int totalPieces){
				this.board = board;
				this.current = current;
				this.totalPieces = totalPieces;
			}
		}
//  ---------- END OF AUXILIAR CLASS ---------- //
		
//  ---------- CLASS CONSTRUCTORS ---------- //
		public Game(int size, int whoStart, String aiType, int limit, String podas){
			this.p1 = new Player(1);
			this.p2 = new Player(2);
			this.current = p1;
			this.board = new Board(size);
			this.undoStack = new LinkedList<UndoNode>();
			this.limit = limit;
			this.gameHasStarted = false;
			this.whoStart = whoStart;
			
			setPodas(podas);
			setAiType(aiType);
			setWhoStart(whoStart);
		}
		
		public Game(int whoStart, String aiType, int limit, String podas, String filename) throws ClassNotFoundException, IOException {
			readObject(this, filename, whoStart, aiType, limit, podas);
		}
//  ---------- END OF CLASS CONSTRUCTORS ---------- //	
		
//  ---------- SERIALIZATION METHODS ---------- //
		public final void writeObject(Game game, String str) throws IOException {
			FileOutputStream fileOut = new FileOutputStream(str);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			out.writeObject(p1);
			out.writeObject(p2);
			out.writeObject(board);
			out.writeObject(undoStack);
			out.writeObject(current);
			out.writeInt(totalPieces);
			out.writeBoolean(gameHasStarted);
			
			out.close();
			fileOut.close();		
		}
		
		@SuppressWarnings("unchecked")
		public final void readObject(Game game, String filename, int whoStart, String aiType, int limit, String podas) {
		      try {
		    	  	FileInputStream fileInputStream = new FileInputStream(filename);
					BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
					ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
					
					Player player1 = (Player) objectInputStream.readObject();
					Player player2 = (Player) objectInputStream.readObject();
					Board board = (Board) objectInputStream.readObject();
					Deque<UndoNode> undoStack = (Deque<UndoNode>) objectInputStream.readObject();
					Player current = (Player) objectInputStream.readObject();
					int pieces = (int) objectInputStream.readInt();
					boolean gameHasStarted = objectInputStream.readBoolean();
					
					game.p1 = player1;
					game.p2 = player2;
					game.setLimit(limit);
					game.setPodas(podas);
					game.setAiType(aiType);
					game.undoStack = undoStack;
					game.board = board;
					game.current = current;
					game.totalPieces = pieces;
					game.gameHasStarted = gameHasStarted;
					game.setWhoStart(whoStart);
					objectInputStream.close();
		      } catch (IOException i) {
		         return;
		      } catch (ClassNotFoundException c) {
		         return;
		      }
		}
//  ---------- END OF SERIALIZATION METHODS ---------- //
		
//  ---------- AUXILIAR FUNCTIONS ---------- //
		public void incrementPieces() {
			this.totalPieces += 1;
		}
		
		public int getPieces() {
			return totalPieces;
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
		
		public void startGame() {
			this.gameHasStarted = true;
		}
		
		public void pushToStack(int[][] matrix) {
			this.undoStack.push(new UndoNode(matrix, this.current, this.totalPieces));
		}
		
		public void undo() {
			if(undoStack.isEmpty()) {
				return;
			}

			UndoNode toReplace = undoStack.pop();
			
			while(toReplace.current != this.current) {
				if(undoStack.isEmpty()) {
					this.board.getMatrix(board.getSize());
					this.totalPieces = 4;
					this.current = p1;
				}
				
				else {
					toReplace = undoStack.pop();
				}
			}
			
			this.board.setBoard(toReplace.board);
			this.totalPieces = toReplace.totalPieces;
			this.current = toReplace.current;
		}	
		
		public void printboard(int[][] b) {
			for(int i = 0; i < board.getSize(); i++) {
				for(int j = 0; j < board.getSize(); j++) {
					System.out.print(b[i][j]);
				}
				System.out.println("\n");
			}
		}
		
		
		
		
		public void setPodas(String podas) {
			this.prune = (podas.equals("on"))? true:false;
		}
		
		public void setLimit(int limit) {
			this.limit = limit;
		}
		
		public void setWhoStart(int whoStart) {
			if(whoStart == 1) {
				this.ai = new MinMaxAI(p1.colour);
			}
			
			else if(whoStart == 2) {
				this.ai = new MinMaxAI(p2.colour);
			}
			
			else {
				this.ai = null;
			}
		}
		
		public void setAiType(String aiType) {
			this.aiType = (aiType.equals("time"))? true:false;
		}
		
		public int enemyColor() {
			if(current.colour == 1) {
				return 2;
			}
			
			return 1;
		}
		
		public int[][] computerTurn(Game game){
			int[][] toRet = ai.makeMove(game);
			return toRet;
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


	public boolean getPrune() {
		return prune;
	}

	public int getLimit() {
		return limit;
	}
	
	public int getWhoStart() {
		return whoStart;
	}
	
	public boolean useTime() {
		return aiType;
	}
	
	public boolean gameHasStarted() {
		return gameHasStarted;
	}
	
	public boolean isBoardFull() {
		int total = board.getSize() * board.getSize();
		return totalPieces == total;
	}

	
	public boolean isComputerPlaying() {
		return ai != null;
	}

	public void setCurrent(int current){
			this.current=new Player(current);
	}

//  ---------- END OF AUXILIAR FUNCTIONS ---------- //
}