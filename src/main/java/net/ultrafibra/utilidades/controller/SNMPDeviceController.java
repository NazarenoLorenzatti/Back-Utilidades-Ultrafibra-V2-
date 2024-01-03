package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.model.OidClass;
import net.ultrafibra.utilidades.model.SNMPDevice;
import net.ultrafibra.utilidades.response.SNMPDeviceResponseRest;
import net.ultrafibra.utilidades.service.impl.SNMPDeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
    "*",
    "http://45.230.65.207",
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1/snmp")
public class SNMPDeviceController {
    
    @Autowired
    private SNMPDeviceServiceImpl deviceService;
    
    /**
     * Listar todos los dispositivos SNMP registrados
     * @return 
     */
    @GetMapping("/listar-dispositivos")
    public ResponseEntity<SNMPDeviceResponseRest> listarDispositivos() {
        return deviceService.listarDispositivos();
    }
    
    /**
     * Buscar dispositivo SNMP por su id
     * @param idDispositivo
     * @return 
     */
    @GetMapping("/buscar-dispositivo/{idDispositivo}")
    public ResponseEntity<SNMPDeviceResponseRest> buscarDispositivo(@PathVariable Long idDispositivo) {
        return deviceService.buscarDispositivo(idDispositivo);
    }
    
    /**
     * Guardar un nuevo dispositivo SNMP
     * @param dispositivo
     * @return 
     */
    @PostMapping("/guardar-dispositivo")
    public ResponseEntity<SNMPDeviceResponseRest> guardarDispositivo(@RequestBody SNMPDevice dispositivo) {
        return deviceService.guardarDispositivo(dispositivo);
    }
    
    /**
     * Editar el dispositivo guardado
     * @param dispositivo
     * @return 
     */
    @PutMapping("/editar-dispositivo")
    public ResponseEntity<SNMPDeviceResponseRest> editarDispositivo(@RequestBody SNMPDevice dispositivo) {
        return deviceService.editarDispositivo(dispositivo);
    }
    
    /**
     * Eliminar dispositivo por su ID
     * @param idDispositivo
     * @return 
     */
    @DeleteMapping("/eliminar-dispositivo/{idDispositivo}")
    public ResponseEntity<SNMPDeviceResponseRest> eliminarDispositivo(@PathVariable Long idDispositivo) {
        return deviceService.eliminarDispositivo(idDispositivo);
    }
    
    /**
     * Realizar la consulta SNMP por el OID al dispositivo
     * @param dispositivo
     * @param oidSolicitud
     * @return 
     */
    @PostMapping("/consulta-snmp")
    public ResponseEntity<SNMPDeviceResponseRest> consultaSNMP(@RequestBody SNMPDevice dispositivo) {
        return deviceService.consultaSNMP(dispositivo, dispositivo.getOids().get(0).getOid());
    }
}
