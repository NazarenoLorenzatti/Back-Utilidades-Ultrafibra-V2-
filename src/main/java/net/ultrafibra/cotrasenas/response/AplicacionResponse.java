package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.Aplicacion;

@Data
public class AplicacionResponse {
    
    private List<Aplicacion> aplicacion;
    
}
