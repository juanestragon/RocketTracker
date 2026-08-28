package gui;

import config.Config;
import config.ConfigStorage;
import events.GameMatchEventListener;
import lang.TranslationParser;
import network.RocketLeagueClient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GuiApplication extends Application {

    private static GuiAPI guiAPI;
    private static GameMatchEventListener guiMatchEventListener;
    private static RocketLeagueClient client;
    private static ConfigStorage configStorage;

    public static void configure(GuiAPI guiAPI, GameMatchEventListener guiMatchEventListener, RocketLeagueClient client, ConfigStorage configStorage) {
        GuiApplication.guiAPI = guiAPI;
        GuiApplication.guiMatchEventListener = guiMatchEventListener;
        GuiApplication.client = client;
        GuiApplication.configStorage = configStorage;
    }



    @Override
    public void start(Stage stage) throws IOException {

        GuiView view = new GuiView(TranslationParser.parse(Files.readString(Path.of("res", "lang", "lang.json")), configStorage.load().getLang()).getGuiTrans());


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
