package com.disasterconnect.websocket;

public record WebSocketEvent(
        String eventType,
        Long entityId,
        String message
) {
}