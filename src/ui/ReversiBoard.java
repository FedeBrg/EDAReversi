package ui;
import MinMax.*;


import java.util.Arrays;
import java.util.List;

import back.Board;
import back.Game;
import back.Player;
import back.Game.UndoNode;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
		
		private void drawGreen() {
			circle.setFill(Color.GREEN);
		}
	}
	
	private class ScoreTile extends StackPane{
		private Circle circle = new Circle(20,20,20);
		private Text score = new Text();
		
		public ScoreTile(int color) {
			circle.setFill(getColor(color));
			setAlignment(Pos.CENTER);
			getChildren().addAll(circle);	
			score.setTextAlignment(TextAlignment.CENTER);
			score.setFont(Font.font("Verdana", 20));
			score.setFill(Color.RED);
			getChildren().addAll(score);
		}
		
		public void refreshScore(Player player) {
			score.setText(Integer.toString(player.score));
		}
		
		private Color getColor(int color) {
			if(color == 1) {
				return Color.BLACK;
			}
			
			return Color.WHITE;
		}
		
	}
	
	private Tile[][] tileMatrix;
	private ScoreTile[][] scoreMatrix;
	
	public Parent createScoreContent(Game game) {
		StackPane scoreboard = new StackPane();
		this.scoreMatrix = new ScoreTile[1][2];
		ScoreTile blackTile = new ScoreTile(1);
		ScoreTile whiteTile = new ScoreTile(2);
		blackTile.refreshScore(game.p1);
		whiteTile.refreshScore(game.p2);
		
		blackTile.setTranslateX(-25);
		whiteTile.setTranslateX(-25);
		whiteTile.setTranslateY(50);
		
		scoreMatrix[0][0] = blackTile;
		scoreMatrix[0][1] = whiteTile;
		
		scoreboard.getChildren().add(blackTile);
		scoreboard.getChildren().add(whiteTile);
		
		return scoreboard;
	}
	
	public void refreshScores(Game game, int[][] matrix) {
		int p1Score = game.calculateP1Score(matrix);
		game.p1.setScore(p1Score);
		game.p2.setScore(game.totalPieces - game.p1.getScore());
		scoreMatrix[0][0].refreshScore(game.p1);
		scoreMatrix[0][1].refreshScore(game.p2);
	}
	
	Parent createContent(Game game) {
		Pane root = new Pane();
		root.setPrefSize(450, 450);
		tileMatrix = new Tile[8][8];
		
		EventHandler mouseHandler = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent t) {
		    	Tile tile = (Tile) t.getSource();
		    	tile.border.setFill(Color.GREEN);
		    	int[][] myMovement = game.board.isValidMove(tile.row, tile.col, game.current.colour);
		    	
		    	if(myMovement != null) {
		    		game.pushToStack(game.board.getBoard());
		    		game.board.setBoard(myMovement);
		    		game.incrementPieces();
		    		refreshScores(game, myMovement);
		    		updateBoard(myMovement);
		    		game.switchPlayer();
		    		
		    		if(game.ai != null) {
		    			int[][] aiBoard = game.computerTurn(game);
		    			
		    			if(aiBoard != null) {
		    				game.pushToStack(game.board.getBoard());
				    		game.board.setBoard(aiBoard);
				    		game.incrementPieces();
				    		refreshScores(game, aiBoard);
				    		updateBoard(aiBoard);
		    			}
		    		
		    			game.switchPlayer();
		    		}
		    	}	
		}};
		
		EventHandler<MouseEvent> displayColour = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent t) {
		    	Tile tile = (Tile) t.getSource();
		    	int[][] myPosition = game.board.isValidMove(tile.row, tile.col, game.current.colour);
				
		    	if(myPosition !=null) {
					tile.border.setFill(Color.YELLOW);
				}
		}};
		
		EventHandler<MouseEvent> returnColour = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent t) {
		    	Tile tile = (Tile) t.getSource();
		    	int[][] myPosition = game.board.isValidMove(tile.row, tile.col, game.current.colour);
				
		    	if(myPosition != null) {
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
	
	public Parent createButtons(Game game) {
		Button undoButton = new Button("Undo");
		
		EventHandler<MouseEvent> undoHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				game.undo();
				updateBoard(game.board.getBoard());
				refreshScores(game, game.board.getBoard());
			}
		};
		undoButton.setOnMouseClicked(undoHandler);
		
		Button nextButton = new Button("Next Move");
		EventHandler<MouseEvent> nextHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				if(game.ai != null) {
	    			int[][] aiBoard = game.computerTurn(game);
	    			
	    			if(aiBoard != null) {
	    				game.switchPlayer();
	    				game.pushToStack(game.board.getBoard());
			    		game.board.setBoard(aiBoard);
			    		game.incrementPieces();
			    		refreshScores(game, aiBoard);
			    		updateBoard(aiBoard);
	    			}
	    		
	    			game.switchPlayer();
	    		}
			}
		};
		nextButton.setOnMouseClicked(nextHandler);

		VBox sp = new VBox();
		sp.setAlignment(Pos.TOP_CENTER);
		sp.setSpacing(10);
		sp.getChildren().addAll(undoButton, nextButton);
		
		return sp;
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
				
				else if(board[i][j] == 0) {
					tileMatrix[i][j].drawGreen();
				}
			}
		}
		
	}	
}
	
