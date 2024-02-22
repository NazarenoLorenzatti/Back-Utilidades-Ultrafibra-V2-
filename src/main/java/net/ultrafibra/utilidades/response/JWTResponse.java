package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.Jwt;

@Data
public class JWTResponse  {
    private List<Jwt> jwt;
}
