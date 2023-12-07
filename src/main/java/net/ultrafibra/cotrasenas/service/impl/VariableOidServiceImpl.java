package net.ultrafibra.cotrasenas.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.cotrasenas.dao.iOidDao;
import net.ultrafibra.cotrasenas.dao.iVariableOidDao;
import net.ultrafibra.cotrasenas.model.OidClass;
import net.ultrafibra.cotrasenas.model.VariableOid;
import net.ultrafibra.cotrasenas.response.VariableOidResponseRest;
import net.ultrafibra.cotrasenas.service.iVariableOidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class VariableOidServiceImpl implements iVariableOidService {

    @Autowired
    private iVariableOidDao variableDao;

    @Autowired
    private iOidDao oidDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<VariableOidResponseRest> listarVariables() {
        VariableOidResponseRest respuesta = new VariableOidResponseRest();
        try {
            List<VariableOid> variables = variableDao.findAll();
            respuesta.getVariableResponse().setVariables(variables);
            respuesta.setMetadata("Respuesta ok", "00", "Lista de Variables cargadas");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);

    }

    @Override
    @Transactional()
    public ResponseEntity<VariableOidResponseRest> guardarVariable(OidClass oid, VariableOid variable) {
        VariableOidResponseRest respuesta = new VariableOidResponseRest();
        List<VariableOid> listaVariables = new ArrayList<>();
        try {
            if (oid != null && variable != null) {
                VariableOid variableGuardada = variableDao.save(variable);
                if (variableGuardada != null) {
                    listaVariables.add(variableGuardada);
                    respuesta.getVariableResponse().setVariables(listaVariables);
                    respuesta.setMetadata("Respuesta ok", "00", "Varaible Guardada");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar la variable");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }

            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar la varible, por que no se envio ninguna");
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
    public ResponseEntity<VariableOidResponseRest> editarVariable(VariableOid variable) {
        VariableOidResponseRest respuesta = new VariableOidResponseRest();
        List<VariableOid> listaVariables = new ArrayList<>();
        try {
            if (variable != null) {
                Optional<VariableOid> variableOptional = variableDao.findById(variable.getIdVariable());
                if (variableOptional.isPresent()) {
                    VariableOid variableEditada = variableOptional.get();
                    variableEditada.setValorVariable(variable.getValorVariable());
                    variableEditada.setSintaxisVariable(variable.getSintaxisVariable());
                    variableEditada = variableDao.save(variableEditada);
                    if (variableEditada != null) {
                        listaVariables.add(variableEditada);
                        respuesta.getVariableResponse().setVariables(listaVariables);
                        respuesta.setMetadata("Respuesta ok", "00", "Varaible Editada");
                    } else {
                        respuesta.setMetadata("Respuesta nok", "-1", "ocurrio un error al intentar guardar la edicion");
                        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
                    }

                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se pudo editar la variable por que no se encuentra");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }

            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo editar la variable por que el valor enviado es nulo");
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
    public ResponseEntity<VariableOidResponseRest> eliminarVariable(Long idVariable) {
        VariableOidResponseRest respuesta = new VariableOidResponseRest();
        try {
            if (idVariable != null) {
                variableDao.deleteById(idVariable);
                respuesta.setMetadata("Respuesta ok", "00", "Varaible Eliminada");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo eliminar la varible, por que no se envio ninguna");
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
    public ResponseEntity<VariableOidResponseRest> buscarVariableDeOid(OidClass oid) {
        VariableOidResponseRest respuesta = new VariableOidResponseRest();
        List<VariableOid> listaVariables;
        try {
            if (oid != null) {
                
                //Se busca por Oid o por Id segun lo que se envie como parametro
                Optional<OidClass> oidOptional = null;
                if(oid.getIdOid()!= null) {
                    oidOptional = oidDao.findById(oid.getIdOid());
                }else if(oid.getOid() != null){
                    oidOptional = oidDao.findByOid(oid.getOid());
                }
                
                if (oidOptional.isPresent()) {
                    listaVariables = (oidOptional.get().getVariables());
                    respuesta.getVariableResponse().setVariables(listaVariables);
                    respuesta.setMetadata("Respuesta ok", "00", "Lista de variables del Oid");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se encontro el OID");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }

            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo encontrar la lista de variables por que el valor enviado es nulo");
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
