package net.ultrafibra.cotrasenas.service;

import net.ultrafibra.cotrasenas.model.Tecnico;
import net.ultrafibra.cotrasenas.response.TecnicoResponseRest;
import org.springframework.http.ResponseEntity;

public interface iTecnicoService {

    public ResponseEntity<TecnicoResponseRest> listarTecnicos();

    public ResponseEntity<TecnicoResponseRest> guardarTecnico(Tecnico tecnico);

    public ResponseEntity<TecnicoResponseRest> eliminarTecnico(Long idTecnico);

    public ResponseEntity<TecnicoResponseRest> editarTecnico(Tecnico tecnico);

    public ResponseEntity<TecnicoResponseRest> buscarTecnico(Tecnico tecnico);

}
