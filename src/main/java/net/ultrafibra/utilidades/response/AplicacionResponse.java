package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.Aplicacion;

@Data
public class AplicacionResponse {
    
    private List<Aplicacion> aplicacion;
    
}
