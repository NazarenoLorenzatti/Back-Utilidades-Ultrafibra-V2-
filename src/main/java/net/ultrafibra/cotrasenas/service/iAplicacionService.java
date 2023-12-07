package net.ultrafibra.cotrasenas.service;

import net.ultrafibra.cotrasenas.model.Aplicacion;
import net.ultrafibra.cotrasenas.response.AplicacionResponseRest;
import org.springframework.http.ResponseEntity;

public interface iAplicacionService {

    public ResponseEntity<AplicacionResponseRest> listarAplicaciones();

    public ResponseEntity<AplicacionResponseRest> buscarAplicacion(Aplicacion aplicacion);
    
    public ResponseEntity<AplicacionResponseRest> buscarAplicacionPorId(Long idAplicacion);

    public ResponseEntity<AplicacionResponseRest> guardarAplicacion(Aplicacion aplicacion, String username);

    public ResponseEntity<AplicacionResponseRest> editarAplicacion(Aplicacion aplicacion);

    public ResponseEntity<AplicacionResponseRest> eliminarAplicacion(Long idAplicacion);
}
