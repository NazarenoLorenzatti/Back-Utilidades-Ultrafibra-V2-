package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.VariableOid;

@Data
public class VariableOidResponse {
    
    private List<VariableOid> Variables;
}
