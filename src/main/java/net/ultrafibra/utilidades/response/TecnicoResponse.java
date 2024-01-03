package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.Tecnico;

@Data
public class TecnicoResponse {
    
    private List<Tecnico> tecnicos;
}
