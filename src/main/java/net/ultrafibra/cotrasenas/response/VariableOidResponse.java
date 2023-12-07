package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.VariableOid;

@Data
public class VariableOidResponse {
    
    private List<VariableOid> Variables;
}
