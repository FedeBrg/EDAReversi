package MinMax;

import back.Board;
import back.Game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MinMaxAI {
    int color;

    public MinMaxAI(int color){
        this.color=color;
    }

    public int[][] makeMove(Game game){
        List<int[][]> moves = game.board.getMoves(game, color);
        Random rand=new Random();
        int n=rand.nextInt();
//        game.board.board=moves.get(1);
        int[][] toRet = minMax(moves, 1, game);
        game.board.board = toRet;
        return toRet;
    }

    public int[][] minMax(List<int[][]> moves,int depth, Game game){
        Board auxBoard,board;
        board=new  Board();
        board.setBoard(moves.get(1));
        Boolean myTurn=false;
        for(int[][] move: moves){
            auxBoard=minMaxRec(move,depth-1, myTurn, game);
            if(auxBoard.score>board.score){
                board=auxBoard;
            }
        }

        return board.getBoard();
    }

    Board minMaxRec(int[][] lastMove,int depth,Boolean myTurn, Game game){
        Board board=new Board();
        board.setBoard(lastMove);
        if(depth==0){

            return  board;
        }
        List<int[][]> moves = game.board.getMoves(game, color);
        Board auxBoard;
        for(int[][] move: moves){
            auxBoard=minMaxRec(move,depth-1, myTurn, game);
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