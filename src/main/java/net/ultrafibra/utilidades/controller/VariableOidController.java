package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.model.OidClass;
import net.ultrafibra.utilidades.model.VariableOid;
import net.ultrafibra.utilidades.response.VariableOidResponseRest;
import net.ultrafibra.utilidades.service.impl.VariableOidServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
    "*",
    "http://45.230.65.207",
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1/variables-oid")
public class VariableOidController {
    
    @Autowired 
    private VariableOidServiceImpl variableService;
    
    /**
     * Listar todas las variables de cada OID
     * @return 
     */
    @GetMapping("get-variables")
    public ResponseEntity<VariableOidResponseRest> listarVariables() {
        return variableService.listarVariables();
    }
    
    /**
     * Gardar una nueva variable para el OID
     * @param variable
     * @return 
     */
    @PostMapping("/guardar-variable")
    public ResponseEntity<VariableOidResponseRest> guardarVariable(@RequestBody VariableOid variable) {
        return variableService.guardarVariable(variable);
    }
    
    /**
     * Edicion de una variable guardada
     * @param variable
     * @return 
     */
    @PutMapping("/editar-variable")
    public ResponseEntity<VariableOidResponseRest> editarVariable(@RequestBody VariableOid variable) {
        return variableService.editarVariable(variable);
    }
    
    /**
     * Eliminar una variable en base a su Id
     * @param idVariable
     * @return 
     */
    @DeleteMapping("/eliminar-variable/{idVariable}")
    public ResponseEntity<VariableOidResponseRest> eliminarVariable(@PathVariable Long idVariable) {
        return variableService.eliminarVariable(idVariable);
    }
    
    /**
     * Buscar variables de Oid
     * @param oid
     * @return 
     */
    public ResponseEntity<VariableOidResponseRest> buscarVariableDeOid(OidClass oid) {
        return variableService.buscarVariableDeOid(oid);
    }
}
