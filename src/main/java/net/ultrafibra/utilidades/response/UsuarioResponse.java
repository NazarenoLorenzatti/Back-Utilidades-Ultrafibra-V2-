package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.Usuario;

@Data
public class UsuarioResponse {
    
    private List<Usuario> usuario;
}
