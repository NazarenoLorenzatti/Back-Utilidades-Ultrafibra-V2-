package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.model.MessageAlert;
import net.ultrafibra.utilidades.model.MessaggePing;

public interface iWebSocketService {

    public void enviarMensaje(MessageAlert mensaje);

    public void enviarMensaje(MessaggePing mensaje);
}
