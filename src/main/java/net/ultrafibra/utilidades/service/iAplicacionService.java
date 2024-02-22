package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.model.Aplicacion;
import net.ultrafibra.utilidades.response.AplicacionResponseRest;
import org.springframework.http.ResponseEntity;

public interface iAplicacionService {

    public ResponseEntity<AplicacionResponseRest> listarAplicaciones();

    public ResponseEntity<AplicacionResponseRest> buscarAplicacion(Aplicacion aplicacion);
    
    public ResponseEntity<AplicacionResponseRest> buscarAplicacionPorId(Long idAplicacion);

    public ResponseEntity<AplicacionResponseRest> guardarAplicacion(Aplicacion aplicacion, String username);

    public ResponseEntity<AplicacionResponseRest> editarAplicacion(Aplicacion aplicacion);

    public ResponseEntity<AplicacionResponseRest> eliminarAplicacion(Long idAplicacion);
}
