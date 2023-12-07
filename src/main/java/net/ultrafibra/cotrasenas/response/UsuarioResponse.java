package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.Usuario;

@Data
public class UsuarioResponse {
    
    private List<Usuario> usuario;
}
