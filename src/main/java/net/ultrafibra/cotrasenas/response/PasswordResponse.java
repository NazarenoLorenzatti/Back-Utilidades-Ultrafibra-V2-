package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.Password;

@Data
public class PasswordResponse {
    
    private List<Password> password;
}
