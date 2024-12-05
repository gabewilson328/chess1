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
        info.put(username, connection);
        connections.put(gameID, info);
    }

    public void remove(int gameID) {
        connections.remove(gameID);
    }

    public void broadcast(String excludeUsername, int gameID, ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        var serializer = new Gson();
        for (var c : connections.get(gameID).values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername)) {
                    c.send(serializer.toJson(serverMessage));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            if (!c.username.equals(excludeUsername))
                connections.remove(c.username);
        }
    }

    public void broadcastToPlayer(String username, int gameID, ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
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
