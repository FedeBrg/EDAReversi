package MinMax;

import back.Board;
import back.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MinMaxAI {
    int color;
    int nodeNumber;

    public MinMaxAI(int color){
        this.color=color;
    }

    public int[][] makeMove(Game game){
        List<int[][]> moves = game.board.getMoves(game);
        int[][] toRet = minMax(moves, 5, game);
        game.board.setBoard(toRet);
        return toRet;
    }
//DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(nodeNumber).append("\"]");

    public int[][] minMax(List<int[][]> moves,int depth, Game game){
        Board auxBoard,board;
        board=new  Board(8); // CAMBIAR
        board.setBoard(moves.get(1));
        Boolean myTurn=true;
        Game current=new Game(game.getCurrent(),game.getPodas(),game.getGameMode(),game.getLimit());
        current.switchPlayer();

        StringBuilder DOT= new StringBuilder("graph ARBOL{\n"); //inicializo el DOT
        this.nodeNumber=0;
        int currentNodeNumber=0;
        for(int[][] move: moves){
            DOT.append(currentNodeNumber).append("--"); //se conecta a

            nodeNumber+=1;

            auxBoard=minMaxRec(move,depth-1, !myTurn, current,DOT);
            if(auxBoard.score>board.score){
                board=auxBoard;
            }

        }


        DOT.append("\n").append("}");
//
//        StringSelection selection = new StringSelection(DOT.toString());
//        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//        clipboard.setContents(selection, selection);

        return board.getBoard();
    }

    Board minMaxRec(int[][] lastMove,int depth,Boolean myTurn, Game game,StringBuilder DOT){
        Game current=new Game(game.getCurrent(),game.getPodas(),game.getGameMode(),game.getLimit());
        current.board.setBoard(lastMove);
        DOT.append(nodeNumber).append("\n");    //meconecte al anterior
        if(depth==0){
            System.out.println("hola");
            current.board.calculateScore(game.getCurrent());
            DOT.append(nodeNumber).append(" ").append("[label=\"").append(current.board.score).append("\"]\n");
            return  current.board;
        }

        List<int[][]> moves = current.board.getMoves(current);

        Board auxBoard;
        current.board.calculateScore(current.getCurrent());
        int currentNodeNumber=nodeNumber;
        for(int[][] move: moves){
            DOT.append(currentNodeNumber).append("--"); //me conecto a

            nodeNumber+=1;

            auxBoard=minMaxRec(move,depth-1, !myTurn, current,DOT);
            if( myTurn && auxBoard.score>current.board.score ){ //poda max
                game.board=auxBoard;
            }
            else if( !myTurn && auxBoard.score<current.board.score ){ //poda min
                game.board=auxBoard;
            }
        }



        return game.board;
    }

}