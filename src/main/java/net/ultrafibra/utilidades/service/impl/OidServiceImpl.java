package net.ultrafibra.utilidades.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.dao.iOidDao;
import net.ultrafibra.utilidades.dao.iSNMPDeviceDao;
import net.ultrafibra.utilidades.model.OidClass;
import net.ultrafibra.utilidades.model.SNMPDevice;
import net.ultrafibra.utilidades.response.OidResponseRest;
import net.ultrafibra.utilidades.service.iOidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OidServiceImpl implements iOidService {

    @Autowired
    private iOidDao oidDao;

    @Autowired
    private iSNMPDeviceDao snmpDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<OidResponseRest> listarOids() {
        OidResponseRest respuesta = new OidResponseRest();
        try {
            List<OidClass> oids = oidDao.findAll();
            respuesta.getOidResponse().setOids(oids);
            respuesta.setMetadata("Respuesta ok", "00", "Lista de Tecnicos cargados");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<OidResponseRest> cargarOid(OidClass oid) {
        OidResponseRest respuesta = new OidResponseRest();
        List<OidClass> listaOids = new ArrayList<>();
        try {
            if (oid != null) {
                Optional<SNMPDevice> snmpOptional = snmpDao.findById(oid.getSnmpDevice().getIdDispositivo());
                if (snmpOptional.isPresent()) {
                    oid.setSnmpDevice(snmpOptional.get());
                    OidClass oidGuardado = oidDao.save(oid);
                    if (oidGuardado != null) {
                        listaOids.add(oidGuardado);
                        respuesta.getOidResponse().setOids(listaOids);
                        respuesta.setMetadata("Respuesta ok", "00", "Oid Guardado");
                    } else {
                        respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid");
                        return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                    }

                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid, por que no se encontro el dispositivo SNMP");
                    return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid, por que no se envio ninguno");
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
    public ResponseEntity<OidResponseRest> editarOid(OidClass oid) {
        OidResponseRest respuesta = new OidResponseRest();
        List<OidClass> listaOids = new ArrayList<>();
        try {
            if (oid != null) {
                OidClass oidEncontrado = oidDao.findById(oid.getIdOid()).get();
                if (oidEncontrado != null) {
                    oidEncontrado.setEvento(oid.getEvento());
                    oidEncontrado.setOid(oid.getOid());
                    OidClass oidGuardado = oidDao.save(oidEncontrado);
                    if (oidGuardado != null) {
                        listaOids.add(oidGuardado);
                        respuesta.getOidResponse().setOids(listaOids);
                        respuesta.setMetadata("Respuesta ok", "00", "Oid Guardado");
                    } else {
                        respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid");
                        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
                    }

                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }

            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid, por que no se envio ninguno");
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
    public ResponseEntity<OidResponseRest> eliminarOid(Long idOid) {
        OidResponseRest respuesta = new OidResponseRest();
        System.out.println("OID RECIBIDO = " + idOid);
        try {
            if (idOid != null) {
                Optional<OidClass> oidEncontrado = oidDao.findById(idOid);
                if (oidEncontrado.isPresent()) {
                    OidClass o = oidEncontrado.get();
                    System.out.println("oidEncontrado = " + o.getEvento() + " " + o.getIdOid());
                    oidDao.delete(o);
                    oidDao.deleteById(idOid);
                    System.out.println("PASO LA ELIMINACION");
                    respuesta.setMetadata("Respuesta ok", "00", "Oid Eliminado");

                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }

            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid, por que no se envio ninguno");
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
    @Transactional(readOnly = true)
    public ResponseEntity<OidResponseRest> buscarOidPorEvento(String evento) {
        OidResponseRest respuesta = new OidResponseRest();
        List<OidClass> listaOids = new ArrayList<>();
        try {
            if (evento != null) {
                OidClass oidEncontrado = oidDao.findByEvento(evento);
                if (oidEncontrado != null) {
                    listaOids.add(oidEncontrado);
                    respuesta.getOidResponse().setOids(listaOids);
                    respuesta.setMetadata("Respuesta ok", "00", "Oid Guardado");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid, por que no se envio ninguno");
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
    @Transactional(readOnly = true)
    public ResponseEntity<OidResponseRest> buscarOidPorOid(String oid) {
        OidResponseRest respuesta = new OidResponseRest();
        List<OidClass> listaOids = new ArrayList<>();
        try {
            if (oid != null) {
                OidClass oidEncontrado = oidDao.findByOid(oid).get();
                if (oidEncontrado != null) {
                    listaOids.add(oidEncontrado);
                    respuesta.getOidResponse().setOids(listaOids);
                    respuesta.setMetadata("Respuesta ok", "00", "Oid Guardado");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid, por que no se envio ninguno");
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
    @Transactional(readOnly = true)
    public ResponseEntity<OidResponseRest> buscarOidPorEventoContiene(String contiene) {
        OidResponseRest respuesta = new OidResponseRest();
        List<OidClass> listaOids;
        try {
            if (contiene != null) {
                listaOids = oidDao.findByEventoContaining(contiene);
                if (listaOids != null) {
                    respuesta.getOidResponse().setOids(listaOids);
                    respuesta.setMetadata("Respuesta ok", "00", "Oid Guardado");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Oid, por que no se envio ninguno");
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
