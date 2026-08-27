package gui;

import events.GameMatchEventListener;
import network.RocketLeagueClient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiApplication extends Application {

    private static GuiAPI guiAPI;
    private static GameMatchEventListener guiMatchEventListener;
    private static RocketLeagueClient client;

    public static void configure(GuiAPI guiAPI, GameMatchEventListener guiMatchEventListener, RocketLeagueClient client) {
        GuiApplication.guiAPI = guiAPI;
        GuiApplication.guiMatchEventListener = guiMatchEventListener;
        GuiApplication.client = client;
    }

    @Override
    public void start(Stage stage) {

        GuiView view = new GuiView();

        GuiController controller = new GuiController(guiAPI, view, client, guiAPI.getConfigStorage());

        guiMatchEventListener.setListener(controller);


        client.setConnectionStateListener(controller);

        Scene scene = new Scene(view.getRoot(), 1200, 750);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.setTitle("Rocket Tracker");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
