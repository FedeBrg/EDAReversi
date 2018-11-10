package ui;
import java.io.IOException;
import java.util.Optional;
import back.Game;
import back.Player;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
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

public class ReversiBoard {
	private final int MARK = 13;
	private int[][] lastMoves;
	private final static int FREE = 0;
	private final static int BLACK = 1;
	private final static int WHITE = 2;
	
	//  ---------- AUXILIAR CLASSES ---------- //
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
			border.setFill(Color.GREEN);
		}
		
		private void setYellow() {
			border.setFill(Color.YELLOWGREEN);
			circle.setFill(Color.DARKGREEN);
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
			if(color == BLACK) {
				return Color.BLACK;
			}
			
			return Color.WHITE;
		}
		
	}
	//  ---------- END OF AUXILIAR CLASSES ---------- //
	
	//  ---------- INSTANCE VARIABLES ---------- //
	private Tile[][] tileMatrix;
	private ScoreTile[][] scoreMatrix;
	//  ---------- END OF INSTANCE VARIABLES ---------- //
	
	//  ---------- JAVAFX CONTENT CREATION ---------- //
	public VBox createScoreContent(Game game) {
		VBox scoreboard = new VBox();
		this.scoreMatrix = new ScoreTile[1][2];
		ScoreTile blackTile = new ScoreTile(1);
		ScoreTile whiteTile = new ScoreTile(2);
		blackTile.refreshScore(game.p1);
		whiteTile.refreshScore(game.p2);
		blackTile.setAlignment(Pos.CENTER);
		whiteTile.setAlignment(Pos.CENTER);
		
		scoreMatrix[0][0] = blackTile;
		scoreMatrix[0][1] = whiteTile;
		
		scoreboard.getChildren().add(blackTile);
		scoreboard.getChildren().add(whiteTile);
		
		scoreboard.setAlignment(Pos.CENTER);
		return scoreboard;
	}
	
	Parent createContent(Game game) {
		Pane root = new Pane();
		root.setPrefSize(54 * game.board.getSize(), 54 * game.board.getSize());
		tileMatrix = new Tile[game.board.getSize()][game.board.getSize()];
		
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent t) {
		    	if(game.gameHasStarted()) {
		    		Tile tile = (Tile) t.getSource();
			    	unprintAvailableMoves(game);
			    	
			    	int[][] myMovement = game.board.isValidMove(tile.row, tile.col, game.getCurrent());
			    	
			    	if(myMovement != null) {
			    		game.pushToStack(game.board.getBoard());
			    		game.switchPlayer();
			    		game.board.setBoard(myMovement);
			    		game.incrementPieces();
			    		updateBoard(myMovement);
			    		refreshScores(game, myMovement);
			    		
			    		if(game.isBoardFull()) {
			    			endGame(game);
			    		}
			    		
			    		boolean hasMoves = game.board.hasAvailableMoves(game.getCurrent());
			    	
			    		if(!hasMoves) {
			    			game.switchPlayer();
			    		}
			    		
			    		else if(game.isComputerPlaying()) {
			    			computerTurn(game);
			    		}
			    	}
			    	
			    	printAvailableMoves(game);
		    	}	
		}};
		
		for(int i = 0; i < game.board.getSize(); i++) {
			for(int j = 0; j < game.board.getSize(); j++) {
				Tile tile = new Tile(i,j);
				tile.setOnMouseClicked(mouseHandler);
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
				if(game.gameHasStarted()) {
					game.undo();
					
					if(game.isComputerPlaying() && game.getCurrent() == game.ai.getColor()) {
						computerTurn(game);
					}
					
					updateBoard(game.board.getBoard());
					refreshScores(game, game.board.getBoard());
					printAvailableMoves(game);
				}
			}
		};
		undoButton.setOnMouseClicked(undoHandler);
		
		Button nextButton = new Button("Next Move");
		EventHandler<MouseEvent> nextHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				boolean hasMoves = game.board.hasAvailableMoves(game.getCurrent());
				
				if(!hasMoves) {
					boolean enemyMoves = game.board.hasAvailableMoves(game.enemyColor());
					if(!enemyMoves) {
						endGame(game);
					}
					
					else if(!game.isBoardFull()) {	
						if(game.isComputerPlaying()) {
							game.switchPlayer();
							computerTurn(game);
						}
						
						else {
							game.switchPlayer();
							printAvailableMoves(game);
						}
					}
				}
				
				else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("NOT A VALID MOVE");
					alert.setContentText("You can't go to next move if you have available moves !");
					alert.showAndWait();
				}		
			}
		};
		nextButton.setOnMouseClicked(nextHandler);

		Button startButton = new Button("Start Game");
		EventHandler<MouseEvent> startHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				if(!game.gameHasStarted()) {
					game.startGame();
					if(game.isComputerPlaying() && game.getCurrent() == game.ai.getColor()) {
						computerTurn(game);
					}
				}
				
				printAvailableMoves(game);
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
					
					if(result != null && result.isPresent()) {
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
		
		Button helpButton = new Button("Help");
		EventHandler<MouseEvent> helpHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("HELP");
				alert.setHeaderText("Help menu");
				alert.setContentText("Start Game -> only can be used once at the start of the game, both loaded or new.\n\nUndo -> only can be used during game.\n\nNext Move -> when you run out of moves, you can press this button to continue.\n\nSave -> save current game.");
				alert.showAndWait();
			}
		};
		helpButton.setOnMouseClicked(helpHandler);
		
		VBox sp = new VBox();
		sp.setAlignment(Pos.TOP_CENTER);
		sp.setSpacing(15);
		sp.getChildren().addAll(startButton, saveButton, undoButton, nextButton, helpButton);
		
		return sp;
	}
	//  ---------- END OF JAVAFX CONTENT CREATION ---------- //
	
	
	//  ---------- AUXILIAR FUNCTIONS ---------- //
	public void endGame(Game game) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Game Ended - Woohoo!");
		int p1Score = game.p1.getScore();
		int p2Score = game.p2.getScore();
		
		int player = (p1Score >= p2Score)? 1:2;
		int pieces = (p1Score >= p2Score)? p1Score : p2Score;
		
		alert.setContentText(String.format("Player %d has won with %d pieces!", player, pieces));
		alert.setHeaderText("Yoo - hoo!");
		alert.showAndWait();
		
		Platform.exit();
		System.exit(0);
	}
	
	public void refreshScores(Game game, int[][] matrix) {
		int p1Score = game.calculateP1Score(matrix);
		game.p1.setScore(p1Score);
		game.p2.setScore(game.getPieces() - game.p1.getScore());
		scoreMatrix[0][0].refreshScore(game.p1);
		scoreMatrix[0][1].refreshScore(game.p2);
	}
	
	
	
	public void printAvailableMoves(Game game) {
		if(game.gameHasStarted()) {
			int[][] toPaint = game.board.getAllAvailableMoves(game, game.getCurrent());
	    	for(int i = 0; i < game.board.getSize(); i++) {
	    		for(int j = 0; j < game.board.getSize(); j++) {
	    			if(toPaint[i][j] == MARK) {
	    				tileMatrix[i][j].setYellow();
	    			}
	    		}
	    	}
	    	
	    	this.lastMoves = toPaint;
    	}
	}
	
	public void unprintAvailableMoves(Game game) {
		if(game.gameHasStarted()) {
			int[][] actualMatrix = game.board.getBoard();
			for(int i = 0; i < game.board.getSize(); i++) {
	    		for(int j = 0; j < game.board.getSize(); j++) {
	    			if(lastMoves[i][j] == MARK && !(actualMatrix[i][j] == BLACK || actualMatrix[i][j] == WHITE)) {
	    				tileMatrix[i][j].drawGreen();
	    			}
	    		}
	    	}
		}
	}
	
	public void computerTurn(Game game) {
		int[][] aiBoard = game.computerTurn(game);
			
		if(aiBoard != null) {
			game.pushToStack(game.board.getBoard());
	    	game.board.setBoard(aiBoard);
	    	game.incrementPieces();
	    	refreshScores(game, aiBoard);
	    	updateBoard(aiBoard);
	    	game.switchPlayer();
	    	
	    	if(game.isBoardFull()) {
				endGame(game);
			}
	    	
	    	printAvailableMoves(game);
		}
			
	}
	
	
	
	public void updateBoard(int[][] board) {
		for(int i = 0; i < board[0].length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				
				if(board[i][j] == BLACK) {
					tileMatrix[i][j].drawBlack();
				}
				
				else if(board[i][j] == WHITE) {
					tileMatrix[i][j].drawWhite();
				}
				
				else if(board[i][j] == FREE) {
					tileMatrix[i][j].drawGreen();
				}
			}
		}		
	}	
//  ---------- END OF AUXILIAR FUNCTIONS ---------- //
	
}
	
