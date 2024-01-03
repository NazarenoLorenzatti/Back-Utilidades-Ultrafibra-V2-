package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.Credencial;

@Data
public class CredencialResponse {
    
    private List<Credencial> credencial;
}
