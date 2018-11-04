package back;

import java.util.LinkedList;
import java.util.List;

public class Board {

	private int[][] matrix4 = {{99,0,0,99},{0,1,1,0},{0,1,1,0},{99,0,0,99}};
	private int[][] matrix6 = {{99,-4,4,4,-4,99},{-4,-12,-2,-2,-12,-4},{4,-2,1,1,-2,4},{4,-2,1,1,-2,4},{-4,-12,-2,-2,-12,-4},{99,-4,4,4,-4,99}};
	private int[][] matrix8 = {{99,-8,8,6,6,8,-8,99},{-8,-24,-4,-3,-3,-4,-24,-8},{8,-4,7,4,4,7,-4,8},{6,-3,4,1,1,4,-3,6},{6,-3,4,1,1,4,-3,6},{8,-4,7,4,4,7,-4,8},{-8,-24,-4,-3,-3,-4,-24,-8},{99,-8,8,6,6,8,-8,99}};
	private int[][] matrix10 = {{99,-16,16,12,6,6,12,16,-16,99},{-16,-48,-8,-6,-3,-6,-8,-48,-16},{16,-8,14,8,4,4,8,14,-8,16},{12,-6,8,7,2,2,7,8,-6,12},{6,-3,4,2,1,1,2,4,-3,6},{6,-3,4,2,1,1,2,4,-3,6},{12,-6,8,7,2,2,7,8,-6,12},{16,-8,14,8,4,4,8,14,-8,16},{-16,-48,-8,-6,-3,-6,-8,-48,-16},{99,-16,16,12,6,6,12,16,-16,99}};
	private int[][] valueMatrix;


	private int[][] initialMatrix4 = {{0,0,0,0},{0,1,2,0},{0,2,1,0},{0,0,0,0}};
	private int[][] initialMatrix6 = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,1,2,0,0},{0,0,2,1,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
	private int[][] initialMatrix8 = {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,1,2,0,0,0},{0,0,0,2,1,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
	private int[][] initialMatrix10 = {{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,1,2,0,0,0},{0,0,0,0,2,1,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0}};

	public int[][] matrix;

	public int score;

	public List<int[][]>moves;
	private int size = 8;
	private final static int FREE = 0;
	private final static int BLACK = 1;
	private final static int WHITE = 2;


	private final int[][] directions = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,-1},{-1,1},{1,-1}};

	public Board(int size){
		this.size=size;
        this.score=0;
		this.valueMatrix=getValueBoard(size);
		this.matrix=getMatrix(size);
	}

	public int[][] isValidMove(int row, int col, int color) {
		
		if(matrix[row][col] != 0) {
			return null;
		}
	
		boolean ret = false;

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
				if(matrix[i][j] == 0) {
					counter++;
				}
			}
		}

		return counter == 0;
	}

	public List<int[][]> getMoves(Game game){
        List<int[][]> moves= new LinkedList<>();
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                int[][] aux = isValidMove(i, j, game.getCurrent());
                if(aux != null)
                    moves.add(aux);
            }
        }
        this.moves=moves;

        return moves;
    }

	public boolean hasAvailableMoves(int colour) {
		boolean ret = false;
		
		int [][] copy = copyMat();
		
		for(int row = 0; row < size &&!ret; row++) {
			for(int col = 0 ; col < size && !ret; col ++) {
				for(int i = 0; i<directions.length && !ret;i++) {
					ret = ret || isValidMove(row+directions[i][0],col+directions[i][1],directions[i][0],directions[i][1],colour, true,copy);
				}
			}
		}
	

		return ret;
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
				mat[i][j] = matrix[i][j];
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

	private int[][] getMatrix(int size){
		int[][] matriz;
		switch (size){
			default:
				throw new IllegalArgumentException();
			case 4:
				matriz=initialMatrix4;
				break;
			case 6:
				matriz=initialMatrix6;
				break;
			case 8:
				matriz=initialMatrix8;
				break;
			case 10:
				matriz=initialMatrix10;
		}
		return matriz;
	}

	private int[][] getValueBoard(int size){
		int[][] matriz;
		switch (size){
			default:
				throw new IllegalArgumentException("invalid size");
			case 4:
				matriz=matrix4;
				break;
			case 6:
				matriz=matrix6;
				break;
			case 8:
				matriz=matrix8;
				break;
			case 10:
				matriz=matrix10;
		}
		return matriz;
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

    public int calculateScore(int current){
		score=0;
		int i,j;
		int[][] board=matrix;
		for(i=0;i<this.size;i++) {
			for (j = 0; j < this.size; j++) {
				if(current==board[i][j]){
					this.score+=valueMatrix[i][j];
				}
				else if(board[i][j]!=current &&(board[i][j]==1 || board[i][j]==2 )){
					score-=valueMatrix[i][j];
				}

			}
		}
		return this.score;
	}
	public int getScore(){return score;}
}

