package ui;

import java.util.List;

import MinMax.MinMaxAI;
import back.Game;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

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
        
        this.game = new Game(0, false, 0, 0);
        this.reversiBoard = new ReversiBoard();
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(reversiBoard.createContent(game)));
		primaryStage.setResizable(true);
		Label p1Score = new Label();
		//p1Score.textProperty().bind(game.p1.score);
		p1Score.setAlignment(Pos.TOP_LEFT);
		Label p2Score = new Label();
		p2Score.setText(Integer.toString(game.p2.score));
		p2Score.setAlignment(Pos.TOP_RIGHT);
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}