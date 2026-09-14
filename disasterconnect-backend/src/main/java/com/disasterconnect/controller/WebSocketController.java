package com.disasterconnect.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public String sendTestMessage(String message) {
        return message;
    }
}