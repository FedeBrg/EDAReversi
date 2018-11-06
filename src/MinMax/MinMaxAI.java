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
import java.util.concurrent.TimeUnit;

public class MinMaxAI {
    public int color;
    int nodeNumber;
    int lastScore;

    public MinMaxAI(int color) {
        this.color = color;
    }

    public int[][] makeMove(Game game) {
        List<int[][]> moves = game.board.getMoves(game);
        int[][] toRet = minMax(moves, 6, game);
        game.board.setBoard(toRet);
        return toRet;
    }

    public int[][] minMax(List<int[][]> moves, int depth, Game game) {
        Board board;
        board = new Board(game.board.getSize());
        Boolean myTurn = true;
        Game current = new Game(game.board.getSize(), game.getWhoStart(),(game.useTime())?"tine":"depth", game.getLimit(), (game.getPodas()) ? "on" : "off");
        current.switchPlayer();
        boolean hasValue = false;
        StringBuilder DOT = new StringBuilder("graph ARBOL{\n"); //inicializo el DOT
        this.nodeNumber = 0;
        long time=System.nanoTime();
        Integer poda=null;
        Boolean podas=false;
        int maxTime=3;
        int currentNodeNumber = 0,score = 0, auxScore;
        for (int[][] move : moves) {
            DOT.append(currentNodeNumber).append("--"); //se conecta a

            nodeNumber += 1;

            auxScore = minMaxRec(move, depth - 1, !myTurn, current, DOT, poda,podas,maxTime,time);
            if (!hasValue) {
                board.setBoard(move);
                score = auxScore;
                hasValue = true;
                poda=score;

            } else if (auxScore > score) {
                board.setBoard(move);
                score = auxScore;
                poda=score;
            }

        }
        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(score).append("\"]\n");

        DOT.append("\n").append("}");

        StringSelection selection = new StringSelection(DOT.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        time=(System.nanoTime()-time);
        System.out.println(score);
        System.out.println(time/1000000000+"."+time%1000000000/1000000);
//        long second=1000000000;
//        while(System.nanoTime()-time>second){
//            System.out.println("entro");
//            try{
//            Thread.sleep(1);
//            }catch (java.lang.InterruptedException exception){
//
//            }
//
//        }
//        System.out.println(time/1000000000+"."+time%1000000000/1000000);
        return board.getBoard();
    }

    int minMaxRec(int[][] lastMove, int depth, Boolean myTurn, Game game, StringBuilder DOT, Integer poda,boolean podas,int maxTime,long startTime) {
        Game current = game;
        current.board.setBoard(lastMove);
        DOT.append(nodeNumber).append("\n");    //meconecte al anterior
        //CASOS BASE
        if (depth == 0) {
            this.lastScore = current.board.calculateScore(game);
            DOT.append(nodeNumber).append(" ").append("[label=\"").append(current.board.score).append("\"]\n");
            return current.board.score;
        }

        current.switchPlayer();

        List<int[][]> moves = current.board.getMoves(current);
        boolean hasValue = false;
        int currentNodeNumber = nodeNumber;
        int auxScore, score = 0;
        Integer podaLocal = null;

        if (moves.size() == 0){
            this.lastScore = current.board.calculateScore(game);
            current.board.score=-1000;
            DOT.append(nodeNumber).append(" ").append("[label=\"").append(current.board.score).append("\"]\n");
            return current.board.score;
        }
        for (int[][] move : moves) {
            DOT.append(currentNodeNumber).append("--"); //me conecto a

            nodeNumber += 1;

            auxScore = minMaxRec(move, depth - 1, !myTurn, current, DOT, podaLocal,podas,maxTime,startTime);

            if((startTime!=(-1) && ((System.nanoTime()-startTime)/1000000000)==maxTime))
                return score;
            if (!hasValue) {
                hasValue = true;
                score = auxScore;
                podaLocal = score;
                if (podas && poda != null) {
                    if (!myTurn && score <= poda) {
                        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(auxScore).append("\"]\n");
                        return score;
                    }
                    if (myTurn && score >= poda) {
                        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(auxScore).append("\"]\n");
                        return score;
                    }
                }
            }
            else {
                if (myTurn && auxScore > score) {
                    score = auxScore;
                    podaLocal = score;
                    if (podas && poda != null && score <= poda) {
                        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(auxScore).append("\"]\n");
                        return score;
                    }
                } else if (!myTurn && auxScore < score) {
                    score = auxScore;
                    podaLocal = score;
                    if (podas && poda != null && score >= poda) {
                        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(auxScore).append("\"]\n");
                        return score;
                    }
                }
            }
        }
        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(score).append("\"]\n");
        lastScore=game.board.score;
        return score;
    }

    public int getColor() {
        return color;
    }
}