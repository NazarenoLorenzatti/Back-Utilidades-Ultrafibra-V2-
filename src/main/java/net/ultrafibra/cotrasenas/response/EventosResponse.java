package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.Eventos;

@Data
public class EventosResponse {
    
    private List<Eventos> eventos;
}
