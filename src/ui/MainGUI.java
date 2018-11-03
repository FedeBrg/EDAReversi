package ui;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import MinMax.MinMaxAI;
import back.Game;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainGUI extends Application{
	public Game game;
	
	//creo que no va a hacer falta tener una referencia a reversiboard porque ya hay una en game
	public ReversiBoard reversiBoard;
	
	@Override
    public void init() throws Exception {
        super.init();
        Parameters parameters = getParameters();
        //esta chota tiene los argumentos ahi re piyos
        List<String> rawArguments = parameters.getRaw();
        
        this.game = new Game(0, false, 1, 0);
        this.reversiBoard = new ReversiBoard();
        Timer timer = new Timer();
		TimerTask somebodyHasWon = new TimerTask() {
			@Override
		    public void run() {
		    	if(game.board.isBoardFull()) {
	    			int score1 = game.p1.score;
	    			int score2 = game.p2.score;
		    		
	    			if(score1 >= score2) {
	    				System.out.println(String.format("%s %d %s", "Player 1 won with ", score1, " balls!"));
	    				this.cancel();
	    			}
	    			
		    		else {
		    			System.out.println(String.format("%s %d %s", "Player 2 won with ", score1, " balls!"));
		    			this.cancel();
		    		}
	    			
	    		}
		    }
		};
		
		timer.schedule(somebodyHasWon, 0, 1000);
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane pane = new BorderPane();
		HBox hbox = new HBox();
		VBox vbox = new VBox();
		vbox.getChildren().add(new Text(Integer.toString(game.p1.score)));
		vbox.getChildren().add(new Text(Integer.toString(game.p2.score)));
		
		Button undo = new Button("Undo");
		hbox.getChildren().add(undo);
		pane.setCenter(reversiBoard.createContent(game));
		pane.setTop(hbox);
		pane.setRight(vbox);
		primaryStage.setScene(new Scene(pane));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}