package net.ultrafibra.utilidades.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.dao.iTecnicoDao;
import net.ultrafibra.utilidades.model.Tecnico;
import net.ultrafibra.utilidades.response.TecnicoResponseRest;
import net.ultrafibra.utilidades.service.iTecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TecnicoServiceImpl implements iTecnicoService {

    @Autowired
    private iTecnicoDao tecnicoDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<TecnicoResponseRest> listarTecnicos() {
        TecnicoResponseRest respuesta = new TecnicoResponseRest();
        try {
            List<Tecnico> tecnicos = tecnicoDao.findAll();
            respuesta.getTecnicoResponse().setTecnicos(tecnicos);
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
    public ResponseEntity<TecnicoResponseRest> guardarTecnico(Tecnico tecnico) {
        TecnicoResponseRest respuesta = new TecnicoResponseRest();
        List<Tecnico> listaTecnicos = new ArrayList<>();
        try {
            Tecnico tecniconGuardado = tecnicoDao.save(tecnico);
            if (tecniconGuardado != null) {
                listaTecnicos.add(tecniconGuardado);
                respuesta.getTecnicoResponse().setTecnicos(listaTecnicos);
                respuesta.setMetadata("Respuesta ok", "00", "Tecnico Guardado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el tecnico");
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
    public ResponseEntity<TecnicoResponseRest> eliminarTecnico(Long idTecnico) {
        TecnicoResponseRest respuesta = new TecnicoResponseRest();
        try {
            Optional<Tecnico> tecniconOptional = tecnicoDao.findById(idTecnico);
            if (tecniconOptional.isPresent()) {
                tecnicoDao.deleteById(idTecnico);
                respuesta.setMetadata("Respuesta ok", "00", "Tecnico Eliminado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo eliminar el tecnico");
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
    public ResponseEntity<TecnicoResponseRest> editarTecnico(Tecnico tecnico) {
        TecnicoResponseRest respuesta = new TecnicoResponseRest();
        List<Tecnico> listaTecnicos = new ArrayList<>();
        try {
            Tecnico tecnicoEditado = tecnicoDao.findById(tecnico.getIdTecnico()).get();
            if (tecnicoEditado != null) {
                tecnicoEditado.setNombreTecnico(tecnico.getNombreTecnico());
                tecnicoEditado.setTelefono(tecnico.getTelefono());
                tecnicoEditado = tecnicoDao.save(tecnicoEditado);
                if (tecnicoEditado != null) {
                    listaTecnicos.add(tecnicoEditado);
                    respuesta.getTecnicoResponse().setTecnicos(listaTecnicos);
                    respuesta.setMetadata("Respuesta ok", "00", "Tecnico Editado");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "Tecnico No Editado");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_MODIFIED);
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo editar el tecnico");
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
    public ResponseEntity<TecnicoResponseRest> buscarTecnico(Tecnico tecnico) {
        TecnicoResponseRest respuesta = new TecnicoResponseRest();
        List<Tecnico> listaTecnicos = new ArrayList<>();
        try {
            Tecnico tecnicoEncontrado = tecnicoDao.findById(tecnico.getIdTecnico()).get();
            if (tecnicoEncontrado != null) {
                listaTecnicos.add(tecnicoEncontrado);
                respuesta.getTecnicoResponse().setTecnicos(listaTecnicos);
                respuesta.setMetadata("Respuesta ok", "00", "Tecnico Encontrado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo encontrar el tecnico");
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
