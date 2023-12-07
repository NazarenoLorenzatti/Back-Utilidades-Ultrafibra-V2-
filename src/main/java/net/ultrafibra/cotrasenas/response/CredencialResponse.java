package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.Credencial;

@Data
public class CredencialResponse {
    
    private List<Credencial> credencial;
}
