package net.ultrafibra.utilidades.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.service.iWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebSocketServiceImpl implements iWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void enviarMensaje(String mensaje) {
        messagingTemplate.convertAndSend("/topic/snmp-notifications", mensaje);
    }

}
