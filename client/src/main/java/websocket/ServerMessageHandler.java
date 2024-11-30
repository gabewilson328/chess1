package websocket;

import websocket.messages.ServerMessage;

public interface ServerMessageHandler {
    void sendMessage(ServerMessage message);
}
