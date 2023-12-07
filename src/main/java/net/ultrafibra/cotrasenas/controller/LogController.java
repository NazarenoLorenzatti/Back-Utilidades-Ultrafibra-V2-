package net.ultrafibra.cotrasenas.controller;

import net.ultrafibra.cotrasenas.model.Host;
import net.ultrafibra.cotrasenas.model.Log;
import net.ultrafibra.cotrasenas.response.LogResponseRest;
import net.ultrafibra.cotrasenas.service.impl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
    "http://45.230.65.207",
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1/logs")
public class LogController {
    
    @Autowired
    private LogServiceImpl logService;
    
    /**
     * Registrar Log de ping
     * @param log
     * @return 
     */
    @PostMapping("/guardar-log")
    public ResponseEntity<LogResponseRest> registrarLog(@RequestBody Log log) {
        return logService.registrarLog(log);
    }
    
    /**
     * Eliminar el log en base al Id proporcionado
     * @param idLog
     * @return 
     */
    @DeleteMapping("/eliminar-log/{idLog}")
    public ResponseEntity<LogResponseRest> eliminarLogPorId(Long idLog) {
        return logService.eliminarLogPorId(idLog);
    }
    
      /**
     * Registrar Log de ping
     * @param log
     * @return 
     */
    @DeleteMapping("/eliminar-log")
    public ResponseEntity<LogResponseRest> eliminarLog(@RequestBody Log log) {
        return logService.eliminarLog(log);
    }
    
    /**
     * Buscar todos los Log del host 
     * @param host
     * @return 
     */
    @PostMapping("/buscar-log")
    public ResponseEntity<LogResponseRest> buscarLog(@RequestBody Host host){
        return logService.buscarLog(host);
    }
    
    /**
     * Eliminar los Logs del Host
     * @param host
     * @return 
     */
    @DeleteMapping("/eliminar-log-host")
    public ResponseEntity<LogResponseRest> eliminarLogDeHost(@RequestBody Host host){
        return logService.eliminarLogDeHost(host);
    }
    
    
}
