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
		public boolean cpu=false;
		public class UndoNode {
			Board board;
			Player current;
			
			UndoNode(Board board, Player current){
				this.board = board;
				this.current = current;
			}
		}
		
		public Game(int whoStart, boolean podas, int gameMode, int limit){
			this.board = new Board();
			this.undoStack = new LinkedList<UndoNode>();
			this.podas = podas;
			setPlayers(whoStart, gameMode, limit);
		}
		
		public void setPlayers(int whoStart, int gameMode, int limit) {
			if(whoStart == 0) {
				this.p1 = new Human(1);
				this.p2 = new Human(2);
				this.current = p1;
			}
			
			else if(whoStart == 1) {
				this.p1 = new Computer(1, gameMode, limit);
				this.p2 = new Human(2);
				this.current = p1;
			}
			
			else {
				this.p1 = new Human(1);
				this.p2 = new Computer(2, gameMode, limit);
				this.current = p1;
			}
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
		
		public void changePlayer() {
			if(current == p1) {
				current = p2;
			}
			
			else {
				current = p1;
			}
		}
	}