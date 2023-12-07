package net.ultrafibra.cotrasenas.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.cotrasenas.dao.iOidDao;
import net.ultrafibra.cotrasenas.model.OidClass;
import net.ultrafibra.cotrasenas.response.OidResponseRest;
import net.ultrafibra.cotrasenas.service.iOidService;
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
        try {
            if (idOid != null) {
                OidClass oidEncontrado = oidDao.findById(idOid).get();
                if (oidEncontrado != null) {
                    oidDao.delete(oidEncontrado);
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
    public ResponseEntity<OidResponseRest> buscarOidPorEvento(String evento) {
        OidResponseRest respuesta = new OidResponseRest();
        List<OidClass> listaOids = new ArrayList<>();
        try {
            if (evento != null) {
                OidClass oidEncontrado = oidDao.findByEvento(evento);
                if (oidEncontrado != null) {
                    listaOids.add( oidEncontrado);                    
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
                    listaOids.add( oidEncontrado);                    
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
