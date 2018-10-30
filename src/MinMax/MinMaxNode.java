package MinMax;

import back.Board;

public class MinMaxNode {
    Board board;
    int score;

    MinMaxNode(Board board){
        this.board=board;
        this.score=board.score();
    }
}
