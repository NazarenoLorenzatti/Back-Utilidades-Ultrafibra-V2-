package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.Datasets;

@Data
public class DatasetsResponse {
    
    private List<Datasets> datasets;
}
