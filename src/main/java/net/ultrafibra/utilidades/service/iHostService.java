package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.model.Host;
import net.ultrafibra.utilidades.response.HostResponseRest;
import org.springframework.http.ResponseEntity;

public interface iHostService {

    public ResponseEntity<HostResponseRest> listarHosts();

    public ResponseEntity<HostResponseRest> buscarHostPorIp(String ipHost);
    
    public ResponseEntity<HostResponseRest> guardarHost(Host host);

    public ResponseEntity<HostResponseRest> eliminarHost(Long idHost);

    public ResponseEntity<HostResponseRest> editarHost(Host host);
    
    public ResponseEntity<HostResponseRest> hacerPing(String ipHost);
    
    
}
