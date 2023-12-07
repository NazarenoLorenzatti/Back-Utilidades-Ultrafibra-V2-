package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.OidClass;

@Data
public class OidResponse {
    
    private List<OidClass> oids;
}
