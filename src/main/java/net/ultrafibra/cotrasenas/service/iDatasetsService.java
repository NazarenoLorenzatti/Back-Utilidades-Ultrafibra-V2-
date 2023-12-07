package net.ultrafibra.cotrasenas.service;

import net.ultrafibra.cotrasenas.model.Datasets;
import net.ultrafibra.cotrasenas.model.Host;
import net.ultrafibra.cotrasenas.response.DatasetsResponseRest;
import org.springframework.http.ResponseEntity;

public interface iDatasetsService {
    
    public ResponseEntity<DatasetsResponseRest> listarData();
    
    public ResponseEntity<DatasetsResponseRest> listarDataHost(Host host);
    
    public ResponseEntity<DatasetsResponseRest> registrarData(Datasets data);
    
    public ResponseEntity<DatasetsResponseRest> eliminarData(Long idData);
    
    
}
