package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.model.Credencial;
import net.ultrafibra.utilidades.response.CredencialResponseRest;
import net.ultrafibra.utilidades.response.PasswordResponseRest;
import net.ultrafibra.utilidades.service.impl.CredencialServiceImpl;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = {"http://localhost"})
@RequestMapping("/api/v1/credencial")
public class CredencialRestController {

    @Autowired
    private CredencialServiceImpl credencialService;

    /**
     * Guardar nuevas credenciales
     *
     * @param credencial
     * @return
     * @throws Exception
     */
    @PostMapping("/guardar-credencial")
    public ResponseEntity<CredencialResponseRest> guardarCredencial(@RequestBody Credencial credencial) throws Exception {
        ResponseEntity<CredencialResponseRest> respuesta = credencialService.nuevaCredencial(credencial);
        return respuesta;
    }

    /**
     * Editar Credenciales existentes
     *
     * @param credencial
     * @return
     * @throws Exception
     */
    @PutMapping("/editar-credencial")
    public ResponseEntity<CredencialResponseRest> editarCredencial(@RequestBody Credencial credencial) throws Exception {
        ResponseEntity<CredencialResponseRest> respuesta = credencialService.editarCredencial(credencial);
        return respuesta;
    }

    /**
     * Actualizar contraseña de aplicacion
     *
     * @param credencial
     * @return
     * @throws Exception
     */
    @PutMapping("/actualizar-contraseña")
    public ResponseEntity<CredencialResponseRest> actualizarContraseña(@RequestBody Credencial credencial) throws Exception {
        ResponseEntity<CredencialResponseRest> respuesta = credencialService.actualizarContraseña(credencial);
        return respuesta;
    }

    /**
     * Buscar credencial por Id metodo Post
     *
     * @param credencial
     * @return
     * @throws Exception
     */
    @PostMapping("/buscar-credencial")
    public ResponseEntity<CredencialResponseRest> buscarCredencial(@RequestBody Credencial credencial) throws Exception {
        ResponseEntity<CredencialResponseRest> respuesta = credencialService.buscarCredencial(credencial);
        return respuesta;
    }

    /**
     * Buscar credencial por Id metodo Get
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/buscar-credencial/{id}")
    public ResponseEntity<CredencialResponseRest> buscarCredencialPorId(@PathVariable Long id) throws Exception {
        ResponseEntity<CredencialResponseRest> respuesta = credencialService.buscarCredencialPorId(id);
        return respuesta;
    }

    /**
     * Eliminar la credencial
     *
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("/eliminar-credencial/{id}")
    public ResponseEntity<CredencialResponseRest> eliminarCredencialPorId(@PathVariable Long id) throws Exception {
        ResponseEntity<CredencialResponseRest> respuesta = credencialService.eliminarCredencial(id);
        return respuesta;
    }

    /**
     * Actualizar vigencia de todas las credenciales
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/actualizar-estado")
    public ResponseEntity<CredencialResponseRest> actualizarEstado() throws Exception {
        ResponseEntity<CredencialResponseRest> respuesta = credencialService.cambiarEstado();
        return respuesta;
    }

    /**
     * Listar todas las credenciales
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/listar-credenciales")
    public ResponseEntity<CredencialResponseRest> listarCredenciales() throws Exception {
        ResponseEntity<CredencialResponseRest> respuesta = credencialService.listarCredenciales();
        return respuesta;
    }

    /**
     * Buscar credencial por administrativo
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/buscar-credencial-administrativo/{id}")
    public ResponseEntity<CredencialResponseRest> buscarCredencialPorAdministrativo(@PathVariable Long id) throws Exception {
        ResponseEntity<CredencialResponseRest> respuesta = credencialService.buscarCredencialPorAdministrativo(id);
        return respuesta;
    }
    
    /**
     * Generar una contraseña aleatoria como sugerencia
     * 
     * @return
     * @throws Exception 
     */
    @GetMapping("/generar-contraseña")
    public ResponseEntity<PasswordResponseRest> generarContraseña() throws Exception {
        ResponseEntity<PasswordResponseRest> respuesta = credencialService.generarPassword();
        return respuesta;
    }
    
 }
