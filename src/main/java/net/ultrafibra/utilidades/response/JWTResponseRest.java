package net.ultrafibra.utilidades.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTResponseRest extends ResponseRest{
    private JWTResponse jwtResponse = new JWTResponse();
}
