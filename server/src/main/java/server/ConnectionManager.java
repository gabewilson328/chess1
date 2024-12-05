package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(String username, int gameID, Session session) {
        var connection = new Connection(username, session);
        ConcurrentHashMap<String, Connection> info = new ConcurrentHashMap<>();
        if (!connections.isEmpty()) {
            if (!connections.get(gameID).isEmpty()) {
                info = connections.get(gameID);
            }
        }
        info.put(username, connection);
        connections.put(gameID, info);
    }

    public void remove(String username, int gameID) {
        var info = connections.get(gameID);
        if (info != null) {
            info.remove(username);
        }
    }

    public void broadcast(String excludeUsername, int gameID, ServerMessage serverMessage) throws IOException {
        var serializer = new Gson();
        for (var c : connections.get(gameID).values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername)) {
                    c.send(serializer.toJson(serverMessage));
                }
            }
        }
    }

    public void sendToPlayer(String username, int gameID, ServerMessage serverMessage) throws IOException {
        var serializer = new Gson();
        var info = connections.get(gameID);
        var c = info.get(username);
        if (c.session.isOpen()) {
            if (c.username.equals(username)) {
                c.send(serializer.toJson(serverMessage));
            }
        }
    }
}
