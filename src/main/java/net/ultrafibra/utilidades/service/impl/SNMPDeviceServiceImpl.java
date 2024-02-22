package net.ultrafibra.utilidades.service.impl;

import net.ultrafibra.utilidades.model.*;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.dao.*;
import net.ultrafibra.utilidades.model.MessageAlert;
import net.ultrafibra.utilidades.response.SNMPDeviceResponseRest;
import net.ultrafibra.utilidades.service.iSNMPDeviceService;
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
    private iOidDao oidDao;

    @Autowired
    private SnmpClient snmpService;

    @Autowired
    private EventosServiceImpl eventoService;

    @Autowired
    private iHostDao hostDao;

    @Autowired
    private WebSocketServiceImpl webSocketService;

    private MessageAlert messageAlert;
    private Map<Long, Map<String, String>> alarmas = new HashMap<>();

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
                if (dispositivo.getHost().getIdHost() != null) {
                    Optional<Host> host = hostDao.findById(dispositivo.getHost().getIdHost());
                    dispositivo.setHost(host.get());
                } else {
                    dispositivo.setHost(null);
                }
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

    // Tarea Programada para el monitoreo del estado de cada UPS
//    @Async
//    @Scheduled(fixedDelay = 300000) // Intervalo de 5 minutos
    public void controlSnmp() throws Exception {
        for (SNMPDevice d : dispositivoDao.findAll()) {
            this.messageAlert = new MessageAlert();

            for (OidClass oid : oidDao.findAll()) {
                if (!oid.isAlarm()) {
                    String varResponse = null;
                    // Realiza la consulta SNMP y guarda el valor de la respuesta en *varResponse para comprobacion
                    ResponseEvent responseSnmp = snmpService.getSnmpData(d.getIpDispositivo(), d.getComunidad(), oid.getOid());
                    if (responseSnmp == null) {
                        continue;
                    } else {
                        varResponse = responseSnmp.getResponse().get(0).getVariable().toString();
                    }
                    Instant instant = Instant.now();
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    Timestamp timestamp = Timestamp.valueOf(localDateTime);
                    this.messageAlert.setDevice(d.getNombreDispositivo());
                    this.messageAlert.setDateAlert(timestamp.toString());
                    this.messageAlert.setIdDevice(d.getIdDispositivo());
                    if (varResponse != null) {
                        this.createSocketMessage(varResponse, oid);
                    }
                }
            }
            this.webSocketService.enviarMensajeSnmp(this.messageAlert);
        }
    }

    // Tarea Programada para el monitoreo de Alarmas SNMP de cada UPS
//    @Async
//    @Scheduled(fixedDelay = 120000) // Intervalo de 2 minutos
    public void monitorearAlarmas() throws Exception {
        for (SNMPDevice d : dispositivoDao.findAll()) {
            ResponseEvent responseSnmp = snmpService.getSnmpData(d.getIpDispositivo(), d.getComunidad(), "1.3.6.1.2.1.33.1.6.1.0");

            if (responseSnmp == null) {
                System.out.println("CONTINUE");
                continue;
            } else if(responseSnmp.getResponse().get(0).getVariable().toInt() <= 0){
                  System.out.println("CONTINUE");
                continue;
            }else {
                int valueVar = responseSnmp.getResponse().get(0).getVariable().toInt();
                System.out.println("cantidad de alarmas = " + valueVar);
                Map<String, String> alarmasOid = new HashMap<>();
                for (int i = 1; i <= valueVar; i++) {
                    String oid = "1.3.6.1.2.1.33.1.6.2.1.2." + i + ".0";
                    ResponseEvent responseAlarm = snmpService.getSnmpData(d.getIpDispositivo(), d.getComunidad(), oid);
                    if (responseAlarm != null) {
                        String alarmOid = responseAlarm.getResponse().get(0).getVariable().toString();
                        Optional<OidClass> oidOptional = oidDao.findByOid(alarmOid);

                        System.out.println("oidOptional = " + oidOptional.get().getEvento());
                        System.out.println("alarmOid = " + alarmOid);

                        alarmasOid.put(oid, oidOptional.get().getEvento());
                    } else {
                        System.out.println("RESPONSE ALARM NULL");
                    }
                }
                verificarCambiosAlarma(d, alarmasOid);
            }
        }
    }

    private void verificarCambiosAlarma(SNMPDevice d, Map<String, String> alarmasOid) {
        Map<String, String> alarmasAnteriores = alarmas.getOrDefault(d.getIdDispositivo(), new HashMap<>());
        Instant instant = Instant.now();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        Timestamp timestamp = Timestamp.valueOf(localDateTime);

        // Comparar las alarmas anteriores con las alarmas actuales
        for (Map.Entry<String, String> entry : alarmasOid.entrySet()) {
            String oid = entry.getKey();
            String descripcion = entry.getValue();
            String prioridad = "LOW";
            switch (descripcion) {
                case "upsAlarmOnBypass", "upsAlarmUpsSystemOff", "upsAlarmFanFailure", "upsAlarmFuseFailure" ->
                    prioridad = "WARN";
                case "upsAlarmBatteryBad", "upsAlarmOnBattery", "upsAlarmLowBattery", 
                        "upsAlarmDepletedBattery", "upsAlarmTempBad", "upsAlarmInputBad", "upsAlarmBypassBad",
                        "upsAlarmChargerFailed", "upsAlarmOutputBad", "upsAlarmOutputOverload", "upsAlarmUpsOutputOff", "upsAlarmShutdownImminent" ->
                    prioridad = "DANGER";
            }

            if (!alarmasAnteriores.containsKey(oid)) {
                System.out.println("ALARMA = Nueva alarma: " + descripcion + " " + prioridad);
                registrarEvento(d, timestamp, "Nueva alarma: " + descripcion, prioridad);
            }
        }

        // Comparar las alarmas actuales con las anteriores para detectar las que se han desactivado
        for (Map.Entry<String, String> entry : alarmasAnteriores.entrySet()) {
            String oid = entry.getKey();
            if (!alarmasOid.containsKey(oid)) {
                registrarEvento(d, timestamp, "Alarma resuelta: " + entry.getValue(), "LOW");
            }
        }
        alarmas.put(d.getIdDispositivo(), alarmasOid);
    }

    private void registrarEvento(SNMPDevice d, Timestamp timestamp, String log, String prioridad) {
        Eventos e = new Eventos();
        e.setFechaEvento(timestamp);
        e.setLogEvento(log);
        e.setSnmpDevice(d);
        e.setPrioridad(prioridad);
        this.eventoService.registrarEvento(e);
    }

    private void createSocketMessage(String varResponse, OidClass oid) {
        switch (oid.getEvento()) {
            case "Estado de la Bateria" -> {
                switch (varResponse) {
                    case "2" -> {
                        messageAlert.setBatteryStatus("Bateria Normal");
                    }
                    case "3" -> {
                        messageAlert.setBatteryStatus("Bateria Baja");
                    }
                    case "4" -> {
                        messageAlert.setBatteryStatus("Bateria Descargada");
                    }
                }
            }
            case "Salidad de Energia" -> {
                switch (varResponse) {
                    case "2" -> {
                        messageAlert.setOutputStatus("Sin Salida");
                    }
                    case "3" -> {
                        messageAlert.setOutputStatus("Salida Normal");
                    }
                    case "4" -> {
                        messageAlert.setOutputStatus("Bypass");
                    }
                    case "5" -> {
                        messageAlert.setOutputStatus("Salida por Bateria");
                    }
                }
            }
            case "Input 1" -> {
                messageAlert.setInput1(Integer.valueOf(varResponse));
            }
            case "Input 2" -> {
                messageAlert.setInput2(Integer.valueOf(varResponse));
            }
            case "Input 3" -> {
                messageAlert.setInput3(Integer.valueOf(varResponse));
            }
            case "Carga de Bateria" -> {
                messageAlert.setBatteryCharge(Integer.valueOf(varResponse));
            }
            case "Temperatura" -> {
                messageAlert.setTemp(Integer.valueOf(varResponse));
            }
            case "Tiempo de Carga estimado" -> {
                messageAlert.setTimeCharge(Integer.valueOf(varResponse));
            }
            case "Output 1" -> {
                messageAlert.setOutput1(Integer.valueOf(varResponse));
            }
            case "Output 2" -> {
                messageAlert.setOutput2(Integer.valueOf(varResponse));
            }
            case "Output 3" -> {
                messageAlert.setOutput3(Integer.valueOf(varResponse));
            }
            case "Bypass 1" -> {
                messageAlert.setBypass1(Integer.valueOf(varResponse));
            }
            case "Bypass 2" -> {
                messageAlert.setBypass2(Integer.valueOf(varResponse));
            }
            case "Bypass 3" -> {
                messageAlert.setBypass3(Integer.valueOf(varResponse));
            }
        }
    }

}
