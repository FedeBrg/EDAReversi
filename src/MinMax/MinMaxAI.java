package MinMax;

import back.Board;
import back.Game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MinMaxAI {
    Game game;
    int color;

    public MinMaxAI(Game game,int color){
        this.game=game;
        this.color=color;
    }

    public void makeMove(){
        List<int[][]> moves=this.getMoves();
        Random rand=new Random();
        int n=rand.nextInt();
//        game.board.board=moves.get(1);
        game.board.board=minMax(moves, 1);
    }

    List<int[][]> getMoves(){
        List<int[][]> moves= new LinkedList<>();
        for(int i = 0;i<game.board.getSize(); i++){
            for (int j = 0; j < game.board.getSize(); j++) {
                int[][] aux = game.board.isValidMove(i,j,color);
                if(aux!=null)
                    moves.add(aux);
            }
        }
        return moves;
    }

    public int[][] minMax(List<int[][]> moves,int depth){
        Board auxBoard,board;
        board=new  Board();
        board.setBoard(moves.get(1));
        Boolean myTurn=false;
        for(int[][] move: moves){
            auxBoard=minMaxRec(move,depth-1, myTurn);
            if(auxBoard.score>board.score){
                board=auxBoard;
            }
        }

        return board.getBoard();
    }

    Board minMaxRec(int[][] lastMove,int depth,Boolean myTurn){
        Board board=new Board();
        board.setBoard(lastMove);
        if(depth==0){
            return  board;
        }
        List<int[][]> moves =getMoves();
        Board auxBoard;
        for(int[][] move: moves){
            auxBoard=minMaxRec(move,depth-1, myTurn);
            if(auxBoard.score>board.score && myTurn){ //poda max
                board=auxBoard;
            }
            else if(auxBoard.score<board.score && !myTurn){ //poda min
                board=auxBoard;
            }
        }



        return board;
    }

}