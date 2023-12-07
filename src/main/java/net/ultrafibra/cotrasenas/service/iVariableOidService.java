package net.ultrafibra.cotrasenas.service;

import net.ultrafibra.cotrasenas.model.OidClass;
import net.ultrafibra.cotrasenas.model.VariableOid;
import net.ultrafibra.cotrasenas.response.VariableOidResponseRest;
import org.springframework.http.ResponseEntity;

public interface iVariableOidService {

    public ResponseEntity<VariableOidResponseRest> listarVariables();
    
    public ResponseEntity<VariableOidResponseRest> guardarVariable(OidClass oid, VariableOid variable);

    public ResponseEntity<VariableOidResponseRest> editarVariable(VariableOid variable);

    public ResponseEntity<VariableOidResponseRest> eliminarVariable(Long idVariable);
    
    public ResponseEntity<VariableOidResponseRest> buscarVariableDeOid(OidClass oid);

}
