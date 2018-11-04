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
    int lastScore;

    public MinMaxAI(int color){
        this.color=color;
    }

    public int[][] makeMove(Game game){
        List<int[][]> moves = game.board.getMoves(game);
        int[][] toRet = minMax(moves, 3, game);
        game.board.setBoard(toRet);
        return toRet;
    }

    public int[][] minMax(List<int[][]> moves,int depth, Game game){
        Board board;
        int score=0,auxScore;
        board=new  Board(8); // CAMBIAR
        board.setBoard(moves.get(1));
        Boolean myTurn=true;
        Game current=new Game(game.getCurrent(),game.getPodas(),game.getGameMode(),game.getLimit());
        current.switchPlayer();
        boolean hasValue=false;
        StringBuilder DOT= new StringBuilder("graph ARBOL{\n"); //inicializo el DOT
        this.nodeNumber=0;
        int currentNodeNumber=0;
        for(int[][] move: moves){
            DOT.append(currentNodeNumber).append("--"); //se conecta a

            nodeNumber+=1;

            auxScore=minMaxRec(move,depth-1, !myTurn, current,DOT);
            if(!hasValue){
                board.setBoard(move);
                score=auxScore;
                hasValue=true;
            }
            else if(auxScore>score){
                board.setBoard(move);
                score=auxScore;
            }

        }
        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(score).append("\"]\n");

        DOT.append("\n").append("}");

        StringSelection selection = new StringSelection(DOT.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

        return board.getBoard();
    }

    int minMaxRec(int[][] lastMove,int depth,Boolean myTurn, Game game,StringBuilder DOT){
        Game current=new Game(game.getCurrent(),game.getPodas(),game.getGameMode(),game.getLimit());
        current.board.setBoard(lastMove);
        DOT.append(nodeNumber).append("\n");    //meconecte al anterior
        current.switchPlayer();
        if(depth==0){
            this.lastScore=current.board.calculateScore(color);
            DOT.append(nodeNumber).append(" ").append("[label=\"").append(current.board.score).append("\"]\n");
            return  current.board.score;
        }

        List<int[][]> moves = current.board.getMoves(current);
        boolean hasValue=false;
        int currentNodeNumber=nodeNumber;
        int auxScore,score=0;
        for(int[][] move: moves){
            DOT.append(currentNodeNumber).append("--"); //me conecto a

            nodeNumber+=1;

            auxScore=minMaxRec(move,depth-1, !myTurn, current,DOT);

            if(!hasValue){
                hasValue=true;
                score=auxScore;
            }
            else {
                if (myTurn && auxScore > score) { //poda max
                    score=auxScore;
                } else if (!myTurn && auxScore < score) { //poda min
                    score= auxScore;
                }
            }
        }
        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(score).append("\"]\n");
        lastScore=game.board.score;
        return score;
    }

}