package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.model.Datasets;
import net.ultrafibra.utilidades.model.Host;
import net.ultrafibra.utilidades.response.DatasetsResponseRest;
import org.springframework.http.ResponseEntity;

public interface iDatasetsService {
    
    public ResponseEntity<DatasetsResponseRest> listarData();
    
    public ResponseEntity<DatasetsResponseRest> listarDataHost(Host host);
    
    public ResponseEntity<DatasetsResponseRest> registrarData(Datasets data);
    
    public ResponseEntity<DatasetsResponseRest> eliminarData(Long idData);
    
    
}
