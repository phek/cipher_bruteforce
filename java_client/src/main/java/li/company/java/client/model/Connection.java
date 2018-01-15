package li.company.java.client.model;

import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Connection {

    private Socket socket = null;
    private boolean connected = false;
    private SocketClient client;
    private String curServer = "";
    private boolean exit = false;

    public Connection(SocketClient client) {
        this.client = client;
    }

    public void connect(String server) {
        if (socket != null) {
            socket.disconnect();
        }

        try {
            socket = IO.socket(server);
            curServer = server;
            socket.on(Socket.EVENT_CONNECT, (Object... args) -> {
                connected = true;
                client.printMessage("Successfully connected to " + curServer);

                socket.emit("request_decode");
                client.printMessage("Brute force started");

            }).on("decode_this", (Object... args) -> {
                try {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray results = Crypter.crackB64VigenereCypher(data.getString("text"), data.getInt("start"), data.getInt("stop"));
                    socket.emit("decode_result", new JSONObject()
                            .put("text", data.getString("text"))
                            .put("result", results)
                            .put("start", data.getString("start"))
                            .put("stop", data.getString("stop")));

                    if (exit) {
                        disconnect();
                        System.exit(0);
                    } else {
                        socket.emit("request_decode");
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                }

            }).on("decode_found", (Object... args) -> {
                try {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray results = data.getJSONArray("result");
                    for (int i = 0; i < results.length(); i++) {
                        client.printMessage(
                                results.getJSONObject(i).getString("key") + ": "
                                + results.getJSONObject(i).getString("value"));
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                }

            }).on("results", (Object... args) -> {
                try {
                    JSONObject data = (JSONObject) args[0];
                    if (data.length() == 0) {
                        client.printMessage("No results found");
                    }
                    Iterator<String> keys = data.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        client.printMessage(key + ": " + data.getString(key));
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                }

            }).on("current", (Object... args) -> {
                client.printMessage("Current key: " + args[0]);

            }).on("exit", (Object... args) -> {
                System.exit(0);

            }).on(Socket.EVENT_DISCONNECT, (Object... args) -> {
                connected = false;
                client.printError("Disconnected");
            });

            socket.connect();
        } catch (URISyntaxException ex) {
            client.printError("Failed to connect to socket");
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void exit() {
        if (isConnected()) {
            exit = true;
        } else {
            System.exit(0);
        }
    }

    private void disconnect() {
        socket.disconnect();
    }

    public void requestResults() {
        socket.emit("request_results");
    }

    public void requestCurrent() {
        socket.emit("request_current");
    }
}
