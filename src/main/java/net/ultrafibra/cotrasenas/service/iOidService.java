package net.ultrafibra.cotrasenas.service;

import net.ultrafibra.cotrasenas.model.OidClass;
import net.ultrafibra.cotrasenas.response.OidResponseRest;
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
