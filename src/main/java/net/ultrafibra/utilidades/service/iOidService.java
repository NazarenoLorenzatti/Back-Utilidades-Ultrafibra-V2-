package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.model.OidClass;
import net.ultrafibra.utilidades.response.OidResponseRest;
import org.springframework.http.ResponseEntity;

public interface iOidService {

    public ResponseEntity<OidResponseRest> listarOids();

    public ResponseEntity<OidResponseRest> cargarOid(OidClass oid);

    public ResponseEntity<OidResponseRest> editarOid(OidClass oid);

    public ResponseEntity<OidResponseRest> eliminarOid(Long idOid);
    
    public ResponseEntity<OidResponseRest> buscarOidPorOid(String oid);

    public ResponseEntity<OidResponseRest> buscarOidPorEvento(String evento);

    public ResponseEntity<OidResponseRest> buscarOidPorEventoContiene(String contiene);
}
