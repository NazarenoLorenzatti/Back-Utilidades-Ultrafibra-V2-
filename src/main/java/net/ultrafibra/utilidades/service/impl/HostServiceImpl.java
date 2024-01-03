package net.ultrafibra.utilidades.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.dao.iHostDao;
import net.ultrafibra.utilidades.model.Datasets;
import net.ultrafibra.utilidades.model.Host;
import net.ultrafibra.utilidades.model.Log;
import net.ultrafibra.utilidades.response.HostResponseRest;
import net.ultrafibra.utilidades.response.LogResponseRest;
import net.ultrafibra.utilidades.service.iHostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private DatasetsServiceImpl dataService;

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

    @Override
    @Transactional()
    public ResponseEntity<HostResponseRest> hacerPing(String ipHost) {
        HostResponseRest respuesta = new HostResponseRest();
        List<Host> hosts = new ArrayList();
        Datasets datasets = new Datasets();
        boolean reachable;
        try {
            boolean registrarLog = true;
            try {
                InetAddress address = InetAddress.getByName(ipHost);
                reachable = address.isReachable(10000); // Ping con un timeout de 10 segundos
            } catch (IOException e) {
                reachable = false;
            }

            Host host = null;
            // Obtén el Host correspondiente a la IP
            Optional<Host> hostOptional = hostDao.findByIpHost(ipHost);
            if (hostOptional.isPresent()) {
                host = hostOptional.get();
                hosts.add(host);
            }

            if (null != host) {

                try {
                    ResponseEntity<LogResponseRest> logResponse = logService.buscarUltimoLogPorHost(host);
                    Log latestLog = logResponse.getBody().getLogResponse().getLogs().get(0);
                    if (latestLog.getFin() == null) {
                        registrarLog = false;
                    }
                } catch (Exception e) {
                    if (reachable) {
                        registrarLog = true;
                    }
                }

                if (reachable) {

                    if (registrarLog) {
                        Log log = new Log();
                        log.setHost(host);

                        // Guarda la data para el Grafico de Charts.js
                        datasets.setHost(host);
                        datasets.setFecha(java.sql.Date.valueOf(LocalDate.now()));
                        datasets.setValor(1);
                        dataService.registrarData(datasets);

                        Instant instant = Instant.now();
                        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                        localDateTime = localDateTime.withSecond(0).withNano(0); // Elimina segundos y milisegundos

                        Timestamp timestamp = Timestamp.valueOf(localDateTime);
                        log.setInicio(timestamp);
                        logService.registrarLog(log);
                        respuesta.getHostResponse().setHosts(hosts);
                        respuesta.setMetadata("Respuesta ok", "00", "Dispositivo Alcanzable");
                        return new ResponseEntity<>(respuesta, HttpStatus.OK);
                    } else {

                        // Guarda la data para el Grafico de Charts.js
                        datasets.setHost(host);
                        datasets.setFecha(java.sql.Date.valueOf(LocalDate.now()));
                        datasets.setValor(1);
                        dataService.registrarData(datasets);

                        respuesta.getHostResponse().setHosts(hosts);
                        respuesta.setMetadata("Respuesta ok", "00", "Dispositivo Alcanzable");
                        return new ResponseEntity<>(respuesta, HttpStatus.OK);
                    }

                } else {
                    // Dispositivo no alcanzable, actualiza el log existente con la fecha de fin
                    if (!registrarLog) {
                        ResponseEntity<LogResponseRest> logResponse = logService.buscarUltimoLogPorHost(host);
                        Log latestLog = logResponse.getBody().getLogResponse().getLogs().get(0);
                        if (latestLog != null && latestLog.getFin() == null) {
                            Instant instant = Instant.now();
                            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                            localDateTime = localDateTime.withSecond(0).withNano(0);
                            Timestamp timestamp = Timestamp.valueOf(localDateTime);
                            latestLog.setFin(timestamp);
                            long diferenciaEnMilisegundos = latestLog.getFin().getTime() - latestLog.getInicio().getTime();
                            double diferenciaEnHoras = diferenciaEnMilisegundos / 3600000.0;
                            DecimalFormat decimalFormat = new DecimalFormat("#.#");
                            latestLog.setDiferenciaEnHoras(Double.parseDouble(decimalFormat.format(diferenciaEnHoras).replace(",", ".")));
                            logService.establecerFin(latestLog);
                        }
                    }

                    // Guarda la data para el Grafico de Charts.js
                    datasets.setHost(host);
                    datasets.setFecha(java.sql.Date.valueOf(LocalDate.now()));
                    datasets.setValor(0);
                    dataService.registrarData(datasets);

                    respuesta.getHostResponse().setHosts(hosts);
                    respuesta.setMetadata("Respuesta nok", "-1", "Dispositivo no Alcanzable");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }
            } else {
                respuesta.getHostResponse().setHosts(hosts);
                respuesta.setMetadata("Respuesta nok", "-1", "Host no encontrado en la base de datos");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @Scheduled(fixedRate = 1800000)
//    public void liberarDatos() {
//        List<Datasets> listaDeDatos = dataService.listarData().getBody().getDatasetsResponse().getDatasets();
//        for (Datasets d : listaDeDatos) {
//
//            Date fechaAlmacenada = d.getFecha();
//            Date fechaActual = new Date(Calendar.getInstance().getTime().getTime());
//            long diferenciaEnMillis = fechaActual.getTime() - fechaAlmacenada.getTime();
//            long diferenciaEnDias = diferenciaEnMillis / (1000 * 60 * 60 * 24);
//
//            if (diferenciaEnDias > 7) {
//                dataService.eliminarData(d.getIdDataset());
//            }
//        }
//    }

}
