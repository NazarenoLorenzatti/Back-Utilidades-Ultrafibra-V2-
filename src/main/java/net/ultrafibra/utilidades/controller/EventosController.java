package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.model.Eventos;
import net.ultrafibra.utilidades.model.SNMPDevice;
import net.ultrafibra.utilidades.response.EventosResponseRest;
import net.ultrafibra.utilidades.service.impl.EventosServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
    "*",
    "http://45.230.65.207", 
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1/eventos")
public class EventosController {
    
    @Autowired
    private EventosServiceImpl eventosService;
    
    /**
     * Listar todos los eventos registrados
     * @return 
     */
    @GetMapping("/listar-eventos")
    public ResponseEntity<EventosResponseRest> listarEventos() {
        return eventosService.listarEventos();
    }
    
    /**
     * Registrar un evento por consultas SNMP
     * @param evento
     * @return 
     */
    @PostMapping("/registrar-evento")
     public ResponseEntity<EventosResponseRest> registrarEvento(@RequestBody Eventos evento) {
         return eventosService.registrarEvento(evento);
     }
     
     /**
      * Eliminar evento individual mediante su Id de la BBDD
      * @param idEvento
      * @return 
      */
     @DeleteMapping("/eliminar-evento/{idEvento}")
     public ResponseEntity<EventosResponseRest> eliminarEvento(@PathVariable Long idEvento){
         return eventosService.eliminarEvento(idEvento);
     }
     
     /**
      * Vaciar Log del dispositivo
      * @param dispositivo
      * @return 
      */
     @PostMapping("/eliminar-eventos")
      public ResponseEntity<EventosResponseRest> eliminarEventos(@RequestBody  SNMPDevice dispositivo){
         return eventosService.eliminarEventos(dispositivo);
     }
      
      /**
       * Listar todos los eventos registrados para el dispositivo
       * @param dispositivo
       * @return 
       */
     @PostMapping("/buscar-eventos")
     public ResponseEntity<EventosResponseRest> buscarEventosPorDispositivo(@RequestBody SNMPDevice dispositivo) {
         return eventosService.buscarEventosPorDispositivo(dispositivo);
     }
     
}
