package MinMax;

import back.Board;
import back.Game;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

public class MinMaxAI {
    private int lastScore;
    private int color;
    private int nodeNumber;

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
        board=new  Board(game.board.getSize()); // CAMBIAR
        Boolean myTurn=true;
        Game current=game;
        current.switchPlayer();
        boolean hasValue=false;
        StringBuilder DOT= new StringBuilder("graph ARBOL{\n"); //inicializo el DOT
        this.nodeNumber=0;
        int currentNodeNumber=0;
        Integer poda=null;
        for(int[][] move: moves){
            DOT.append(currentNodeNumber).append("--"); //se conecta a

            nodeNumber+=1;

            auxScore=minMaxRec(move,depth-1, !myTurn, current,DOT,poda);
            if(!hasValue){
                board.setBoard(move);
                score=auxScore;
                hasValue=true;
                poda=score;
            }
            else if(auxScore>score){
                board.setBoard(move);
                score=auxScore;
                poda=score;
            }

        }
        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(score).append("\"]\n");

        DOT.append("\n").append("}");

        StringSelection selection = new StringSelection(DOT.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        System.out.println(score);

        return board.getBoard();
    }

    int minMaxRec(int[][] lastMove,int depth,Boolean myTurn, Game game,StringBuilder DOT,Integer poda){
        Game current=game;
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
        Integer podaLocal=null;
        for(int[][] move: moves){
            DOT.append(currentNodeNumber).append("--"); //me conecto a

            nodeNumber+=1;

            auxScore=minMaxRec(move,depth-1, !myTurn, current,DOT,podaLocal);

            if(!hasValue){
                hasValue=true;
                score=auxScore;
                podaLocal=score;
                if(poda!=null) {
                    if (!myTurn && score<=poda ) {
                        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(auxScore).append("\"]\n");
                        return score;
                    }
                    if (myTurn && score>= poda) {
                        DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(auxScore).append("\"]\n");
                        return score;
                    }
                }
            }
            else {
                if (myTurn && auxScore > score) {
                    score=auxScore;
                    podaLocal=score;
                    if(poda!=null){
                        if(score>=poda){
                            DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(auxScore).append("\"]\n");
                            return score;
                        }
                    }
                } else if (!myTurn && auxScore < score) {
                    score= auxScore;
                    podaLocal=score;
                    if(poda!=null){
                        if(score<=poda){
                            DOT.append(currentNodeNumber).append(" ").append("[label=\"").append(auxScore).append("\"]\n");
                            return score;
                        }
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