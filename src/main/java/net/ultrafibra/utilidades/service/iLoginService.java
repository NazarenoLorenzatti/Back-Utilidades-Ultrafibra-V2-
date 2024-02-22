package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.response.JWTResponseRest;
import net.ultrafibra.utilidades.response.ResponseRest;
import org.springframework.http.ResponseEntity;



public interface iLoginService {
    
    public ResponseEntity<ResponseRest> login(String userName, String password);
    public ResponseEntity<JWTResponseRest> confirm(String userName, String password, int pin);
    
}
