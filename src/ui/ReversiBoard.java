package ui;
import MinMax.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import back.Board;
import back.Game;
import back.Player;
import back.Game.UndoNode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
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
	
	public VBox createScoreContent(Game game) {
		VBox scoreboard = new VBox();
		this.scoreMatrix = new ScoreTile[1][2];
		ScoreTile blackTile = new ScoreTile(1);
		ScoreTile whiteTile = new ScoreTile(2);
		blackTile.refreshScore(game.p1);
		whiteTile.refreshScore(game.p2);
		blackTile.setAlignment(Pos.CENTER);
		whiteTile.setAlignment(Pos.CENTER);
		
//		blackTile.setTranslateX(-8);
//		whiteTile.setTranslateX(-8);
//		whiteTile.setTranslateY(50);
		
		scoreMatrix[0][0] = blackTile;
		scoreMatrix[0][1] = whiteTile;
		
		scoreboard.getChildren().add(blackTile);
		scoreboard.getChildren().add(whiteTile);
		
		scoreboard.setAlignment(Pos.CENTER);
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
		root.setPrefSize(54 * game.board.getSize(), 54 * game.board.getSize());
		tileMatrix = new Tile[game.board.getSize()][game.board.getSize()];
		
		EventHandler mouseHandler = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent t) {
		    	if(game.gameHasStarted) {
		    		Tile tile = (Tile) t.getSource();
			    	tile.border.setFill(Color.GREEN);
			    	
			    	int[][] myMovement = game.board.isValidMove(tile.row, tile.col, game.current.colour);
			    	
			    	if(myMovement != null) {
			    		game.pushToStack(game.board.getBoard());
			    		game.switchPlayer();
			    		game.board.setBoard(myMovement);
			    		game.incrementPieces();
			    		refreshScores(game, myMovement);
			    		updateBoard(myMovement);
			    		if(game.board.isBoardFull()) {
			    			Alert alert = new Alert(AlertType.CONFIRMATION);
				    		alert.setTitle("Game Ended - Woohoo!");
				    		
				    		int player = (game.p1.score > game.p2.score)? 1:2;
				    		int pieces = (game.p1.score > game.p2.score)? game.p1.score : game.p2.score;
				    		
				    		alert.setContentText(String.format("Player %d has won with %d pieces!", player, pieces));
				    		alert.setHeaderText("Finally!");
				    		alert.showAndWait();
				    		
				    		Platform.exit();
				    		System.exit(0);
			    		}
			    		
			    		boolean hasMoves = game.board.hasAvailableMoves(game.current.colour);
			    		System.out.println(hasMoves);
			    		if(!hasMoves) {
			    			game.switchPlayer();

			    			/*if(game.ai != null) {
			    				int[][] aiBoard = game.computerTurn(game);
				    			
				    			if(aiBoard != null) {
				    				game.pushToStack(game.board.getBoard());
						    		game.board.setBoard(aiBoard);
						    		game.incrementPieces();
						    		refreshScores(game, aiBoard);
						    		updateBoard(aiBoard);
				    			}
				    			game.switchPlayer();
			    			}*/
			    		}
			    		
			    		else if(game.ai != null) {
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
		    	}	
		}};
		
		EventHandler<MouseEvent> displayColour = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent t) {
		    	if(game.gameHasStarted) {
		    		Tile tile = (Tile) t.getSource();
			    	int[][] myPosition = game.board.isValidMove(tile.row, tile.col, game.current.colour);
			    	if(myPosition !=null) {
						tile.border.setFill(Color.YELLOW);
						tile.circle.setFill(Color.YELLOW);
					}
		    	}
		}};
		
		EventHandler<MouseEvent> returnColour = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent t) {
		    	if(game.gameHasStarted) {
		    		Tile tile = (Tile) t.getSource();
			    	int[][] myPosition = game.board.isValidMove(tile.row, tile.col, game.current.colour);
					
			    	if(myPosition != null) {
						tile.border.setFill(Color.GREEN);
						tile.circle.setFill(Color.GREEN);
					}
		    	}
		}};
		
		for(int i = 0; i < game.board.getSize(); i++) {
			for(int j = 0; j < game.board.getSize(); j++) {
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
				boolean hasMoves = game.board.hasAvailableMoves(game.current.colour);
				System.out.println(hasMoves);
				if(!hasMoves) {
					if(!game.board.isBoardFull()){
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

						else {
							game.switchPlayer();
						}
					}

				}
				
				else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("NO YU DONT DO DAT");
					alert.setContentText("You can't go to next move if you have available moves !");
					alert.showAndWait();
				}		
			}
		};
		nextButton.setOnMouseClicked(nextHandler);

		Button startButton = new Button("Start Game");
		EventHandler<MouseEvent> startHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				if(!game.gameHasStarted) {
					game.startGame();
					if(game.ai != null && game.p1.colour == game.ai.color) {
						int[][] aiBoard = game.computerTurn(game);
						if(aiBoard != null) {
							game.pushToStack(game.board.getBoard());
				    		game.board.setBoard(aiBoard);
				    		game.incrementPieces();
				    		refreshScores(game, aiBoard);
				    		updateBoard(aiBoard);	
						}
					}
				}
			}
		};
		startButton.setOnMouseClicked(startHandler);
		
		Button saveButton = new Button("Save");
		
		EventHandler<MouseEvent> saveHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				try {
					TextInputDialog tid = new TextInputDialog("Savegame");
					tid.setTitle("Time to save your game!");
					tid.setHeaderText("Savegame");
					tid.setContentText("Enter filename");
					Optional<String> result = tid.showAndWait();
					
					if(result != null) {
						game.writeObject(game, result.get());
						Alert alert = new Alert(AlertType.CONFIRMATION);
			    		alert.setTitle("Game Saved - Woohoo!");
			    		
			    		alert.setContentText(String.format("Yay!"));
			    		alert.setHeaderText("Finally this great game is saved!");
			    		alert.showAndWait();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Can't save game !");
					alert.setContentText("All our operators are currently busy. Please try again later");
					alert.showAndWait();
				}
			}
		};
		saveButton.setOnMouseClicked(saveHandler);
		
		VBox sp = new VBox();
		sp.setAlignment(Pos.TOP_CENTER);
		sp.setSpacing(15);
		sp.getChildren().addAll(startButton, saveButton, undoButton, nextButton);
		
		return sp;
	}
	
	public void updateBoard(int[][] board) {
		for(int i = 0; i < board[0].length;i++) {
			for(int j = 0; j < board[0].length;j++) {
				if(board[i][j] == 1) {
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
	
