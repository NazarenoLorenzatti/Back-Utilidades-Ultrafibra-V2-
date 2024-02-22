package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.OidClass;

@Data
public class OidResponse {
    
    private List<OidClass> oids;
}
