package net.ultrafibra.utilidades.controller;

import java.io.IOException;
import net.ultrafibra.utilidades.model.Usuario;
import net.ultrafibra.utilidades.response.UsuarioResponseRest;
import net.ultrafibra.utilidades.service.impl.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = {
    "*",
    "http://45.230.65.207",
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1/usuario")
public class UsuarioRestController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    /**
     * Guardar Usuario
     *
     * @param usuario
     * @return
     * @throws Exception
     */
    @PostMapping("/crear-usuario")
    public ResponseEntity<UsuarioResponseRest> crearUsuario(@RequestBody Usuario usuario) throws Exception {
        ResponseEntity<UsuarioResponseRest> respuesta = usuarioService.guardarUsuario(usuario);
        return respuesta;
    }

    /**
     * Actualizar Usuario
     *
     * @param usuario
     * @return
     * @throws Exception
     */
    @PutMapping("/actualizar-usuario")
    public ResponseEntity<UsuarioResponseRest> actualizarUsuario(@RequestBody Usuario usuario) throws Exception {
        ResponseEntity<UsuarioResponseRest> respuesta = usuarioService.actualizarUsuario(usuario);
        return respuesta;
    }

    /**
     * Buscar usuario por Username
     *
     * @param username
     * @return
     
    @GetMapping("/buscar-usuario/{username}")
    public ResponseEntity<UsuarioResponseRest> buscarUsuario(@PathVariable String username) throws Exception {
        ResponseEntity<UsuarioResponseRest> respuesta = usuarioService.buscarUsuario(username);
        return respuesta;
    }* @throws Exception
     */

    /**
     * Buscar Usuario por ID
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/buscar-usuario/{id}")
    public ResponseEntity<UsuarioResponseRest> buscarUsuario(@PathVariable Long id) throws Exception {
        ResponseEntity<UsuarioResponseRest> respuesta = usuarioService.buscarUsuarioPorId(id);
        return respuesta;
    }

    /**
     * Eliminar Usuario por ID
     *
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("/eliminar-usuario/{id}")
    public ResponseEntity<UsuarioResponseRest> eliminarUsuario(@PathVariable Long id) throws Exception {
        ResponseEntity<UsuarioResponseRest> respuesta = usuarioService.eliminarUsuarioPorId(id);
        return respuesta;
    }

    /**
     * Eliminar usuario
     *
     * @param usuario
     * @return
     * @throws Exception
     */
    @PostMapping("/eliminar-usuario")
    public ResponseEntity<UsuarioResponseRest> eliminarUsuario(@RequestBody Usuario usuario) throws Exception {
        ResponseEntity<UsuarioResponseRest> respuesta = usuarioService.eliminarUsuario(usuario);
        return respuesta;
    }

    /**
     * Listar todos los Usuarios
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/listar-usuarios")
    public ResponseEntity<UsuarioResponseRest> listarUsuarios() throws Exception {
        ResponseEntity<UsuarioResponseRest> respuesta = usuarioService.listarUsuarios();
        return respuesta;
    }

    /**
     *Subir foto de perfil
     * 
     * @param username
     * @param imagen
     * @return
     * @throws IOException
     */
    @PostMapping("/subir-foto-perfil")
    public ResponseEntity<UsuarioResponseRest> subirFoto(@RequestParam("username") String username,
            @RequestParam("img") MultipartFile imagen) throws IOException {
        ResponseEntity<UsuarioResponseRest> respuesta = usuarioService.subirFoto(username, imagen.getBytes());
        return respuesta;
    }

}
