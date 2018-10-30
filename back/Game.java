package back;

import java.util.Deque;
import java.util.LinkedList;

public class Game {
	private Player p1;
	private Player p2;
	
	private Board board;
	private Deque<UndoNode> undoStack;
	private Player current;
	private boolean podas;
	private int gameMode;
	
	public class UndoNode {
		Board board;
		Player current;
		
		UndoNode(Board board, Player current){
			this.board = board;
			this.current = current;
		}
	}
	
	Game(Player p1, Player p2, Player current, boolean podas, int gameMode){
		this.board = new Board();
		this.undoStack = new LinkedList<UndoNode>();
		this.p1 = p1;
		this.p2 = p2;
		this.current = current;
		this.podas = podas;
		this.gameMode = gameMode;
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
}
