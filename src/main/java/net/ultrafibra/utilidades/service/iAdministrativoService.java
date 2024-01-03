package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.model.Administrativo;
import net.ultrafibra.utilidades.model.Usuario;
import net.ultrafibra.utilidades.response.AdministrativoResponseRest;
import org.springframework.http.ResponseEntity;

public interface iAdministrativoService {
    
    public ResponseEntity<AdministrativoResponseRest> listarAdministrativos();
    public ResponseEntity<AdministrativoResponseRest> buscarAdministrativoPorId(Long idAdministrativo);
    public ResponseEntity<AdministrativoResponseRest> buscarAdministrativo(Administrativo administrativo);
    public ResponseEntity<AdministrativoResponseRest> guardarAdministrativo(Administrativo administrativo);
    public ResponseEntity<AdministrativoResponseRest> editarAdministrativo(Administrativo administrativo);
    public ResponseEntity<AdministrativoResponseRest> eliminarAdministrativo(Long idAdministrativo);
    public ResponseEntity<AdministrativoResponseRest> buscarAdministrativoPorUsuario(Usuario username);

    
}
