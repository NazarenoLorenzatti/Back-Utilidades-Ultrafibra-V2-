package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.Datasets;

@Data
public class DatasetsResponse {
    
    private List<Datasets> datasets;
}
