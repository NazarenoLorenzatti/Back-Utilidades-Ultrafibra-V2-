package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.Tecnico;

@Data
public class TecnicoResponse {
    
    private List<Tecnico> tecnicos;
}
