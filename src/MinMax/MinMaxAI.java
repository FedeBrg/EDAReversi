package MinMax;

import back.Board;
import back.Game;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.Serializable;
import java.util.List;

public class MinMaxAI implements Serializable {
//  ---------- INSTANCE VARIABLES ---------- //
	private static final long serialVersionUID = 1L;
	private static final long NANO=1000000000;
	public int color;
    int nodeNumber,lastScore,lastNode;
//  ---------- END OF INSTANCE VARIABLES ---------- //
    
//  ---------- CLASS CONSTRUCTOR ---------- //
    public MinMaxAI(int color) {
        this.color = color;
    }
//  ---------- END OF CLASS CONSTRUCTOR ---------- //

//  ---------- AUXILIAR METHODS ---------- //
    public int[][] makeMove(Game game) {
        List<int[][]> moves = game.board.getMoves(game);
        int[][] toRet;
        if(!game.useTime())
            toRet = minMaxDepth(moves, game);
        else{
            toRet = minMaxTime(moves,game);
        }

        return toRet;
    }


    private int[][] minMaxTime(List<int[][]> moves, Game game) {
        Board board;
        board = new Board(game.board.getSize());

        Game current = new Game(game.board.getSize(), game.getWhoStart(), (game.useTime()) ? "time" : "depth", game.getLimit(), (game.getPrune()) ? "on" : "off");
        current.switchPlayer();

        StringBuilder DOT =new StringBuilder(); //inicializo el DOT

        this.nodeNumber = 0;
        long time = System.nanoTime();
        Integer poda = null;
        int maxTime, currentNodeNumber = 0, score = 0, auxScore;
        maxTime = game.getLimit();
        boolean prune = game.getPrune(), hasValue = false, myTurn = true,outOfTime=false;

        for (int depth = 3;!outOfTime && (( System.nanoTime()-time) / NANO <= maxTime); depth += 2) {
            DOT=new StringBuilder("graph ARBOL{\n");
            //System.out.print("Checking level ");
            //System.out.println(depth);
            for (int[][] move : moves) {
                DOT.append(currentNodeNumber).append("--"); //se conecta a

                nodeNumber += 1;

                auxScore = minMaxRec(move, depth - 1, !myTurn, current, DOT, poda, prune, maxTime, time);
                if((System.nanoTime()-time)/NANO >= maxTime){
                    //System.out.println("Failed, no more time");
                    outOfTime=true;
                    break;
                }
                if (!hasValue) {
                    board.setBoard(move);
                    score = auxScore;
                    hasValue = true;
                    poda = score;

                } else if (auxScore > score) {
                    board.setBoard(move);
                    score = auxScore;
                    poda = score;
                }

                DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(score).append("\"]\n").append("\n").append("}");
            }
            if(!outOfTime){
                //System.out.println("Completed level: "+depth);
                //System.out.println("Time:"+(System.nanoTime()-time)/NANO+"."+(System.nanoTime()-time)%NANO/1000000);
            }

        }


        StringSelection selection = new StringSelection(DOT.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

        time=(System.nanoTime()-time);
        //System.out.println(score);
        //System.out.println(time/NANO+"."+time%NANO/1000000);

        return board.getBoard();
    }


    private int[][] minMaxDepth(List<int[][]> moves, Game game) {
        Board board;
        board = new Board(game.board.getSize());
        Game current = new Game(game.board.getSize(), game.getWhoStart(),(game.useTime())?"time":"depth", game.getLimit(), (game.getPrune()) ? "on" : "off");
        current.switchPlayer();
        StringBuilder DOT = new StringBuilder("graph ARBOL{\n"); //inicializo el DOT
        this.nodeNumber = 0;
        long time=System.nanoTime();
        Integer poda=null;
        int depth,currentNodeNumber = 0,score = 0, auxScore;
        depth=game.getLimit();
        boolean prune=game.getPrune(),hasValue = false,myTurn = true;
            for (int[][] move : moves) {
                DOT.append(currentNodeNumber).append("--"); //se conecta a

                nodeNumber += 1;

                auxScore = minMaxRec(move, depth - 1, !myTurn, current, DOT, poda, prune, -1, time);
                if (!hasValue) {
                    board.setBoard(move);
                    score = auxScore;
                    hasValue = true;
                    poda = score;

                } else if (auxScore > score) {
                    board.setBoard(move);
                    score = auxScore;
                    poda = score;
                }

            }
        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(score).append("\"]\n");

        DOT.append("\n").append("}");

        StringSelection selection = new StringSelection(DOT.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        time=(System.nanoTime()-time);
        //System.out.println("The score was:" + score);
        //System.out.println("Total time was" + time/NANO+"."+time%NANO/1000000);
        return board.getBoard();
    }

    private int minMaxRec(int[][] lastMove, int depth, Boolean myTurn, Game game, StringBuilder DOT, Integer poda,boolean prune,int maxTime,long startTime) {
        Game current = new Game(game.board.getSize(), game.getWhoStart(),(game.useTime())?"time":"depth", game.getLimit(), (game.getPrune()) ? "on" : "off");
        current.board.setBoard(lastMove);
        DOT.append(nodeNumber).append("\n");    //me conecte al anterior
        //CASOS BASE
        if (depth == 0) {
            this.lastScore = current.board.calculateScore(current,color);
            DOT.append(nodeNumber).append(" ").append("[label=\"").append(current.board.score).append("\"]\n");
            return current.board.score;
        }

        current.switchPlayer();
        if((maxTime!=(-1) && ((System.nanoTime()-startTime)/NANO)>=maxTime))
            return 0;


        List<int[][]> moves = current.board.getMoves(current);
        boolean hasValue = false;
        int currentNodeNumber = nodeNumber;
        int auxScore, score = 0;
        Integer podaLocal = poda;

        if(!myTurn)
            DOT.append(nodeNumber).append("[shape=box]\n");
        if (moves.size() == 0){
            this.lastScore = current.board.calculateScore(current,color);
            DOT.append(nodeNumber).append(" ").append("[label=\"").append(current.board.score).append("\"]\n");
            this.lastNode=currentNodeNumber;
            return current.board.score;
        }


        for (int[][] move : moves) {
            DOT.append(currentNodeNumber).append("--"); //me conecto a

            nodeNumber += 1;

            auxScore = minMaxRec(move, depth - 1, !myTurn, current, DOT, podaLocal,prune,maxTime,startTime);

            if((maxTime!=(-1) && ((System.nanoTime()-startTime)/NANO)>=maxTime)){
                return score;
            }

            if (!hasValue) {
                hasValue = true;
                score = auxScore;
                podaLocal = score;
                if (prune && poda != null) {
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
                    if (prune && poda != null && score <= poda) {
                        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(auxScore).append("\"]\n");
                        return score;
                    }
                } else if (!myTurn && auxScore < score) {
                    score = auxScore;
                    podaLocal = score;
                    if (prune && poda != null && score >= poda) {
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
//  ---------- END OF AUXILIAR METHODS ---------- //
}