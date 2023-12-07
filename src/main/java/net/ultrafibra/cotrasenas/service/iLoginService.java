package net.ultrafibra.cotrasenas.service;

import net.ultrafibra.cotrasenas.response.JWTResponseRest;
import net.ultrafibra.cotrasenas.response.ResponseRest;
import org.springframework.http.ResponseEntity;



public interface iLoginService {
    
    public ResponseEntity<ResponseRest> primerFactor(String userName, String password);
    public ResponseEntity<JWTResponseRest> login(String userName, String password, int pin);
    
}
