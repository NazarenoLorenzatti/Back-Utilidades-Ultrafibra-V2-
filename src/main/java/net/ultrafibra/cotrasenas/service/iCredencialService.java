package net.ultrafibra.cotrasenas.service;

import net.ultrafibra.cotrasenas.model.Credencial;
import net.ultrafibra.cotrasenas.response.CredencialResponseRest;
import net.ultrafibra.cotrasenas.response.PasswordResponseRest;
import org.springframework.http.ResponseEntity;

public interface iCredencialService {

    public ResponseEntity<CredencialResponseRest> listarCredenciales();

    public ResponseEntity<CredencialResponseRest> buscarCredencial(Credencial credencial);
    
    public ResponseEntity<CredencialResponseRest> buscarCredencialPorId(Long idCredencial);
    
    public ResponseEntity<CredencialResponseRest> buscarCredencialPorAdministrativo(Long idAdministrativo);
    
    public ResponseEntity<CredencialResponseRest> nuevaCredencial(Credencial credencial);

    public ResponseEntity<CredencialResponseRest> editarCredencial(Credencial credencial);
    
    public ResponseEntity<CredencialResponseRest> actualizarContraseña(Credencial credencial);

    public ResponseEntity<CredencialResponseRest> eliminarCredencial(Long idCredencial);
    
    public ResponseEntity<PasswordResponseRest> generarPassword();
}
