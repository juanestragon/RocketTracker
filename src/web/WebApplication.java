package web;

import events.GameMatchEventListener;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.RocketLeagueClient;

public class WebApplication extends Application {

    private static WebAPI webAPI;
    private static GameMatchEventListener webMatchEventListener;
    private static RocketLeagueClient client;

    public static void configure(
            WebAPI webAPI,
            GameMatchEventListener webMatchEventListener,
            RocketLeagueClient client
    ) {

        WebApplication.webAPI = webAPI;
        WebApplication.webMatchEventListener =
                webMatchEventListener;
        WebApplication.client = client;
    }

    @Override
    public void start(Stage stage) {

        WebView view = new WebView();
        view.setConnectionStatus(false);

        WebController controller =
                new WebController(
                        webAPI,
                        view,
                        client,
                        webAPI.getConfigStorage()
                );

        /*
         * Eventos de partidas:
         *
         * RocketLeagueListener
         * -> MatchEventListener
         * -> GameMatchEventListener
         * -> WebController
         */
        webMatchEventListener.setListener(
                controller
        );

        /*
         * Estados de conexión:
         *
         * RocketLeagueClient
         * -> ConnectionStateListener
         * -> WebController
         */
        client.setConnectionStateListener(
                controller
        );

        Scene scene = new Scene(
                view.getRoot(),
                1200,
                750
        );

        scene.getStylesheets().add(
                getClass()
                        .getResource("/css/style.css")
                        .toExternalForm()
        );

        stage.setMinWidth(1000);
        stage.setMinHeight(650);

        stage.setTitle("Rocket Tracker");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
