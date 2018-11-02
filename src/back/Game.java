package back;
import java.util.Deque;
import java.util.LinkedList;

import MinMax.MinMaxAI;

public class Game {
		public Player p1;
		public Player p2;
		
		public Board board;
		private Deque<UndoNode> undoStack;
		public Player current;
		private boolean podas;
		private int gameMode;
		private int limit;
		public MinMaxAI ai = null;
		
		public class UndoNode {
			Board board;
			Player current;
			
			UndoNode(Board board, Player current){
				this.board = board;
				this.current = current;
			}
		}
		
		
		public Game(int whoStart, boolean podas, int gameMode, int limit){
			this.p1 = new Player(1);
			this.p2 = new Player(2);
			this.current = p1;
			this.board = new Board();
			this.undoStack = new LinkedList<UndoNode>();
			this.podas = podas;
			if(gameMode != 0) {
				this.ai = new MinMaxAI(p2.colour);
			}
			this.gameMode = gameMode;
			this.limit = limit;
		}
		
		public int[][] computerTurn(Game game){
			return ai.makeMove(game);
		}
		
		public void undo() {
			if(undoStack.isEmpty()) {
				return;
			}
			
			UndoNode toReplace = undoStack.pop();
			board = toReplace.board;
			current = toReplace.current;
		}
		
		public void pushToStack() {
			undoStack.push(new UndoNode(board, current));
		}
		
		public void switchPlayer() {
			if(current == p1) {
				current = p2;
			}
			
			else {
				current = p1;
			}
		}
	}