package app;

import config.Config;
import config.ConfigStorage;
import events.GameMatchEventListener;
import javafx.application.Application;
import matches.MatchStorage;
import network.RocketLeagueClient;
import web.WebAPI;
import web.WebApplication;

import java.nio.file.Path;

public class Main {



    public static void main(String[] args) {

        ConfigStorage configStorage = new ConfigStorage(Path.of("data", "config.json"));
        Config config = configStorage.load();
        WebAPI webAPI = new WebAPI(configStorage);
        GameMatchEventListener eventListener = new GameMatchEventListener(webAPI);



        RocketLeagueClient client = new RocketLeagueClient(config.getRocketLeagueUrl(), config.getPlayerName(), new MatchStorage(Path.of(config.getStoragePath().toUri()
                                )), eventListener);

        client.start();
        WebApplication.configure(webAPI, eventListener, client);
        Application.launch(
                WebApplication.class,
                args
        );

        client.stop();
    }
}