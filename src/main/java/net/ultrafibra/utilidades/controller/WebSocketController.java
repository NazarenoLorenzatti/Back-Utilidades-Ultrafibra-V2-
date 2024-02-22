package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.model.Eventos;
import net.ultrafibra.utilidades.model.MessageAlert;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic")
    public MessageAlert evento(Eventos evento) {
        return new MessageAlert();
    }

}
