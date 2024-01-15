package net.ultrafibra.utilidades.service.impl;

import net.ultrafibra.utilidades.model.*;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.dao.iSNMPDeviceDao;
import net.ultrafibra.utilidades.model.MessageAlert;
import net.ultrafibra.utilidades.model.Tecnico;
import net.ultrafibra.utilidades.response.SNMPDeviceResponseRest;
import net.ultrafibra.utilidades.service.iSNMPDeviceService;
import net.ultrafibra.utilidades.util.EmailService;
import net.ultrafibra.utilidades.util.SMSService;
import net.ultrafibra.utilidades.util.SnmpClient;
import org.snmp4j.event.ResponseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private EventosServiceImpl eventosService;

    @Autowired
    private WebSocketServiceImpl webSocketService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TecnicoServiceImpl tecnicoService;

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
    public ResponseEntity<SNMPDeviceResponseRest> consultaSNMP(String ipDispositivo, String oidSolicitud, String telefono, String email) {
        SNMPDeviceResponseRest respuesta = new SNMPDeviceResponseRest();
        List<SNMPDevice> dispositivos = new ArrayList<>();
        try {
            if (ipDispositivo != null && oidSolicitud != null) {
                SNMPDevice d = dispositivoDao.findByIpDispositivo(ipDispositivo);
                ResponseEvent responseSnmp = snmpService.getSnmpData(d.getIpDispositivo(), d.getComunidad(), oidSolicitud);
                if (responseSnmp != null) {

                    VariableOid v = new VariableOid("0", "PRUEBA DE CONEXION", "LOW", d.getOids().get(0));
                    registrarEvento(d, v);
                    this.enviarAlertasPrueba(telefono, email);

                    dispositivos.add(d);
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

 //  @Async
  /*  @Scheduled(fixedDelay = 360000) // Intervalo de 6 minutos
    public void controlSnmp() throws Exception {
        for (SNMPDevice d : dispositivoDao.findAll()) {
            for (OidClass oid : d.getOids()) {
                // Realiza la consulta SNMP y guarda el valor de la respuesta en *varResponse para comprobacion
                ResponseEvent responseSnmp = snmpService.getSnmpData(d.getIpDispositivo(), d.getComunidad(), oid.getOid());
                String varResponse = responseSnmp.getResponse().get(0).getVariable().toString();
                VariableOid var = oid.getVariables().get(0);

                // Si el OID corresponde a un Input de energia
                if (oid.isInput()) {
                    if (Integer.parseInt(varResponse) >= Integer.parseInt(var.getValorVariable())) {
                        var.setSintaxisVariable(var.getSintaxisVariable() + " Input Normal, voltaje: " + varResponse);
                        var.setPrioridad("LOW");
                        this.registrarEvento(d, var);
                    } else if (Integer.parseInt(varResponse) < Integer.parseInt(var.getValorVariable())) {
                        var.setSintaxisVariable(var.getSintaxisVariable() + " ERROR EN INPUT, voltaje: " + varResponse);
                        this.registrarEvento(d, var);
                    }
                } else {
                    for (VariableOid v : oid.getVariables()) {
                        if (v.getValorVariable().equals(varResponse)) {
                            this.registrarEvento(d, v);
                        }
                    }
                }
            }
            Thread.sleep(5000);
        }
    }
    */
    private void enviarMensaje(Eventos evento, Tecnico t) {
        this.smsService.enviarMensaje(
                // CUERPO DE SMS
                evento.getSnmpDevice().getNombreDispositivo() + "\n"
                + evento.getLogEvento() + "\n"
                + evento.getFechaEvento(),
                // TELEFONO
                t.getTelefono());
    }

    private void enviarEmail(Eventos evento, Tecnico t) {
        this.emailService.enviarMail(
                // MAIL DESTINATARIO
                t.getEmail(),
                //ASUNTO
                evento.getSnmpDevice().getNombreDispositivo() + " " + evento.getLogEvento(),
                // CUERPO DE MAIL
                evento.getSnmpDevice().getNombreDispositivo() + "\n"
                + evento.getLogEvento() + "\n"
                + evento.getFechaEvento()
        );
    }

    private void socketMessage(Eventos evento) {
        // Envio de Mensaje Web Socket
        MessageAlert messageAlert = new MessageAlert(
                evento.getLogEvento(),
                evento.getSnmpDevice().getNombreDispositivo(),
                evento.getFechaEvento().toString(),
                evento.getSnmpDevice().getIdDispositivo(),
                evento.getPrioridad()
        );
        this.webSocketService.enviarMensaje(messageAlert);
    }

    private void registrarEvento(SNMPDevice d, VariableOid v) {
        // Fecha que se registra el evento
        Instant instant = Instant.now();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        Timestamp timestamp = Timestamp.valueOf(localDateTime);

        // Nuevo Evento
        Eventos nuevoEvento = new Eventos();
        nuevoEvento.setLogEvento(v.getSintaxisVariable());
        nuevoEvento.setNombreOid(v.getOid().getEvento());
        nuevoEvento.setOidEvento(v.getOid().getOid());
        nuevoEvento.setVariableEvento(v.getValorVariable());
        nuevoEvento.setSnmpDevice(d);
        nuevoEvento.setPrioridad(v.getPrioridad());
        nuevoEvento.setFechaEvento(timestamp);
        eventosService.registrarEvento(nuevoEvento);

        // Mensaje del Socket al Front
        this.socketMessage(nuevoEvento);
        // Se envian las alertas si no es una prueba
        if (!nuevoEvento.getLogEvento().contains("PRUEBA")) {
            this.enviarAlertas(nuevoEvento);
        }
    }

    private void enviarAlertas(Eventos nuevoEvento) {
        // Alertas por SMS o Mail segun corresponda
        for (Tecnico t : tecnicoService.listarTecnicos().getBody().getTecnicoResponse().getTecnicos()) {
            if (nuevoEvento.getPrioridad().equals("WARN")) {
                this.enviarEmail(nuevoEvento, t);
            } else if (nuevoEvento.getPrioridad().equals("DANGER")) {
                this.enviarEmail(nuevoEvento, t);
                this.enviarMensaje(nuevoEvento, t);
            }
        }
    }

    private void enviarAlertasPrueba(String telefono, String email) {
        this.smsService.enviarMensaje("AVISO DE PRUEBA", telefono);
        this.emailService.enviarMail(email, "MAIL DE PRUEBA", "MAIL DE PRUEBA");
    }

}
