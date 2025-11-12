package com.safepasto.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/alertas")
    @SendTo("/topic/alertas")
    public String handleAlerta(String mensaje) {
        return "Alerta recibida: " + mensaje;
    }
}