package ui;

import java.util.Map;
import back.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MainGUI extends Application{
	public Game game;
	public ReversiBoard reversiBoard;
	
	@Override
    public void init() throws Exception {
        super.init();

        Parameters parameters = getParameters();
        Map<String, String> arguments = parameters.getNamed();
        
        if(!arguments.containsKey("load") && arguments.size() == 5) {
        	this.game = new Game(Integer.valueOf(arguments.get("size")), 
            		Integer.valueOf(arguments.get("ai")), arguments.get("mode").toString(), 
            		Integer.valueOf(arguments.get("param")), arguments.get("prune"));	
        }
        
        else if(arguments.size() == 5) {
        	this.game = new Game(Integer.valueOf(arguments.get("ai")), arguments.get("mode").toString(), 
            		Integer.valueOf(arguments.get("param")), arguments.get("prune").toString(), arguments.get("load").toString());	
        }
        
        else {
        	System.out.println("Check parameters. \n");
        }
        
        this.reversiBoard = new ReversiBoard();
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane pane = new BorderPane();
		pane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #dc143c, #661a33)");
		pane.setLeft(reversiBoard.createButtons(game));
		pane.setCenter(reversiBoard.createContent(game));
		Parent scoreboard = reversiBoard.createScoreContent(game);
		pane.setRight(scoreboard);
		BorderPane.setAlignment(scoreboard, Pos.BOTTOM_CENTER);
		StackPane sp = new StackPane();
		sp.getChildren().add(new Text(""));
		pane.setTop(sp);;
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