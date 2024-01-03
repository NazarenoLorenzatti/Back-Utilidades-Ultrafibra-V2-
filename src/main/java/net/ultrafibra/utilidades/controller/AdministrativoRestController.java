package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.model.Administrativo;
import net.ultrafibra.utilidades.model.Usuario;
import net.ultrafibra.utilidades.response.AdministrativoResponseRest;
import net.ultrafibra.utilidades.service.impl.AdministrativoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = {"45.230.65.207"})
@RequestMapping("/api/v1/administrativo")
public class AdministrativoRestController {

    @Autowired
    private AdministrativoServiceImpl administrativoService;

    /**
     * Guardar datos de un nuevo Administrativo
     *
     * @param administrativo
     * @return
     * @throws Exception
     */
    @PostMapping("/crear-administrativo")
    public ResponseEntity<AdministrativoResponseRest> crearAdministrativo(@RequestBody Administrativo administrativo) throws Exception {
        ResponseEntity<AdministrativoResponseRest> respuesta = administrativoService.guardarAdministrativo(administrativo);
        return respuesta;
    }

    /**
     * Editar informacion del administrativo
     *
     * @param administrativo
     * @return
     * @throws Exception
     */
    @PutMapping("/editar-administrativo")
    public ResponseEntity<AdministrativoResponseRest> editarAdministrativo(@RequestBody Administrativo administrativo) throws Exception {
        ResponseEntity<AdministrativoResponseRest> respuesta = administrativoService.editarAdministrativo(administrativo);
        return respuesta;
    }

    /**
     * Buscar administrativo Por ID metodo Get
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/buscar-administrativo/{id}")
    public ResponseEntity<AdministrativoResponseRest> buscarAdministrativoPorId(@PathVariable Long id) throws Exception {
        ResponseEntity<AdministrativoResponseRest> respuesta = administrativoService.buscarAdministrativoPorId(id);
        return respuesta;
    }
    
    /**
     * Buscar Administrativo en base al usuario
     * 
     * @param usuario
     * @return
     * @throws Exception 
     */
    @PostMapping("/buscar-administrativo-usuario")
    public ResponseEntity<AdministrativoResponseRest> buscarAdministrativoPorusuario(@RequestBody Usuario usuario) throws Exception {
        ResponseEntity<AdministrativoResponseRest> respuesta = administrativoService.buscarAdministrativoPorUsuario(usuario);
        return respuesta;
    }

    /**
     * Buscar administrativo por ID metodo Post
     *
     * @param administrativo
     * @return
     * @throws Exception
     */
    @PostMapping("/buscar-administrativo")
    public ResponseEntity<AdministrativoResponseRest> buscarAdministrativo(@RequestBody Administrativo administrativo) throws Exception {
        ResponseEntity<AdministrativoResponseRest> respuesta = administrativoService.buscarAdministrativo(administrativo);
        return respuesta;
    }

    /**
     * Eliminar administrativo por ID
     *
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("/eliminar-administrativo/{id}")
    public ResponseEntity<AdministrativoResponseRest> eliminarAdministrativo(@PathVariable Long id) throws Exception {
        ResponseEntity<AdministrativoResponseRest> respuesta = administrativoService.eliminarAdministrativo(id);
        return respuesta;
    }

    /**
     * Listar todos los administrativos guardados
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/listar-administrativos")
    public ResponseEntity<AdministrativoResponseRest> listarAdministrativos() throws Exception {
        ResponseEntity<AdministrativoResponseRest> respuesta = administrativoService.listarAdministrativos();
        return respuesta;
    }

}
