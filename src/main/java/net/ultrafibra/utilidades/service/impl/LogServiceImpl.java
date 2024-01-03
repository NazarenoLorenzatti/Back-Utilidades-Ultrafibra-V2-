package net.ultrafibra.utilidades.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.dao.iHostDao;
import net.ultrafibra.utilidades.dao.iLogDao;
import net.ultrafibra.utilidades.model.Host;
import net.ultrafibra.utilidades.model.Log;
import net.ultrafibra.utilidades.response.LogResponseRest;
import net.ultrafibra.utilidades.service.iLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LogServiceImpl implements iLogService {

    @Autowired
    private iLogDao logDao;
    
    @Autowired
    private iHostDao hostDao;

    @Override
    @Transactional()
    public ResponseEntity<LogResponseRest> registrarLog(Log log) {
        LogResponseRest respuesta = new LogResponseRest();
        List<Log> listaLogs = new ArrayList<>();
        try {
            Log logGuardado = logDao.save(log);
            if (logGuardado != null) {
                listaLogs.add(logGuardado);
                respuesta.getLogResponse().setLogs(listaLogs);
                respuesta.setMetadata("Respuesta ok", "00", "Log registrado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo registrar el Log");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<LogResponseRest> eliminarLog(Log log) {
        LogResponseRest respuesta = new LogResponseRest();
        try {
            if (log != null) {
                logDao.delete(log);
                respuesta.setMetadata("Respuesta ok", "00", "Log Eliminado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro el Log a eliminar");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<LogResponseRest> buscarLog(Host host) {
        LogResponseRest respuesta = new LogResponseRest();
        List<Log> listaLogs;
        try {
            if (host != null) {
                listaLogs = logDao.findByHost(host);
                respuesta.getLogResponse().setLogs(listaLogs);
                respuesta.setMetadata("Respuesta ok", "00", "Logs encontrador para el host");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo encontrar el Log");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<LogResponseRest> buscarUltimoLogPorHost(Host host) {
        LogResponseRest respuesta = new LogResponseRest();
        List<Log> listaLogs = new ArrayList();
        try {
            if (host != null) {
                for (Log l : logDao.findByHost(host)) {
                    if (l.getFin() == null) {
                        listaLogs.add(l);
                        respuesta.getLogResponse().setLogs(listaLogs);
                        respuesta.setMetadata("Respuesta ok", "00", "ULtimo Logs encontrado para el host");
                    }
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo encontrar el Log");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<LogResponseRest> establecerFin(Log latestLog) {
        LogResponseRest respuesta = new LogResponseRest();
        List<Log> listaLogs = new ArrayList();
        try {
            if (latestLog != null) {
                Optional<Log> logEncontrado = logDao.findById(latestLog.getIdLog());
                if (logEncontrado.isPresent()) {
                    listaLogs.add(logDao.save(logEncontrado.get()));
                    respuesta.getLogResponse().setLogs(listaLogs);
                    respuesta.setMetadata("Correcto", "00", "Se Registro el Log");
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo encontrar el Log");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<LogResponseRest> eliminarLogDeHost(Host host) {
        LogResponseRest respuesta = new LogResponseRest();
        try {
            if (host != null) {
                logDao.deleteByHost(host);
                respuesta.setMetadata("Respuesta ok", "00", "Se Eliminaron los log");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudieron eliminar Los Logs");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al intentar eliminar los Logs");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<LogResponseRest> eliminarLogPorId(Long idLog) {
        LogResponseRest respuesta = new LogResponseRest();
        try {
            Optional<Log> logEncontrado = logDao.findById(idLog);
            if (logEncontrado.isPresent()) {
                logDao.deleteById(logEncontrado.get().getIdLog());
                respuesta.setMetadata("Respuesta ok", "00", "Se Elimino los log");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo eliminar el Log");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al intentar eliminar el Log");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<LogResponseRest> buscarLogPorMes(Host host) {
        LogResponseRest respuesta = new LogResponseRest();
        List<Log> listaLogs = new ArrayList();
        java.util.Date dateUtil = new java.util.Date();
        try {
            if (host != null) {
                for(Log l: logDao.findByHost(hostDao.findById(host.getIdHost()).get())){
                    if(l.getInicio().getMonth() == dateUtil.getMonth() && l.getInicio().getYear() == dateUtil.getYear()){
                      
                        listaLogs.add(l);
                    }
                }
                respuesta.getLogResponse().setLogs(listaLogs);
                respuesta.setMetadata("Respuesta ok", "00", "Logs encontrador para el host");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo encontrar el Log");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

}
