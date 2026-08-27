package app;

import config.ConfigStorage;
import events.GameMatchEventListener;
import network.RocketLeagueClient;
import gui.GuiAPI;
import gui.GuiApplication;

import javafx.application.Application;
import java.nio.file.Path;

public class Main {



    public static void main(String[] args) {

        ConfigStorage configStorage = new ConfigStorage(Path.of("data", "config.json"));
        GuiAPI guiAPI = new GuiAPI(configStorage);
        GameMatchEventListener eventListener = new GameMatchEventListener(guiAPI);

        RocketLeagueClient client = new RocketLeagueClient(configStorage, eventListener);
        client.start();

        GuiApplication.configure(guiAPI, eventListener, client);
        Application.launch(GuiApplication.class, args);

        client.stop();
    }
}