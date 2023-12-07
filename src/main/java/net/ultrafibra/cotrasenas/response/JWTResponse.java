package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.Jwt;

@Data
public class JWTResponse  {
    private List<Jwt> jwt;
}
