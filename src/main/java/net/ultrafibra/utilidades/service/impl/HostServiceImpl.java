package net.ultrafibra.utilidades.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.dao.iHostDao;
import net.ultrafibra.utilidades.dao.iTecnicoDao;
import net.ultrafibra.utilidades.model.*;
import net.ultrafibra.utilidades.response.HostResponseRest;
import net.ultrafibra.utilidades.service.iHostService;
import net.ultrafibra.utilidades.util.EmailService;
import net.ultrafibra.utilidades.util.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class HostServiceImpl implements iHostService {

    @Autowired
    private iHostDao hostDao;

    @Autowired
    private LogServiceImpl logService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WebSocketServiceImpl webSocketService;

    @Autowired
    private iTecnicoDao tecnicoDao;

    private Map<String, Boolean> hostStatusMap = new HashMap<>();
    private Map<String, Long> logStartTimeMap = new HashMap<>();

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<HostResponseRest> listarHosts() {
        HostResponseRest respuesta = new HostResponseRest();
        try {
            List<Host> hosts = (List<Host>) hostDao.findAll();
            respuesta.getHostResponse().setHosts(hosts);
            respuesta.setMetadata("Respuesta ok", "00", "Lista de Host cargados");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<HostResponseRest> guardarHost(Host host) {
        HostResponseRest respuesta = new HostResponseRest();
        List<Host> listaHosts = new ArrayList<>();
        try {
            Host hostGuardado = hostDao.save(host);
            if (hostGuardado != null) {
                listaHosts.add(hostGuardado);
                respuesta.getHostResponse().setHosts(listaHosts);
                respuesta.setMetadata("Respuesta ok", "00", "Aplicacion Guardada");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar la aplicacion");
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
    public ResponseEntity<HostResponseRest> eliminarHost(Long idHost) {
        HostResponseRest respuesta = new HostResponseRest();
        List<Host> listaHosts = new ArrayList<>();
        try {
            Optional<Host> hostEncontrado = hostDao.findById(idHost);
            if (hostEncontrado.isPresent()) {
                listaHosts.add(hostEncontrado.get());
                respuesta.getHostResponse().setHosts(listaHosts);
                hostDao.delete(hostEncontrado.get());
                respuesta.setMetadata("Respuesta ok", "00", "Se Elimino el Host");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo eliminar el Host");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al intentar eliminar el host");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<HostResponseRest> editarHost(Host host) {
        HostResponseRest respuesta = new HostResponseRest();
        List<Host> listaHosts = new ArrayList<>();
        try {
            Host hostGuardado = hostDao.save(host);
            if (hostGuardado != null) {
                listaHosts.add(hostGuardado);
                respuesta.getHostResponse().setHosts(listaHosts);
                respuesta.setMetadata("Respuesta ok", "00", "Host Editada");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo editar el Host");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "No se pudo editar el Host");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<HostResponseRest> buscarHostPorIp(String ipHost) {
        HostResponseRest respuesta = new HostResponseRest();
        List<Host> listaHosts = new ArrayList<>();
        try {
            Optional<Host> aplicacionOptional = hostDao.findByIpHost(ipHost);
            if (aplicacionOptional.isPresent()) {
                listaHosts.add(aplicacionOptional.get());
                respuesta.getHostResponse().setHosts(listaHosts);
                respuesta.setMetadata("Respuesta ok", "00", "Apliacion encontrada");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro la aplicacion");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public void pingHost(Host h) {
        try {
            InetAddress address = InetAddress.getByName(h.getIpHost());
            boolean reachable = address.isReachable(10000);
            this.socketMessage(h, reachable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    @Scheduled(fixedDelay = 120000) // Intervalo de 2 minutos
    public void ping() {
        for (Host h : hostDao.findAll()) {
            boolean reachable;
            try {
                InetAddress address = InetAddress.getByName(h.getIpHost());
                reachable = address.isReachable(10000);

                if (reachable && !hostStatusMap.getOrDefault(h.getIpHost(), false)) { // Si estaba desconectado y ahora está conectado

                    logStartTimeMap.put(h.getIpHost(), System.currentTimeMillis());
                    this.socketMessage(h, reachable);
                    enviarAlertas("Encendido - "+h.getNombreHost(), h.getNombreHost() + " Encendido " + System.currentTimeMillis());

                } else if (!reachable && hostStatusMap.getOrDefault(h.getIpHost(), false)) { // Si estaba conectado y ahora está desconectado

                    //Registrar Evento
                    long logStartTime = logStartTimeMap.getOrDefault(h.getIpHost(), 0L);
                    long logEndTime = System.currentTimeMillis();
                    this.registrarEvento(h, logStartTime, logEndTime);
                    //Envio informacion Socket
                    this.socketMessage(h, reachable);
                    enviarAlertas("Apagado - "+h.getNombreHost(), h.getNombreHost() + " Apagado " + System.currentTimeMillis());
                }

                hostStatusMap.put(h.getIpHost(), reachable);
            } catch (IOException e) {

                if (hostStatusMap.getOrDefault(h.getIpHost(), false)) {
                    long logStartTime = logStartTimeMap.getOrDefault(h.getIpHost(), 0L);
                    long logEndTime = System.currentTimeMillis();
                    this.registrarEvento(h, logStartTime, logEndTime);
                    this.socketMessage(h, false);
                }
                hostStatusMap.put(h.getIpHost(), false);
            }

        }
    }

    private void registrarEvento(Host h, long inicio, long fin) {
        Log log = new Log();
        double diferenciaEnMinutos = (double) (fin - inicio) / (60 * 1000);
        log.setHost(h);
        log.setInicio(new Timestamp(inicio));
        log.setFin(new Timestamp(fin));
        log.setDiferenciaEnHoras(diferenciaEnMinutos);
        logService.registrarLog(log);
    }

    private void socketMessage(Host h, boolean racheable) {
        // Envio de Mensaje Web Socket
        MessaggePing messaggePing = new MessaggePing(
                h.getIdHost(),
                h.getIpHost(),
                h.getNombreHost(),
                racheable
        );
        this.webSocketService.enviarMensaje(messaggePing);
    }

    private void enviarAlertas(String aviso, String asunto) {
        for (Tecnico t : tecnicoDao.findAll()) {
            emailService.enviarMail(t.getEmail(), asunto, aviso);
            smsService.enviarMensaje(aviso, t.getTelefono());
        }
    }
}
