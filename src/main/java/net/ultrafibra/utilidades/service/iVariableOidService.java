package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.model.OidClass;
import net.ultrafibra.utilidades.model.VariableOid;
import net.ultrafibra.utilidades.response.VariableOidResponseRest;
import org.springframework.http.ResponseEntity;

public interface iVariableOidService {

    public ResponseEntity<VariableOidResponseRest> listarVariables();
    
    public ResponseEntity<VariableOidResponseRest> guardarVariable(VariableOid variable);

    public ResponseEntity<VariableOidResponseRest> editarVariable(VariableOid variable);

    public ResponseEntity<VariableOidResponseRest> eliminarVariable(Long idVariable);
    
    public ResponseEntity<VariableOidResponseRest> buscarVariableDeOid(OidClass oid);

}
