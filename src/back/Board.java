package back;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Board implements Serializable{
//  ---------- INSTANCE VARIABLES ---------- //
	public int[][] matrix;
	public int score;
	public Queue<Pair<Integer,Integer>> positions;
	public List<int[][]>moves;
	private static final int MARK = 13;
	private static final long serialVersionUID = 1L;
	private int[][] matrix4 = {{99,0,0,99},{0,1,1,0},{0,1,1,0},{99,0,0,99}};
	private int[][] matrix6 = {{99,-4,4,4,-4,99},{-4,-12,-2,-2,-12,-4},{4,-2,1,1,-2,4},{4,-2,1,1,-2,4},{-4,-12,-2,-2,-12,-4},{99,-4,4,4,-4,99}};
	private int[][] matrix8 = {{99,-8,8,6,6,8,-8,99},{-8,-24,-4,-3,-3,-4,-24,-8},{8,-4,7,4,4,7,-4,8},{6,-3,4,1,1,4,-3,6},{6,-3,4,1,1,4,-3,6},{8,-4,7,4,4,7,-4,8},{-8,-24,-4,-3,-3,-4,-24,-8},{99,-8,8,6,6,8,-8,99}};
	private int[][] matrix10 = {{200,-16,16,12,6,6,12,16,-16,200},{-16,-48,-8,-6,-3,-3,-6,-8,-48,-16},{16,-8,14,8,4,4,8,14,-8,16},{12,-6,8,7,2,2,7,8,-6,12},{6,-3,4,2,1,1,2,4,-3,6},{6,-3,4,2,1,1,2,4,-3,6},{12,-6,8,7,2,2,7,8,-6,12},{16,-8,14,8,4,4,8,14,-8,16},{-16,-48,-8,-6,-3,-3,-6,-8,-48,-16},{200,-16,16,12,6,6,12,16,-16,200}};
	private int[][] valueMatrix;
	private int[][] initialMatrix4 = {{0,0,0,0},{0,1,2,0},{0,2,1,0},{0,0,0,0}};
	private int[][] initialMatrix6 = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,1,2,0,0},{0,0,2,1,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
	private int[][] initialMatrix8 = {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,1,2,0,0,0},{0,0,0,2,1,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
	private int[][] initialMatrix10 = {{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,1,2,0,0,0,0},{0,0,0,0,2,1,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0}};
	private int size;
	private final int[][] directions = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,-1},{-1,1},{1,-1}};
	private final static int FREE = 0;
//  ---------- END OF INSTANCE VARIABLES ---------- //
	
//  ---------- CLASS CONSTRUCTOR ---------- //
	public Board(int size){
		this.size = size;
        this.score = 0;
		this.valueMatrix = getValueBoard(size);
		getMatrix(size);
	}
//  ---------- END OF CLASS CONSTRUCTOR ---------- //

//  ---------- AUXILIAR FUNCTIONS ---------- //
	public int[][] isValidMove(int row, int col, int color) {		
		if(matrix[row][col] != 0) {
			return null;
		}

		boolean ret = false;
		int [][] copy = copyMat();

		for(int i = 0; i < directions.length; i++) {
			boolean retAux = isValidMove(row+directions[i][0], col+directions[i][1], directions[i][0], directions[i][1], color, true, copy);
			ret = ret || retAux;
		}

		if(ret) {
			copy[row][col] = color;
			return copy;
		}
		
		return null;
	}

	public List<int[][]> getMoves(Game game){
        List<int[][]> moves= new LinkedList<>();
        this.positions=new LinkedList<>();
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                int[][] aux = isValidMove(i, j, game.getCurrent());
                if(aux != null) {
                	moves.add(aux);
                	this.positions.add(new Pair<>(i,j));
                }
            }
        }
        
        this.moves=moves;
        return moves;
    }
	
	public int[][] getAllAvailableMoves(Game game, int colour){
		int[][] toReturn = new int[size][size];
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(game.board.isValidMove(i, j, colour) != null) {
					toReturn[i][j] = MARK;
				}
			}
		}
		
		return toReturn;
	}

	public boolean hasAvailableMoves(int colour) {
		boolean ret = false;
		
		int [][] copy = copyMat();
		
		for(int row = 0; row < size && !ret; row++) {
			for(int col = 0 ; col < size && !ret; col ++) {
				for(int i = 0; i<directions.length && !ret;i++) {
					if(matrix[row][col]==0)
						ret = ret || isValidMove(row+directions[i][0], col+directions[i][1], directions[i][0], directions[i][1], colour, true, copy);
				}
			}
		}
		
		return ret;
	}

	private boolean isValidMove(int row, int col, int i, int j, int color, boolean first, int[][] mat) {

		if(outOfBounds(row, col) || mat[row][col] == FREE ) {
			return false;
		}

		if(mat[row][col] == color && first) {
			return false;
		}
		else if(mat[row][col] == color) {
			return true;
		}

		int prevColor = mat[row][col];
		mat[row][col] = color;

		boolean ret = isValidMove(row+i, col+j, i, j, color, false, mat);

		if(!ret) {
			mat[row][col] = prevColor;
		}

		return ret;

	}

	private int [][] copyMat() {
		int [][] mat = new int[size][size];
		
		for(int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				mat[i][j] = matrix[i][j];
			}
		}

		return mat;
	}

	private boolean outOfBounds(int row, int col) {
		return row<0 || row>=size || col<0 || col>=size;
	}

	public int getSize() {
		return size;
	}

	public void getMatrix(int size){
		switch (size){
			case 4:
				this.matrix = initialMatrix4;
				break;
			case 6:
				this.matrix = initialMatrix6;
				break;
			case 8:
				this.matrix = initialMatrix8;
				break;
			case 10:
				this.matrix = initialMatrix10;
				break;
			default:
				throw new IllegalArgumentException();
		}
	}

	private int[][] getValueBoard(int size){
		int[][] matrix;
		
		switch (size){
			default:
				throw new IllegalArgumentException("invalid size");
			case 4:
				matrix=matrix4;
				break;
			case 6:
				matrix=matrix6;
				break;
			case 8:
				matrix=matrix8;
				break;
			case 10:
				matrix=matrix10;
		}
		
		return matrix;
	}

	public void setBoard(int[][] board){
	    this.matrix=board;
	}

    public int[][] getBoard(){
	    return matrix;
    }

    public int calculatePlayerScore(int colour) {
    	int counter = 0;

    	for(int i = 0; i < size; i++) {
    		for(int j = 0; j < size; j++) {
    			if(matrix[i][j] == colour) {
    				counter++;
    			}
    		}
    	}

    	return counter;
    }

    public int calculateScore(Game game, int color){
		score=0;
		List<int[][]> moves = this.getMoves(game);
		int myPoints = 0, theirPoints = 0;
		int[][] board = matrix;
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(color == board[i][j]){
					score += valueMatrix[i][j];
					myPoints += 1;
				}
				
				else if(color != board[i][j] && board[i][j] != 0 ){
					score -= valueMatrix[i][j];
					theirPoints-=1;
				}
			}
		}
		
		if(game.getCurrent() == color) {
			score += moves.size();	
		}
		
		else {
			score -= moves.size();
		}
		
		if(moves.size() == 0) {
			return (color == game.getCurrent())? 1000 : -1000;
		}
			
		if(game.isBoardFull() && myPoints > theirPoints) {
			return 10000;
		}
			
		if(theirPoints == 0) {
			return score = 10000;
		}
			
		if(myPoints == 0) {
			return score = 10000;
		}
			
		return score;
	}
	
    public int getScore(){
		return score;
	}
//  ---------- END OF AUXILIAR FUNCTIONS ---------- //
}

