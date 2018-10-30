package MinMax;

import back.Board;
import back.Game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MinMaxAI {
    Game game;
    int color;

    MinMaxAI(Game game,int color){
        this.game=game;
        this.color=color;
    }

    void MakeMove(){
        List<Board> moves=this.getMoves();
        Random rand=new Random();
        int n=rand.nextInt();
        game.board=moves.get(n%moves.size());
    }
    List<Board> getMoves(){
        List<Board> moves= new LinkedList<>();
        for(int i = 0;i<game.board.size; i++){
            for (int j = 0; j < game.board.size; j++) {
                Board aux =game.board.isValidMove(i,j,color);
                if(aux!=null)
                    moves.add(aux);
            }
        }

    }
}
