package com.kurt.gym.config.socketIO;

import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.kurt.gym.config.socketIO.Message.MessageType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SocketService {

    public void sendMessage(String room,String eventName, SocketIOClient senderClient, String message) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName,
                        new Message(MessageType.SERVER, message));
            }
        }
    }

}