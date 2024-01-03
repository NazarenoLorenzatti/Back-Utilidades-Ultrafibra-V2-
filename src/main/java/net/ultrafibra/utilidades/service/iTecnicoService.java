package net.ultrafibra.utilidades.service;

import net.ultrafibra.utilidades.model.Tecnico;
import net.ultrafibra.utilidades.response.TecnicoResponseRest;
import org.springframework.http.ResponseEntity;

public interface iTecnicoService {

    public ResponseEntity<TecnicoResponseRest> listarTecnicos();

    public ResponseEntity<TecnicoResponseRest> guardarTecnico(Tecnico tecnico);

    public ResponseEntity<TecnicoResponseRest> eliminarTecnico(Long idTecnico);

    public ResponseEntity<TecnicoResponseRest> editarTecnico(Tecnico tecnico);

    public ResponseEntity<TecnicoResponseRest> buscarTecnico(Tecnico tecnico);

}
