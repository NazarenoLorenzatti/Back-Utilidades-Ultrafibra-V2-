package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.Administrativo;

@Data
public class AdministrativoResponse {
    
    private List<Administrativo> administrativo;
}
