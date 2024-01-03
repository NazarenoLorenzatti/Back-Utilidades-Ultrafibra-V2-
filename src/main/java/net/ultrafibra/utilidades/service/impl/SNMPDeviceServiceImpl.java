package net.ultrafibra.utilidades.service.impl;

import net.ultrafibra.utilidades.model.OidClass;
import net.ultrafibra.utilidades.model.Eventos;
import net.ultrafibra.utilidades.model.SNMPDevice;
import net.ultrafibra.utilidades.model.VariableOid;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.dao.iSNMPDeviceDao;
import net.ultrafibra.utilidades.response.SNMPDeviceResponseRest;
import net.ultrafibra.utilidades.service.iSNMPDeviceService;
import net.ultrafibra.utilidades.util.SnmpClient;
import org.snmp4j.event.ResponseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SNMPDeviceServiceImpl implements iSNMPDeviceService {

    @Autowired
    private iSNMPDeviceDao dispositivoDao;

    @Autowired
    private SnmpClient snmpService;

    @Autowired
    private OidServiceImpl oidService;

    @Autowired
    private EventosServiceImpl eventosService;

    @Autowired
    private  WebSocketServiceImpl webSocketService;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<SNMPDeviceResponseRest> listarDispositivos() {
        SNMPDeviceResponseRest respuesta = new SNMPDeviceResponseRest();
        try {
            List<SNMPDevice> dispositivos = dispositivoDao.findAll();
            respuesta.getSnmpDeviceResponse().setSNMPDevices(dispositivos);
            respuesta.setMetadata("Respuesta ok", "00", "Lista de dispositivos cargados");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<SNMPDeviceResponseRest> buscarDispositivo(Long idDispositivo) {
        SNMPDeviceResponseRest respuesta = new SNMPDeviceResponseRest();
        List<SNMPDevice> dispositivos = new ArrayList<>();
        try {
            if (idDispositivo != null) {
                SNMPDevice dispositivoEncontrado = dispositivoDao.findById(idDispositivo).get();
                if (dispositivoEncontrado != null) {
                    dispositivos.add(dispositivoEncontrado);
                    respuesta.getSnmpDeviceResponse().setSNMPDevices(dispositivos);
                    respuesta.setMetadata("Respuesta ok", "00", "Dispositivo Encontrado");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se encuentra el dispositivo");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encuentra el dispositivo");
                return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
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
    public ResponseEntity<SNMPDeviceResponseRest> guardarDispositivo(SNMPDevice dispositivo) {
        SNMPDeviceResponseRest respuesta = new SNMPDeviceResponseRest();
        List<SNMPDevice> dispositivos = new ArrayList<>();
        try {
            if (dispositivo != null) {
                SNMPDevice dispositivoGuardado = dispositivoDao.save(dispositivo);
                if (dispositivoGuardado != null) {
                    dispositivos.add(dispositivoGuardado);
                    respuesta.getSnmpDeviceResponse().setSNMPDevices(dispositivos);
                    respuesta.setMetadata("Respuesta ok", "00", "Dispositivo Guardado");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se guardo el dispositivo");
                    return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encuentra el dispositivo");
                return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
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
    public ResponseEntity<SNMPDeviceResponseRest> eliminarDispositivo(Long idDispositivo) {
        SNMPDeviceResponseRest respuesta = new SNMPDeviceResponseRest();
        try {
            if (idDispositivo != null) {
                dispositivoDao.deleteById(idDispositivo);
                respuesta.setMetadata("Respuesta ok", "00", "Dispositivo Eliminado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encuentra el dispositivo");
                return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
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
    public ResponseEntity<SNMPDeviceResponseRest> editarDispositivo(SNMPDevice dispositivo) {
        SNMPDeviceResponseRest respuesta = new SNMPDeviceResponseRest();
        List<SNMPDevice> dispositivos = new ArrayList<>();
        try {
            if (dispositivo != null) {
                Optional<SNMPDevice> dispositivoEncontrado = dispositivoDao.findById(dispositivo.getIdDispositivo());
                if (dispositivoEncontrado.isPresent()) {
                    SNMPDevice dispositivoGuardado = dispositivoEncontrado.get();
                    dispositivoGuardado.setIpDispositivo(dispositivo.getIpDispositivo());
                    dispositivoGuardado.setNombreDispositivo(dispositivo.getNombreDispositivo());
                    dispositivoGuardado = dispositivoDao.save(dispositivoGuardado);
                    if (dispositivoGuardado != null) {
                        dispositivos.add(dispositivoGuardado);
                        respuesta.getSnmpDeviceResponse().setSNMPDevices(dispositivos);
                        respuesta.setMetadata("Respuesta ok", "00", "Dispositivo Guardado");
                    } else {
                        respuesta.setMetadata("Respuesta nok", "-1", "No se Guardo la edicion del dispositivo");
                        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se Encuentra el dispositivo");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encuentra el dispositivo");
                return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    // METODO PARA PROBAR LA CONEXION SNMP 
    @Override
    @Transactional()
    public ResponseEntity<SNMPDeviceResponseRest> consultaSNMP(SNMPDevice dispositivo, String oidSolicitud) {
        SNMPDeviceResponseRest respuesta = new SNMPDeviceResponseRest();
        List<SNMPDevice> dispositivos = new ArrayList<>();
        System.out.println("dispositivos = " + dispositivo.getIpDispositivo());
        try {
            if (dispositivo != null && oidSolicitud != null) {
                SNMPDevice dispositivoConsultado = dispositivoDao.findByIpDispositivo(dispositivo.getIpDispositivo());
                ResponseEvent responseSnmp = snmpService.getSnmpData(dispositivoConsultado.getIpDispositivo(), dispositivoConsultado.getComunidad(), oidSolicitud);
                if (responseSnmp != null) {
                    String respuestaSnmp = null;
                    var variables = oidService.buscarOidPorOid(oidSolicitud).getBody().getOidResponse().getOids().get(0).getVariables();
                    for (VariableOid v : variables) {
                        if (v.getValorVariable().equals(responseSnmp.getResponse().get(0).getVariable().toString())) {
                            respuestaSnmp = v.getSintaxisVariable();
                        }
                    }

                    // Guardamos el Evento de Prueba para mantener el registro
                    Eventos evento = new Eventos();
                    Instant instant = Instant.now();
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    Timestamp timestamp = Timestamp.valueOf(localDateTime);
                    evento.setFechaEvento(timestamp);
                    evento.setLogEvento("PRUEBA DE CONEXION");
                    evento.setSnmpDevice(dispositivoConsultado);
                    this.eventosService.registrarEvento(evento);
                    
                    // Envio de Mensaje Web Socket
                    this.webSocketService.enviarMensaje(evento.getLogEvento() + " " + evento.getFechaEvento().toString());
                    
                    dispositivos.add(dispositivoConsultado);
                    respuesta.getSnmpDeviceResponse().setSNMPDevices(dispositivos);
                    respuesta.setMetadata("Respuesta ok", "00", "Dispositivo Encontrado");
                    
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se encuentra el dispositivo");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encuentra el dispositivo");
                return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    /*
    @Async
    @Scheduled(fixedDelay = 5000) // Intervalo de 5 segundos (ajusta según tus necesidades)
    public void controlSnmp() throws Exception {
        for (SNMPDevice d : dispositivoDao.findAll()) {
            Eventos evento = eventosService.ultimoEvento(d);
            if (evento != null) {
                for (OidClass oid : d.getOids()) {
                    ResponseEvent responseSnmp = snmpService.getSnmpData(d.getIpDispositivo(), d.getComunidad(), oid.getOid());

                    // Si la variable es igual no hace nada por que significa que no hubo cambios con respecto al ultimo evento registrado
                    if (!evento.getVariableEvento().equals(responseSnmp.getResponse().get(0).getVariable().toString())) {
                        Eventos nuevoEvento = new Eventos();
                        for (VariableOid v : oid.getVariables()) {
                            if (v.getValorVariable().equals(responseSnmp.getResponse().get(0).getVariable().toString())) {
                                nuevoEvento.setLogEvento(v.getSintaxisVariable());
                                nuevoEvento.setVariableEvento(v.getValorVariable());
                            }
                        }
                        nuevoEvento.setSnmpDevice(d);
                        Instant instant = Instant.now();
                        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                        Timestamp timestamp = Timestamp.valueOf(localDateTime);

                        nuevoEvento.setFechaEvento(timestamp);
                        eventosService.registrarEvento(nuevoEvento);
                        this.messagingTemplate.convertAndSend("/topic/event-updates", "Nuevo evento registrado: " + nuevoEvento.getLogEvento());
                    }

                }
            } else {
                for (OidClass oid : d.getOids()) {
                    ResponseEvent responseSnmp = snmpService.getSnmpData(d.getIpDispositivo(), d.getComunidad(), oid.getOid());
                    Eventos nuevoEvento = new Eventos();
                    for (VariableOid v : oid.getVariables()) {
                        if (v.getValorVariable().equals(responseSnmp.getResponse().get(0).getVariable().toString())) {
                            nuevoEvento.setLogEvento(v.getSintaxisVariable());
                            nuevoEvento.setVariableEvento(v.getValorVariable());
                        }
                    }
                    nuevoEvento.setSnmpDevice(d);
                    Instant instant = Instant.now();
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    Timestamp timestamp = Timestamp.valueOf(localDateTime);

                    nuevoEvento.setFechaEvento(timestamp);
                    eventosService.registrarEvento(nuevoEvento);
                    this.messagingTemplate.convertAndSend("/topic/event-updates", "Nuevo evento registrado: " + nuevoEvento.getLogEvento());
                }
            }

        }
    }
*/
}
