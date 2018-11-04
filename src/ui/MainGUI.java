package ui;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import MinMax.MinMaxAI;
import back.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
        
        this.game = new Game(1, false, 1, 0);
        this.reversiBoard = new ReversiBoard();
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane pane = new BorderPane();
		pane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #dc143c, #661a33)");
		pane.setLeft(reversiBoard.createButtons(game));
		pane.setCenter(reversiBoard.createContent(game));
		pane.setRight(reversiBoard.createScoreContent(game));
		primaryStage.setScene(new Scene(pane));
		primaryStage.setTitle("WELCOME TO THE EDA RICEFIELDS REVERSI MADERFAKER");
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent window) {
				Platform.exit();
				System.exit(0);
			}
		});
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}