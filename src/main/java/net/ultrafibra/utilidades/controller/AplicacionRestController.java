package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.model.Aplicacion;
import net.ultrafibra.utilidades.response.AplicacionResponseRest;
import net.ultrafibra.utilidades.service.impl.AplicacionServiceImpl;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = {"*"})
@RequestMapping("/api/v1/aplicacion")
public class AplicacionRestController {
    
    @Autowired
    private AplicacionServiceImpl aplicacionService;

    /**
     * Guardar nueva aplicacion
     *
     * @param aplicacion
     * @param username
     * @return
     * @throws Exception
     */
    @PostMapping("/guardar-aplicacion/{username}")
    public ResponseEntity<AplicacionResponseRest> guardarAplicacion(@RequestBody Aplicacion aplicacion, @PathVariable String username) throws Exception {
        ResponseEntity<AplicacionResponseRest> respuesta = aplicacionService.guardarAplicacion(aplicacion, username);
        return respuesta;
    }

    /**
     * Editar aplicacion guardada en base de datos
     *
     * @param aplicacion
     * @return
     * @throws Exception
     */
    @PutMapping("/editar-aplicacion")
    public ResponseEntity<AplicacionResponseRest> editarAplicacion(@RequestBody Aplicacion aplicacion) throws Exception {
        ResponseEntity<AplicacionResponseRest> respuesta = aplicacionService.editarAplicacion(aplicacion);
        return respuesta;
    }
    
    /**
     * Buscar aplicacion por Id metodo Post
     * 
     * @param aplicacion
     * @return
     * @throws Exception 
     */
    @PostMapping("/buscar-aplicacion")
    public ResponseEntity<AplicacionResponseRest> buscarAplicacion(@RequestBody Aplicacion aplicacion) throws Exception {
        ResponseEntity<AplicacionResponseRest> respuesta = aplicacionService.buscarAplicacion(aplicacion);
        return respuesta;
    }
    
    /**
     * Buscar aplicacion por Id metodo Get
     * 
     * @param id
     * @return
     * @throws Exception 
     */
    @GetMapping("/buscar-aplicacion/{id}")
    public ResponseEntity<AplicacionResponseRest> buscarAplicacion(@PathVariable Long id) throws Exception {
        ResponseEntity<AplicacionResponseRest> respuesta = aplicacionService.buscarAplicacionPorId(id);
        return respuesta;
    }
    
    /**
     * Listar todas las aplicaciones guardadas en la base
     * 
     * @return
     * @throws Exception 
     */
    @GetMapping("/listar-aplicaciones")
    public ResponseEntity<AplicacionResponseRest> listarAplicaciones() throws Exception {
        ResponseEntity<AplicacionResponseRest> respuesta = aplicacionService.listarAplicaciones();
        return respuesta;
    }
    
    /**
     * Eliminar aplicacion por id
     * 
     * @param id
     * @return
     * @throws Exception 
     */
    @DeleteMapping("/eliminar-aplicacion/{id}")
    public ResponseEntity<AplicacionResponseRest> eliminarAplicacion(@PathVariable Long id) throws Exception {
        ResponseEntity<AplicacionResponseRest> respuesta = aplicacionService.eliminarAplicacion(id);
        return respuesta;
    }
    
}
