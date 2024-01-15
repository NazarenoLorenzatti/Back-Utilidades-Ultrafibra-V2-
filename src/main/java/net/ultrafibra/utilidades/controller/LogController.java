package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.model.Host;
import net.ultrafibra.utilidades.model.Log;
import net.ultrafibra.utilidades.response.LogResponseRest;
import net.ultrafibra.utilidades.service.impl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
    "*",
    "http://45.230.65.207",
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1/logs")
public class LogController {

    @Autowired
    private LogServiceImpl logService;

    /**
     * Registrar Log de ping
     *
     * @param log
     * @return
     */
    @PostMapping("/guardar-log")
    public ResponseEntity<LogResponseRest> registrarLog(@RequestBody Log log) {
        return logService.registrarLog(log);
    }

    /**
     * Eliminar el log en base al Id proporcionado
     *
     * @param idLog
     * @return
     */
    @DeleteMapping("/eliminar-log/{idLog}")
    public ResponseEntity<LogResponseRest> eliminarLogPorId(Long idLog) {
        return logService.eliminarLogPorId(idLog);
    }

    /**
     * Registrar Log de ping
     *
     * @param log
     * @return
     */
    @DeleteMapping("/eliminar-log")
    public ResponseEntity<LogResponseRest> eliminarLog(@RequestBody Log log) {
        return logService.eliminarLog(log);
    }

    /**
     * Buscar todos los Log del host
     *
     * @param host
     * @return
     */
    @PostMapping("/buscar-log")
    public ResponseEntity<LogResponseRest> buscarLog(@RequestBody Host host) {
        return logService.buscarLog(host);
    }

    /**
     * Eliminar los Logs del Host
     *
     * @param host
     * @return
     */
    @PostMapping("/eliminar-log-host")
    public ResponseEntity<LogResponseRest> eliminarLogDeHost(@RequestBody Host host) {
        return logService.eliminarLogDeHost(host);
    }

    /**
     * Buscar Log por mes Actual
     *
     * @param host
     * @return
     */
    @PostMapping("/buscar-log-mes")
    public ResponseEntity<LogResponseRest> buscarLogPorMes(@RequestBody Host host) {
        return logService.buscarLogPorMes(host);
    }
    
    /**
     * Buscar el ultimo log del host
     * @param host
     * @return 
     */
    @PostMapping("/ultimo-log")
    public ResponseEntity<LogResponseRest> buscarUltimoLogPorHost(@RequestBody Host host) {
        return logService.buscarUltimoLogPorHost(host);
    }
}
