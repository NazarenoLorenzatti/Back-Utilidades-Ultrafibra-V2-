package net.ultrafibra.cotrasenas.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.cotrasenas.dao.iAdministrativoDao;
import net.ultrafibra.cotrasenas.dao.iUsuarioDao;
import net.ultrafibra.cotrasenas.model.Administrativo;
import net.ultrafibra.cotrasenas.model.Usuario;
import net.ultrafibra.cotrasenas.response.AdministrativoResponseRest;
import net.ultrafibra.cotrasenas.service.iAdministrativoService;
import net.ultrafibra.cotrasenas.util.ImgCompressor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AdministrativoServiceImpl implements iAdministrativoService {

    @Autowired
    private iUsuarioDao usuarioDao;

    @Autowired
    private iAdministrativoDao administrativoDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<AdministrativoResponseRest> listarAdministrativos() {
        AdministrativoResponseRest respuesta = new AdministrativoResponseRest();
        try {
            List<Administrativo> administrativos = (List<Administrativo>) administrativoDao.findAll();
            respuesta.getAdministrativoResponse().setAdministrativo(administrativos);
            respuesta.setMetadata("Respuesta ok", "00", "Lista de administrativos cargados");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<AdministrativoResponseRest> buscarAdministrativoPorId(Long idAdministrativo) {
        AdministrativoResponseRest respuesta = new AdministrativoResponseRest();
        List<Administrativo> listaAdministrativos = new ArrayList<>();
        try {
            Optional<Administrativo> administrativoOptional = administrativoDao.findById(idAdministrativo);
            if (administrativoOptional.isPresent()) {
                byte[] imagenDescomprimida = ImgCompressor.decompressZLib(administrativoOptional.get().getUsuario().getImgPerfil());
                administrativoOptional.get().getUsuario().setImgPerfil(imagenDescomprimida);

                listaAdministrativos.add(administrativoOptional.get());
                respuesta.getAdministrativoResponse().setAdministrativo(listaAdministrativos);
                respuesta.setMetadata("Respuesta ok", "00", "Administrativo encontrado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro el Administrativo");
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
    public ResponseEntity<AdministrativoResponseRest> eliminarAdministrativo(Long idAdministrativo) {
        AdministrativoResponseRest respuesta = new AdministrativoResponseRest();
        try {
            administrativoDao.deleteById(idAdministrativo);
            respuesta.setMetadata("Respuesta ok", "00", "Administrativo eliminado");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al eliminar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<AdministrativoResponseRest> guardarAdministrativo(Administrativo administrativo) {
        AdministrativoResponseRest respuesta = new AdministrativoResponseRest();
        List<Administrativo> listaAdministrativos = new ArrayList<>();
        try {
            administrativo.setUsuario(usuarioDao.findByUsername(administrativo.getUsuario().getUsername()));
            Administrativo administrativoGuardado = administrativoDao.save(administrativo);
            if (administrativoGuardado != null) {
                listaAdministrativos.add(administrativoGuardado);
                respuesta.getAdministrativoResponse().setAdministrativo(listaAdministrativos);
                respuesta.setMetadata("Respuesta ok", "00", "Administrativo Guardado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se Pudo guardar el administrativo");
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<AdministrativoResponseRest> editarAdministrativo(Administrativo administrativo) {
        AdministrativoResponseRest respuesta = new AdministrativoResponseRest();
        List<Administrativo> listaAdministrativos = new ArrayList<>();
        try {
            Optional<Administrativo> administrativoOptional = administrativoDao.findById(administrativo.getIdAdministrativo());
            if (administrativoOptional.isPresent()) {

                administrativoOptional.get().setApellido(administrativo.getApellido());
                administrativoOptional.get().setNombre(administrativo.getNombre());
                administrativoOptional.get().setTelefono(administrativo.getTelefono());
                administrativoOptional.get().setEmail(administrativo.getEmail());

                Administrativo administrativoActualizado = administrativoDao.save(administrativoOptional.get());

                if (administrativoActualizado != null) {
                    listaAdministrativos.add(administrativoOptional.get());
                    respuesta.getAdministrativoResponse().setAdministrativo(listaAdministrativos);
                    respuesta.setMetadata("Respuesta ok", "00", "Administrativo Actualizado");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se Pudo Actualizar el administrativo");
                    return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
                }

            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro el Administrativo");
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
    public ResponseEntity<AdministrativoResponseRest> buscarAdministrativo(Administrativo administrativo) {
        AdministrativoResponseRest respuesta = new AdministrativoResponseRest();
        List<Administrativo> listaAdministrativos = new ArrayList<>();
        try {
            Optional<Administrativo> administrativoOptional = administrativoDao.findById(administrativo.getIdAdministrativo());
            if (administrativoOptional.isPresent()) {
                byte[] imagenDescomprimida = ImgCompressor.decompressZLib(administrativoOptional.get().getUsuario().getImgPerfil());
                administrativoOptional.get().getUsuario().setImgPerfil(imagenDescomprimida);

                listaAdministrativos.add(administrativoOptional.get());
                respuesta.getAdministrativoResponse().setAdministrativo(listaAdministrativos);
                respuesta.setMetadata("Respuesta ok", "00", "Administrativo encontrado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro el Administrativo");
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
    public ResponseEntity<AdministrativoResponseRest> buscarAdministrativoPorUsuario(Usuario usuario) {
        AdministrativoResponseRest respuesta = new AdministrativoResponseRest();
        List<Administrativo> listaAdministrativos = new ArrayList<>();
        try {
            Optional<Administrativo> administrativoOptional = administrativoDao.findByUsuario(usuarioDao.findByUsername(usuario.getUsername()));
            if (administrativoOptional.isPresent()) {
                if (administrativoOptional.get().getUsuario().getImgPerfil() != null) {
                    byte[] imagenDescomprimida = ImgCompressor.decompressZLib(administrativoOptional.get().getUsuario().getImgPerfil());
                    administrativoOptional.get().getUsuario().setImgPerfil(imagenDescomprimida);
                }
                listaAdministrativos.add(administrativoOptional.get());
                respuesta.getAdministrativoResponse().setAdministrativo(listaAdministrativos);
                respuesta.setMetadata("Respuesta ok", "00", "Administrativo encontrado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro el Administrativo");
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
