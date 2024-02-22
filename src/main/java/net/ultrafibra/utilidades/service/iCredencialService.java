package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.model.Credencial;
import net.ultrafibra.utilidades.response.CredencialResponseRest;
import net.ultrafibra.utilidades.response.PasswordResponseRest;
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
