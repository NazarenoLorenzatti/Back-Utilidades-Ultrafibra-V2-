package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.model.SNMPDevice;
import net.ultrafibra.utilidades.response.SNMPDeviceResponseRest;
import org.springframework.http.ResponseEntity;

public interface iSNMPDeviceService {
    
    public ResponseEntity<SNMPDeviceResponseRest> listarDispositivos();
    
    public ResponseEntity<SNMPDeviceResponseRest> buscarDispositivo(Long idDispositivo);
    
    public ResponseEntity<SNMPDeviceResponseRest> guardarDispositivo(SNMPDevice dispositivo);
    
    public ResponseEntity<SNMPDeviceResponseRest> eliminarDispositivo(Long idDispositivo);
    
    public ResponseEntity<SNMPDeviceResponseRest> editarDispositivo(SNMPDevice dispositivo);
    
    public ResponseEntity<SNMPDeviceResponseRest> consultaSNMP(SNMPDevice dispositivo, String oidSolicitud);
    
}
