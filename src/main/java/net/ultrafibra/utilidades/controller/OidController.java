package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.model.OidClass;
import net.ultrafibra.utilidades.response.OidResponseRest;
import net.ultrafibra.utilidades.service.impl.OidServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
    "*",
    "http://45.230.65.207",
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1/oids")
public class OidController {

    @Autowired
    private OidServiceImpl oidService;

    /**
     * Listar OIDs
     *
     * @return
     */
    @GetMapping("/listar-oids")
    public ResponseEntity<OidResponseRest> listarOids() {
        return oidService.listarOids();
    }

    /**
     * Guardar nuevo OID
     *
     * @param oid
     * @return
     */
    @PostMapping("/guardar-oid")
    public ResponseEntity<OidResponseRest> cargarOid(@RequestBody OidClass oid) {
        return oidService.cargarOid(oid);
    }

    /**
     * Editar Oid guardado
     *
     * @param oid
     * @return
     */
    @PutMapping("/editar-oid")
    public ResponseEntity<OidResponseRest> editarOid(@RequestBody OidClass oid) {
        return oidService.editarOid(oid);
    }

    /**
     * Eliminar el Oid seleccionado
     *
     * @param idOid
     * @return
     */
    @DeleteMapping("/eliminar-oid/{idOid}")
    public ResponseEntity<OidResponseRest> eliminarOid(@PathVariable Long idOid) {
        return oidService.eliminarOid(idOid);
    }
    
    /**
     * Buscar Oid por su nombre de evento
     * @param evento
     * @return 
     */
    @GetMapping("/buscar-oid-evento/{evento}")
    public ResponseEntity<OidResponseRest> buscarOidPorEvento(@PathVariable String evento) {
        return oidService.buscarOidPorEvento(evento);
    }
    
    /** 
     * Buscar oid por su numero de identificador de Objeto
     * @param oid
     * @return 
     */
    @GetMapping("/buscar-oid/{oid}")
    public ResponseEntity<OidResponseRest> buscarOidPorOid(@PathVariable String oid) {
        return oidService.buscarOidPorOid(oid);
    }
    
    /**
     * Buscar oid por su evento siempre que contenga lo que se envia por parametro
     * @param contiene
     * @return 
     */
    @GetMapping("/buscar-oid/{contiene}")
    public ResponseEntity<OidResponseRest> buscarOidPorEventoContiene(String contiene) {
         return oidService.buscarOidPorEventoContiene(contiene);
    }

}
