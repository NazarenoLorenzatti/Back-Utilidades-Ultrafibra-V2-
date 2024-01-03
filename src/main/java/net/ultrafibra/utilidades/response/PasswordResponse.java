package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.Password;

@Data
public class PasswordResponse {
    
    private List<Password> password;
}
