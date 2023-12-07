package net.ultrafibra.cotrasenas.controller;

import jakarta.annotation.security.PermitAll;
import net.ultrafibra.cotrasenas.model.OidClass;
import net.ultrafibra.cotrasenas.model.SNMPDevice;
import net.ultrafibra.cotrasenas.model.Usuario;
import net.ultrafibra.cotrasenas.response.*;
import net.ultrafibra.cotrasenas.service.impl.HostServiceImpl;
import net.ultrafibra.cotrasenas.service.impl.LoginServiceImpl;
import net.ultrafibra.cotrasenas.service.impl.SNMPDeviceServiceImpl;
import net.ultrafibra.cotrasenas.util.SnmpClient;
import org.ietf.jgss.Oid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
    "http://45.230.65.207", 
    "http://localhost",
    "http://192.168.1.77"})
public class LoginController {

    @Autowired
    private LoginServiceImpl loginService;
    
    @Autowired
    private HostServiceImpl hostClient;
    
    @Autowired
    private SnmpClient snmpClient2;

    /**
     * Primer Factor de autenticacion
     * 
     * @param loginRequest
     * @return
     * @throws Exception 
     */
    @PostMapping(path = "/primer-factor", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseRest> primerFactor(@RequestBody Usuario loginRequest) throws Exception {
        return loginService.primerFactor(loginRequest.getUsername(), loginRequest.getPassword());
    }

    /**
     * Segundo Factor de autenticacion y proporcion de TOKEN
     *
     * @param loginRequest
     * @return
     * @throws Exception
     */    
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTResponseRest> login(@RequestBody Usuario loginRequest) throws Exception {
        return loginService.login(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getPin());
    }
    
    
    @PermitAll
    @GetMapping("/prueba")
    public ResponseEntity<HostResponseRest> prueba() throws Exception {
        return hostClient.hacerPing("10.20.30.201");
    }
}
