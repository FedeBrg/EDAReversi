package back;

import java.util.LinkedList;
import java.util.List;

public class Board {
		
	public int[][] board = {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,1,2,0,0,0},
				{0,0,0,2,1,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
	
	public int score;
	
	private int size = 8;
	public int numberOfPieces = 4;
	private final static int FREE = 0;
	private final static int BLACK = 1;
	private final static int WHITE = 2;
	
	
	private final int[][] directions = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,-1},{-1,1},{1,-1}};
	
	public int[][] isValidMove(int row, int col, int color) {
		
		boolean ret = false;
		if(board[row][col] != 0) {
			return null;
		}
		
		
		int [][] copy = copyMat();
		
		for(int i = 0; i<directions.length;i++) { 
			boolean retAux = isValidMove(row+directions[i][0],col+directions[i][1],directions[i][0],directions[i][1],color, true,copy);
			ret = ret || retAux;
		}
		
		if(ret) {
			copy[row][col] = color;

			return copy;
		}
		

		return null;
		
	}
	
	public boolean isBoardFull() {
		int counter = 0;
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(board[i][j] == 0) {
					counter++;
				}
			}
		}
		
		return counter == 0;
	}
	
	public List<int[][]> getMoves(Game game, int colour){
        List<int[][]> moves= new LinkedList<>();
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                int[][] aux = isValidMove(i, j, colour);
                if(aux != null)
                    moves.add(aux);
            }
        }
        
        return moves;
    }
	
	public int[][] hasAvailableMoves(int row, int col, int colour) {
		boolean ret = false;
		if(board[row][col] != 0) {
			return null;
		}
		
		int[][] copy = copyMat();
		
		for(int i = 0; i<directions.length;i++) { 
			ret = ret || isValidMove(row+directions[i][0],col+directions[i][1],directions[i][0],directions[i][1],colour, true,copy);
		}
		
		if(ret) {
			copy[row][col] = colour;

			return copy;
		}
		
		return null;
	}

	private boolean isValidMove(int row, int col, int i, int j, int color, boolean first, int[][] mat) {
		
		if(outOfBounds(row ,col) || mat[row][col] == 0 ) {
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
		
		boolean ret = isValidMove(row+i,col+j,i,j,color,false,mat);
		
		if(!ret) {
			mat[row][col] =prevColor;
		}
		
		return ret;
		
	}
	
	private int [][] copyMat() {
		int [][] mat = new int[8][8];
		for(int i = 0; i<8; i++) {
			for (int j = 0; j < 8; j++) {
				mat[i][j] = board[i][j];
			}
		}
		
		return mat;
	}
	
	private boolean outOfBounds(int row, int col) {
		return row<0 || row>7 || col<0 || col>7;
	}

	public int getSize() {
		return size;
	}

	public void setBoard(int[][] board){
	    this.board=board;
    }

    public int[][] getBoard(){
	    return board;
    }
    
    public int calculatePlayerScore(int colour) {
    	int counter = 0;
    	
    	for(int i = 0; i < size; i++) {
    		for(int j = 0; j < size; j++) {
    			if(board[i][j] == colour) {
    				counter++;
    			}
    		}
    	}
    	
    	return counter;
    }
		
}

