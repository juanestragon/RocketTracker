package network;

import config.Config;
import config.ConfigStorage;
import events.MatchEventListener;
import matches.MatchStorage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;

public class RocketLeagueClient {

    private final MatchEventListener eventListener;

    private final HttpClient httpClient;

    private volatile WebSocket webSocket;

    private volatile ConnectionState connectionState = ConnectionState.DISCONNECTED;
    private volatile ConnectionStateListener connectionStateListener;

    private volatile boolean running;
    private ConfigStorage configStorage;
    private Thread connectionThread;
    private Config config;

    public RocketLeagueClient(ConfigStorage configStorage, MatchEventListener eventListener) {
        this.configStorage = configStorage;
        this.eventListener = eventListener;

        this.httpClient = HttpClient.newHttpClient();
    }

    // ============================
    // Listener
    // ============================

    public void setConnectionStateListener(ConnectionStateListener listener) {

        this.connectionStateListener = listener;

        if (listener != null) {
            listener.onConnectionStateChanged(connectionState);
        }
    }

    // ============================
    // Control
    // ============================

    public synchronized void start() {

        if (running) {
            return;
        }

        running = true;

        setConnectionState(ConnectionState.CONNECTING);

        connectionThread = new Thread(this::connectionLoop, "RocketLeagueConnection");
        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    public synchronized void stop() {

        running = false;

        if (connectionThread != null) {
            connectionThread.interrupt();
            connectionThread = null;
        }

        WebSocket socket = webSocket;
        webSocket = null;

        if (socket != null) {

            try {
                socket.sendClose(WebSocket.NORMAL_CLOSURE, "Cliente detenido");
            } catch (Exception ignored) {
            }
        }

        setConnectionState(ConnectionState.DISCONNECTED);
    }

    // ============================
    // Conexión
    // ============================

    private void connectionLoop() {

        while (running && !Thread.currentThread().isInterrupted()) {

            try {

                System.out.println("Intentando conectar con Rocket League...");
                config = configStorage.load();
                setConnectionState(ConnectionState.CONNECTING);

                webSocket = httpClient.newWebSocketBuilder().buildAsync(
                        URI.create(config.getRocketLeagueUrl()),
                        new RocketLeagueListener(config.getPlayerName(), new MatchStorage(config.getStoragePath()), config.getPacketSendRate(),  eventListener, this::handleConnectionClosed))
                        .join();


                setConnectionState(ConnectionState.CONNECTED);

                while (running && webSocket != null) {
                    Thread.sleep(500);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;

            } catch (Exception e) {

                webSocket = null;
                if (!running) {
                    return;
                }

                System.out.println("Rocket League no está disponible. Reintentando en 2 segundos...");

                setConnectionState(ConnectionState.CONNECTING);

                try {
                    Thread.sleep(2000);

                } catch (InterruptedException interruptedException) {

                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private void handleConnectionClosed() {

        running = false;
        webSocket = null;

        setConnectionState(ConnectionState.DISCONNECTED);
    }

    // ============================
    // Estado
    // ============================

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public boolean isRunning() {
        return running;
    }

    private void setConnectionState(ConnectionState state) {

        if (connectionState == state) {
            return;
        }

        connectionState = state;
        ConnectionStateListener listener = connectionStateListener;

        if (listener != null) {
            listener.onConnectionStateChanged(state);
        }
    }
}
