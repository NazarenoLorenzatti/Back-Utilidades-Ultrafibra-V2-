package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.response.JWTResponseRest;
import net.ultrafibra.utilidades.response.ResponseRest;
import net.ultrafibra.utilidades.response.HostResponseRest;
import jakarta.annotation.security.PermitAll;
import net.ultrafibra.utilidades.model.OidClass;
import net.ultrafibra.utilidades.model.SNMPDevice;
import net.ultrafibra.utilidades.model.Usuario;
import net.ultrafibra.utilidades.service.impl.HostServiceImpl;
import net.ultrafibra.utilidades.service.impl.LoginServiceImpl;
import net.ultrafibra.utilidades.service.impl.SNMPDeviceServiceImpl;
import net.ultrafibra.utilidades.util.SMSService;
import net.ultrafibra.utilidades.util.SnmpClient;
import org.ietf.jgss.Oid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
    "*",
    "http://45.230.65.207",
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1")
public class LoginController {

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private SNMPDeviceServiceImpl snmpService;

    /**
     * Primer Factor de autenticacion
     *
     * @param loginRequest
     * @return
     * @throws Exception
     */
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseRest> login(@RequestBody Usuario loginRequest) throws Exception {
        return loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    /**
     * Segundo Factor de autenticacion y proporcion de TOKEN
     *
     * @param loginRequest
     * @return
     * @throws Exception
     */
    @PostMapping(path = "/confirm", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTResponseRest> confirm(@RequestBody Usuario loginRequest) throws Exception {
        return loginService.confirm(loginRequest.getUsername(), loginRequest.getPassword(), loginRequest.getPin());
    }

    @PermitAll
    @GetMapping("/prueba")
    public void prueba() throws Exception {
        snmpService.monitorearAlarmas();
    }
}
