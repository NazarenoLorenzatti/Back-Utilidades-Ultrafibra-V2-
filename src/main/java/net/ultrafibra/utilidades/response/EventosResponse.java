package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.Eventos;

@Data
public class EventosResponse {
    
    private List<Eventos> eventos;
}
