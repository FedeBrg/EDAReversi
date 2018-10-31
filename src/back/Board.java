package back;

import java.util.List;

public class Board {
		
	public int[][] board = {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,1,2,0,0,0},
				{0,0,0,2,1,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
	public int score;
	private int size = 8;
	private final static int FREE = 0;
	private final static int BLACK = 1;
	private final static int WHITE = 2;
	
	private final int[][] directions = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,-1},{-1,1},{1,-1}};
	
	public int[][] isValidMove(int row, int col, int color) {
		
		boolean ret = false;		
		
		int [][] copy = copyMat();
		
		for(int i = 0; i<directions.length;i++) { 
			ret = ret || isValidMove(row+directions[i][0],col+directions[i][1],directions[i][0],directions[i][1],color, true,copy);
		}
		if(ret) {
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
	
	
	public static void main(String[] args) {
		Board b = new Board();
		System.out.println(b.isValidMove(2, 4,BLACK));
		for(int i = 0; i<8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.print(b.board[i][j]);
			}
			System.out.println();
		}
	}

	public int getSize() {
		return size;
	}
}

