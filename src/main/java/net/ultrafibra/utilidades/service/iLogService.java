package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.model.Host;
import net.ultrafibra.utilidades.model.Log;
import net.ultrafibra.utilidades.response.LogResponseRest;
import org.springframework.http.ResponseEntity;

public interface iLogService {

    public ResponseEntity<LogResponseRest> registrarLog(Log log);

    public ResponseEntity<LogResponseRest> eliminarLog(Log log);

    public ResponseEntity<LogResponseRest> buscarLog(Host host);

    public ResponseEntity<LogResponseRest> buscarUltimoLogPorHost(Host host);

    public ResponseEntity<LogResponseRest> establecerFin(Log latestLog);
    
    public ResponseEntity<LogResponseRest> eliminarLogDeHost(Host host);
    
    public ResponseEntity<LogResponseRest> eliminarLogPorId(Long idLog);
    
    public  ResponseEntity<LogResponseRest> buscarLogPorMes(Host host);
}
