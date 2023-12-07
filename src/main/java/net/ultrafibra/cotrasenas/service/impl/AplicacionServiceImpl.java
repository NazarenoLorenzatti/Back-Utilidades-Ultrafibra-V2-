package net.ultrafibra.cotrasenas.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.cotrasenas.dao.iAdministrativoDao;
import net.ultrafibra.cotrasenas.dao.iAplicacionDao;
import net.ultrafibra.cotrasenas.dao.iUsuarioDao;
import net.ultrafibra.cotrasenas.model.Administrativo;
import net.ultrafibra.cotrasenas.model.Aplicacion;
import net.ultrafibra.cotrasenas.response.AplicacionResponseRest;
import net.ultrafibra.cotrasenas.service.iAplicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AplicacionServiceImpl implements iAplicacionService {

    @Autowired
    private iAplicacionDao aplicacionDao;
    
    @Autowired
    private iAdministrativoDao administrativoDao;
    
    @Autowired
    private iUsuarioDao usuarioDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<AplicacionResponseRest> listarAplicaciones() {
        AplicacionResponseRest respuesta = new AplicacionResponseRest();
        try {
            List<Aplicacion> aplicaciones = (List<Aplicacion>) aplicacionDao.findAll();
            respuesta.getAplicacionResponse().setAplicacion(aplicaciones);
            respuesta.setMetadata("Respuesta ok", "00", "Lista de Aplicaciones cargados");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<AplicacionResponseRest> buscarAplicacion(Aplicacion aplicacion) {
        AplicacionResponseRest respuesta = new AplicacionResponseRest();
        List<Aplicacion> listaAplicaciones = new ArrayList<>();
        try {
            Optional<Aplicacion> aplicacionOptional = aplicacionDao.findById(aplicacion.getIdAplicacion());
            if (aplicacionOptional.isPresent()) {
                listaAplicaciones.add(aplicacionOptional.get());
                respuesta.getAplicacionResponse().setAplicacion(listaAplicaciones);
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
    @Transactional
    public ResponseEntity<AplicacionResponseRest> guardarAplicacion(Aplicacion aplicacion, String username) {
        AplicacionResponseRest respuesta = new AplicacionResponseRest();
        List<Aplicacion> listaAplicaciones = new ArrayList<>();
        try {
            List<Administrativo> listAdm = new ArrayList<>();
            listAdm.add(administrativoDao.findByUsuario(usuarioDao.findByUsername(username)).get());
            aplicacion.setAdministrativos(listAdm);
            Aplicacion aplicacionGuardada = aplicacionDao.save(aplicacion);
            if (aplicacionGuardada != null) {         
                listaAplicaciones.add(aplicacionGuardada);
                respuesta.getAplicacionResponse().setAplicacion(listaAplicaciones);
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
    @Transactional
    public ResponseEntity<AplicacionResponseRest> eliminarAplicacion(Long idAplicacion) {
        AplicacionResponseRest respuesta = new AplicacionResponseRest();
        try {
            aplicacionDao.deleteById(idAplicacion);
            respuesta.setMetadata("Respuesta ok", "00", "Aplicacion eliminada");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al eliminar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<AplicacionResponseRest> editarAplicacion(Aplicacion aplicacion) {
        AplicacionResponseRest respuesta = new AplicacionResponseRest();
        List<Aplicacion> listaAplicaciones = new ArrayList<>();
        try {
            Optional<Aplicacion> aplicacionOptional = aplicacionDao.findById(aplicacion.getIdAplicacion());
            if (aplicacionOptional.isPresent()) {

                aplicacionOptional.get().setNombreAplicacion(aplicacion.getNombreAplicacion());
                aplicacionOptional.get().setUrl(aplicacion.getUrl());
                Aplicacion aplicacionEditada = aplicacionDao.save(aplicacionOptional.get());

                if (aplicacionEditada != null) {
                    listaAplicaciones.add(aplicacionEditada);
                    respuesta.getAplicacionResponse().setAplicacion(listaAplicaciones);
                    respuesta.setMetadata("Respuesta ok", "00", "Aplicacion Editada");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se pudo editar la aplicacion");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }
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
    public ResponseEntity<AplicacionResponseRest> buscarAplicacionPorId(Long idAplicacion) {
        AplicacionResponseRest respuesta = new AplicacionResponseRest();
        List<Aplicacion> listaAplicaciones = new ArrayList<>();
        try {
            Optional<Aplicacion> aplicacionOptional = aplicacionDao.findById(idAplicacion);
            if (aplicacionOptional.isPresent()) {
                listaAplicaciones.add(aplicacionOptional.get());
                respuesta.getAplicacionResponse().setAplicacion(listaAplicaciones);
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

}
