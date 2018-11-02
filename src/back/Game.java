package back;
import java.util.Deque;
import java.util.LinkedList;

public class Game {
		private Player p1;
		private Player p2;
		
		public Board board;
		private Deque<UndoNode> undoStack;
		public Player current;
		private boolean podas;
		private int gameMode;
		private int limit;
		public boolean isComputerPlaying;
		
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
			this.isComputerPlaying = gameMode != 0;
			this.gameMode = gameMode;
			this.limit = limit;
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