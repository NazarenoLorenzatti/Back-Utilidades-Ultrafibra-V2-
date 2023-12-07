package net.ultrafibra.cotrasenas.service;

import net.ultrafibra.cotrasenas.model.Eventos;
import net.ultrafibra.cotrasenas.model.SNMPDevice;
import net.ultrafibra.cotrasenas.response.EventosResponseRest;
import org.springframework.http.ResponseEntity;

public interface iEventosService {
    
    public ResponseEntity<EventosResponseRest> listarEventos();
    public ResponseEntity<EventosResponseRest> registrarEvento(Eventos evento);
    public ResponseEntity<EventosResponseRest> eliminarEvento(Long idEvento);
    public ResponseEntity<EventosResponseRest> eliminarEventos(SNMPDevice dispositivo);
    public ResponseEntity<EventosResponseRest> buscarEventosPorDispositivo(SNMPDevice dispositivo);
    
    
}

