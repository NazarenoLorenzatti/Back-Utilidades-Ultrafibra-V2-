package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.Administrativo;

@Data
public class AdministrativoResponse {
    
    private List<Administrativo> administrativo;
}
