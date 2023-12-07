package net.ultrafibra.cotrasenas.service.impl;

import java.util.*;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.cotrasenas.dao.iRolesDao;
import net.ultrafibra.cotrasenas.dao.iUsuarioDao;
import net.ultrafibra.cotrasenas.model.Rol;
import net.ultrafibra.cotrasenas.model.Usuario;
import net.ultrafibra.cotrasenas.response.UsuarioResponseRest;
import net.ultrafibra.cotrasenas.service.iUsuarioService;
import net.ultrafibra.cotrasenas.util.ImgCompressor;
import net.ultrafibra.cotrasenas.util.PasswordGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UsuarioServiceImpl implements iUsuarioService {

    @Autowired
    private iUsuarioDao usuarioDao;

    @Autowired
    private iRolesDao rolesDao;
    
     @Autowired
    private PasswordGeneratorService passwordGenerator;

    public static String encriptarPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UsuarioResponseRest> listarUsuarios() {
        UsuarioResponseRest respuesta = new UsuarioResponseRest();
        try {
            List<Usuario> usuarios = (List<Usuario>) usuarioDao.findAll(); // Busca la lista de Usuarios
            respuesta.getUsuarioResponse().setUsuario(usuarios); // Setea la lista de usuarios encontrada en la respuesta 
            respuesta.setMetadata("Respuesta ok", "00", "Respuesta exitosa"); // Setea la metadata de la respuesta en caso de ser correcta
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar"); //Setea la metadata en caso de que la respueta sea incorrecta
            e.getStackTrace();
            return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR); // Da como retorno la restepuesta con la metadata incorrecta y el error
        }
        return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.OK); // Retorna en caso de ser correcto la lista de usuarios y la respuesta de estado OK
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UsuarioResponseRest> buscarUsuarioPorId(Long idUsuario) {
        UsuarioResponseRest respuesta = new UsuarioResponseRest();
        List<Usuario> listaUsuarios = new ArrayList<>();
        try {
            Optional<Usuario> usuarioOptional = usuarioDao.findById(idUsuario);
            if (usuarioOptional.isPresent()) {
                listaUsuarios.add(usuarioOptional.get());
                respuesta.getUsuarioResponse().setUsuario(listaUsuarios);
                respuesta.setMetadata("Respuesta ok", "02", "Usuario encontrado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro el usuario");
                return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar el usuario por ID");
            e.getStackTrace();
            return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<UsuarioResponseRest> guardarUsuario(Usuario usuario) {
        UsuarioResponseRest respuesta = new UsuarioResponseRest();
        List<Usuario> listaUsuarios = new ArrayList<>();
        try {
            if (usuarioDao.existsByUsername(usuario.getUsername())) {
                listaUsuarios.add(usuarioDao.findByUsername(usuario.getUsername()));
                respuesta.getUsuarioResponse().setUsuario(listaUsuarios);
                respuesta.setMetadata("Respuesta ok", "00", "EL USUARIO YA EXISTE");
            } else {
                usuario.setPassword(encriptarPassword(usuario.getPassword())); // Encripto el password antes de guardarlo.
                List<Rol> roles = new ArrayList();
                for(Rol r : usuario.getRoles()){
                    if(r.getIdRol() != null){
                         roles.add(rolesDao.findById(r.getIdRol()).get());
                    } else if(r.getNombreRol() != null){
                         roles.add(rolesDao.findByNombreRol(r.getNombreRol()));
                    } else if (r.getIdRol() == null && r.getNombreRol() == null ){
                         roles.add(rolesDao.findByNombreRol("ROLE_USER"));
                    }
                }               
                usuario.setRoles(roles);
                Usuario usuarioGuardado = usuarioDao.save(new Usuario(usuario.getUsername(), usuario.getPassword(), passwordGenerator.generarPin(), roles));
                if (usuarioGuardado != null) {
                    listaUsuarios.add(usuarioGuardado);
                    respuesta.getUsuarioResponse().setUsuario(listaUsuarios);
                    respuesta.setMetadata("Respuesta ok", "00", "Usuario Guardado");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se Pudo guardar el usuario");
                    return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al itentar guardar el usuario");
            e.getStackTrace();
            return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<UsuarioResponseRest> actualizarUsuario(Usuario usuario) {
        UsuarioResponseRest respuesta = new UsuarioResponseRest();
        List<Usuario> listaUsuarios = new ArrayList<>();
        try {
            Optional<Usuario> usuarioOptional = usuarioDao.findById(usuario.getIdUsuario());
            if (usuarioOptional.isPresent()) {
                usuarioOptional.get().setPassword(encriptarPassword(usuario.getPassword()));
                usuarioOptional.get().setUsername(usuario.getUsername());
                Usuario usuarioActualizado = usuarioDao.save(usuarioOptional.get());
                if (usuarioActualizado != null) {
                    listaUsuarios.add(usuarioActualizado);
                    respuesta.getUsuarioResponse().setUsuario(listaUsuarios);
                    respuesta.setMetadata("Respuesta ok", "00", "Usuario Actualizado");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se Pudo Actualizar el usuario");
                    return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.BAD_REQUEST);
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro el usuario");
                return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar el usuario por ID");
            e.getStackTrace();
            return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<UsuarioResponseRest> eliminarUsuarioPorId(Long idUsuario) {
        UsuarioResponseRest respuesta = new UsuarioResponseRest();
        try {
            usuarioDao.deleteById(idUsuario);
            respuesta.setMetadata("respuesta ok", "00", "Registro eliminado");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al eliminar");
            e.getStackTrace();
            return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UsuarioResponseRest> buscarUsuario(String username) {
        UsuarioResponseRest respuesta = new UsuarioResponseRest();
        List<Usuario> listaUsuarios = new ArrayList<>();
        try {
            Usuario usuarioEncontrado = usuarioDao.findByUsername(username);
            if (usuarioEncontrado != null) {
                listaUsuarios.add(usuarioEncontrado);
                respuesta.getUsuarioResponse().setUsuario(listaUsuarios);
                respuesta.setMetadata("Respuesta ok", "00", "Usuario encontrado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro el usuario");
                return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar el usuario por Username");
            e.getStackTrace();
            return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<UsuarioResponseRest> eliminarUsuario(Usuario usuario) {
        UsuarioResponseRest respuesta = new UsuarioResponseRest();
        try {
            usuarioDao.delete(usuarioDao.findByUsername(usuario.getUsername()));
            respuesta.setMetadata("respuesta ok", "00", "Registro eliminado");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al eliminar");
            e.getStackTrace();
            return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<UsuarioResponseRest>(respuesta, HttpStatus.OK);
    }

    @Override 
    @Transactional
    public ResponseEntity<UsuarioResponseRest> subirFoto(String username, byte[] img) {
        UsuarioResponseRest respuesta = new UsuarioResponseRest();
        List<Usuario> listaUsuarios = new ArrayList<>();
        try {
            Usuario usuarioEncontrado = usuarioDao.findByUsername(username);
            if (usuarioEncontrado != null) {
                usuarioEncontrado.setImgPerfil(ImgCompressor.compressZLib(img));
                listaUsuarios.add(usuarioDao.save(usuarioEncontrado));                
                respuesta.getUsuarioResponse().setUsuario(listaUsuarios);
                respuesta.setMetadata("Respuesta ok", "00", "Imagen subida correctamente");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro el usuario");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al subir la imagen");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

}
