package net.ultrafibra.cotrasenas.service.impl;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.cotrasenas.dao.iSNMPDeviceDao;
import net.ultrafibra.cotrasenas.model.*;
import net.ultrafibra.cotrasenas.response.SNMPDeviceResponseRest;
import net.ultrafibra.cotrasenas.service.iSNMPDeviceService;
import net.ultrafibra.cotrasenas.util.SnmpClient;
import org.snmp4j.event.ResponseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Override
    @Transactional()
    public ResponseEntity<SNMPDeviceResponseRest> consultaSNMP(SNMPDevice dispositivo, OidClass oidSolicitud) {
        SNMPDeviceResponseRest respuesta = new SNMPDeviceResponseRest();
        List<SNMPDevice> dispositivos = new ArrayList<>();
        try {
            if (dispositivo != null && oidSolicitud != null) {
                SNMPDevice dispositivoConsultado = dispositivoDao.findByIpDispositivo(dispositivo.getIpDispositivo());
                ResponseEvent responseSnmp = snmpService.getSnmpData(dispositivo.getIpDispositivo(), "Megalinkro", oidSolicitud.getOid());
                if (responseSnmp != null && dispositivoConsultado != null) {
                    Eventos nuevoEvento = new Eventos();
                    OidClass oid = oidService.buscarOidPorOid(oidSolicitud.getOid()).getBody().getOidResponse().getOids().get(0);

                    for (VariableOid v : oid.getVariables()) {
                        // Si el valor de la respuesta SNMP es igual a alguno de los guardados para el OID Solicitado
                        if (v.getValorVariable().equals(responseSnmp.getResponse().get(0).getVariable().toString())) {
                            nuevoEvento.setLogEvento(v.getSintaxisVariable());
                            nuevoEvento.setSnmpDevice(dispositivoConsultado);

                            Instant instant = Instant.now();
                            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                            Timestamp timestamp = Timestamp.valueOf(localDateTime);

                            nuevoEvento.setFechaEvento(timestamp);
                            eventosService.registrarEvento(nuevoEvento);
                        }
                    }

                    dispositivos.add(dispositivo);
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
}
