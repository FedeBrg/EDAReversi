package ui;
import MinMax.*;


import java.util.Arrays;
import java.util.List;

import back.Board;
import back.Game;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ReversiBoard {
	
	private class Tile extends StackPane{
		
		private Circle circle = new Circle(20,20,20);
		private int row;
		private int col;
		private Rectangle border;
		public Tile(int row, int col) {
			this.row = row;
			this.col = col;
			this.border = new Rectangle(50,50);
			
			border.setFill(Color.GREEN);
			border.setStroke(Color.BLACK);
			border.setStrokeWidth(4);
			
			circle.setFill(null);
			setAlignment(Pos.CENTER);
			getChildren().addAll(border,circle);
			}
		
		private void drawWhite() {
			circle.setFill(Color.WHITE);
		}
		
		private void drawBlack() {
			circle.setFill(Color.BLACK);
		}
	}
	
	private Tile[][] tileMatrix;
	
	Parent createContent(Game game) {
		Pane root = new Pane();
		root.setPrefSize(450, 450);
		tileMatrix = new Tile[8][8];
		
		EventHandler mouseHandler = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent t) {
		    	Tile tile = (Tile) t.getSource();
		    	tile.border.setFill(Color.GREEN);
		    	
		    	//me fijo si el lugar donde clickee es valido
		    	int[][] mat = game.board.isValidMove(tile.row, tile.col, game.current.colour);
				
		    	//si es valido, entro aca
		    	if(mat != null) {
		    		updateBoard(mat);
		    		game.board.setBoard(mat);
					game.p1.setScore(game.board.calculatePlayerScore(game.current.colour));
					if(game.ai != null) {
						game.switchPlayer();
						int[][] aiBoard = game.computerTurn(game);
						game.switchPlayer();

						if(aiBoard != null) {
							updateBoard(aiBoard);	
						}
					}
					
					else {
						game.switchPlayer();
					}
				}
		    	
		    	//si entro aca es porque oclickee en cualquier lugar o porque no tengo movimientos o 
		    	//se lleno el tablero
		    	else {
		    		List<int[][]> aux = game.board.getMoves(game);
		    		if(aux.size() == 0) {
		    			//si no tengo movimientos y le toca a la compu
			    		if(game.ai != null) {
			    			while(game.board.hasAvailableMoves(tile.row, tile.col, game.current.colour) == null) {
			    				int[][] aiBoard = game.computerTurn(game);
							
			    				if(aiBoard != null) {
			    					updateBoard(aiBoard);	
			    				}
			    			}
			    		}
			    		//si no tengo movimientos y le toca al otro negro
			    		else {
			    			game.switchPlayer();
			    		}
		    		}
		    		
		    	}
		    		
		}};
		
		EventHandler displayColour = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent t) {
		    	Tile tile = (Tile) t.getSource();
		    	//aca hay que poner la booleana pero no entiendo los parametros y paja preguntarles
		    	int[][] mat = game.board.isValidMove(tile.row, tile.col, game.current.colour);
				
		    	if(mat !=null) {
					tile.border.setFill(Color.YELLOW);
				}
		    
		}};
		
		EventHandler returnColour = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent t) {
		    	Tile tile = (Tile) t.getSource();
		    	//aca hay que poner la booleana pero no entiendo los parametros y paja preguntarles
		    	int[][] mat = game.board.isValidMove(tile.row, tile.col, game.current.colour);
				
		    	if(mat !=null) {
					tile.border.setFill(Color.GREEN);
				}
		    
		}};
		
		for(int i = 0; i<8;i++) {
			for(int j = 0; j<8;j++) {
				Tile tile = new Tile(i,j);
				tile.setOnMouseClicked(mouseHandler);
				tile.setOnMouseEntered(displayColour);
				tile.setOnMouseExited(returnColour);
				tile.setTranslateX(j*50);
				tile.setTranslateY(i*50);
				
				root.getChildren().add(tile);
				tileMatrix[i][j]=tile;
			}
		}
		
		updateBoard(game.board.getBoard());
		return root;
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
}
	
