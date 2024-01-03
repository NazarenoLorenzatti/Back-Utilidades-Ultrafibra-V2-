package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.model.Eventos;
import net.ultrafibra.utilidades.model.SNMPDevice;
import net.ultrafibra.utilidades.response.EventosResponseRest;
import org.springframework.http.ResponseEntity;

public interface iEventosService {
    
    public ResponseEntity<EventosResponseRest> listarEventos();
    public ResponseEntity<EventosResponseRest> registrarEvento(Eventos evento);
    public ResponseEntity<EventosResponseRest> eliminarEvento(Long idEvento);
    public ResponseEntity<EventosResponseRest> eliminarEventos(SNMPDevice dispositivo);
    public ResponseEntity<EventosResponseRest> buscarEventosPorDispositivo(SNMPDevice dispositivo);
    
    
}

