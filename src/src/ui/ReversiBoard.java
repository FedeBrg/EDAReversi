
package ui;
import MinMax.*;


import java.util.Arrays;

import back.Board;
import back.Game;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ReversiBoard extends Application{
	
	private Tile[][] tileMatrix;
	Game game=new Game(1,false,0,0);
	
	
	private Parent createContent() {
		Pane root = new Pane();
		root.setPrefSize(900, 900);
		tileMatrix = new Tile[8][8];
		
		
		for(int i = 0; i<8;i++) {
			for(int j = 0; j<8;j++) {
				Tile tile = new Tile(i,j);
				tile.setTranslateX(j*100);
				tile.setTranslateY(i*100);
				
				root.getChildren().add(tile);
				
				tileMatrix[i][j]=tile;

			}
		}
		updateBoard(game.board.board);
		return root;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();
	}
	
	private class Tile extends StackPane{
		
		private Circle circle = new Circle(40,40,40);
		private int row;
		private int col;
		
		public Tile(int row, int col) {
			
			this.row = row;
			this.col = col;
			
			Rectangle border = new Rectangle(100,100);
			
			border.setFill(Color.GREEN);
			border.setStroke(Color.BLACK);
			border.setStrokeWidth(4);
			
			circle.setFill(null);
			
			
			setAlignment(Pos.CENTER);
			
			getChildren().addAll(border,circle);
			
			setOnMouseClicked(event -> {
					int [][] mat = game.board.isValidMove(row, col, game.current.colour);
					if(mat !=null) {
						game.changePlayer();
						updateBoard(mat);
						game.board.board = mat;
						if(game.cpu) {
							MinMaxAI ai=new MinMaxAI(game,game.current.colour);
							ai.makeMove();
							updateBoard(game.board.board);
							game.changePlayer();
						}
					}
						
			});
			
			
			
		
		}
		
		private void drawWhite() {
			circle.setFill(Color.WHITE);
		}
		
		private void drawBlack() {
			circle.setFill(Color.BLACK);
		}
	}
	
	
	public void updateBoard(int [][] board) {
		for(int i = 0; i<8;i++) {
			for(int j = 0; j<8;j++) {
				if(board[i][j]==1) {
					tileMatrix[i][j].drawBlack();
				}
				else if(board[i][j] == 2) {
					tileMatrix[i][j].drawWhite();
				}
			}
		}
		
	}
	
	
	public static void main(String[] args) {	
		
		launch(args);		
	}
		
}
	
