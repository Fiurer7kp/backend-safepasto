package com.safepasto.service.impl;

import com.safepasto.model.dto.AlertaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendAlertNotification(AlertaResponse alerta) {
        messagingTemplate.convertAndSend("/topic/alertas", alerta);
    }
}
