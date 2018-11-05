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
        int[][] toRet = minMax(moves, 3, game);
        game.board.setBoard(toRet);
        return toRet;
    }
//(game.useTime())?"tine":"depth"
    public int[][] minMax(List<int[][]> moves, int depth, Game game) {
        Board board;
        board = new Board(game.board.getSize());
        Boolean myTurn = true;
        Game current = new Game(game.board.getSize(), game.getWhoStart(),game.getAiType() , game.getLimit(), (game.getPodas()) ? "on" : "off");
        current.switchPlayer();
        boolean hasValue = false;
        StringBuilder DOT = new StringBuilder("graph ARBOL{\n"); //inicializo el DOT
        this.nodeNumber = 0;
        long time=System.nanoTime();
        Integer poda=null;
        int currentNodeNumber = 0,score = 0, auxScore;
        for (int[][] move : moves) {
            DOT.append(currentNodeNumber).append("--"); //se conecta a

            nodeNumber += 1;

            auxScore = minMaxRec(move, depth - 1, !myTurn, current, DOT, poda);
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
        System.out.println(time/1000000000+"."+time%1000000000/1000000);

        return board.getBoard();
    }

    int minMaxRec(int[][] lastMove, int depth, Boolean myTurn, Game game, StringBuilder DOT, Integer poda) {
        Game current = game;
        current.board.setBoard(lastMove);
        DOT.append(nodeNumber).append("\n");    //meconecte al anterior
        current.switchPlayer();
        if (depth == 0) {
            this.lastScore = current.board.calculateScore(color);
            DOT.append(nodeNumber).append(" ").append("[label=\"").append(current.board.score).append("\"]\n");
            return current.board.score;
        }

        List<int[][]> moves = current.board.getMoves(current);
        boolean hasValue = false;
        int currentNodeNumber = nodeNumber;
        int auxScore, score = 0;
        Integer podaLocal = null;
        if (moves.size() == 0){
            this.lastScore = current.board.calculateScore(color);
            DOT.append(nodeNumber).append(" ").append("[label=\"").append(current.board.score).append("\"]\n");
            return current.board.score;
        }
        for (int[][] move : moves) {
            DOT.append(currentNodeNumber).append("--"); //me conecto a

            nodeNumber += 1;

            auxScore = minMaxRec(move, depth - 1, !myTurn, current, DOT, podaLocal);

            if (!hasValue) {
                hasValue = true;
                score = auxScore;
                podaLocal = score;
                if (poda != null) {
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
                    if(poda!=null) {
                        if (score <= poda) {
                            DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(auxScore).append("\"]\n");
                            return score;
                        }
                    }
                } else if(poda!=null){
                    if (!myTurn && auxScore < score) {
                        score = auxScore;
                        podaLocal = score;
                        if (score >= poda) {
                            DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(auxScore).append("\"]\n");
                            return score;
                        }
                    }
                }

                DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(score).append("\"]\n");
                lastScore = game.board.score;
                return score;
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