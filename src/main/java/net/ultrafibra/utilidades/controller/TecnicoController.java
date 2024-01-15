package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.model.Tecnico;
import net.ultrafibra.utilidades.response.TecnicoResponseRest;
import net.ultrafibra.utilidades.service.impl.TecnicoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
    "*",
    "http://45.230.65.207",
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1/tecnicos")
public class TecnicoController {

    @Autowired
    private TecnicoServiceImpl tecnicoService;
    
    /**
     * Listar todos los tecnicos registrados para las alertas
     * @return 
     */
    @GetMapping("/listar-tecnicos")
    public ResponseEntity<TecnicoResponseRest> listarTecnicos() {
        return tecnicoService.listarTecnicos();
    }
    
    /**
     * Registrar un nuevo tecnico que reciba las alertas
     * @param tecnico
     * @return 
     */
    @PostMapping("/guardar-tecnico")
    public ResponseEntity<TecnicoResponseRest> guardarTecnico(@RequestBody Tecnico tecnico) {
        return tecnicoService.guardarTecnico(tecnico);
    }
    
    /**
     * Editar Tecnico Guardado
     * @param tecnico
     * @return 
     */
    @PutMapping("/editar-tecnico")
    public ResponseEntity<TecnicoResponseRest> editarTecnico(@RequestBody Tecnico tecnico) {
        return tecnicoService.editarTecnico(tecnico);
    }
    
    /**
     * Eliminar el tecnico registrado por su Id
     * @param idTecnico
     * @return 
     */
    @DeleteMapping("/eliminar-tecnico/{idTecnico}")
    public ResponseEntity<TecnicoResponseRest> eliminarTecnico(@PathVariable Long idTecnico) {
        return tecnicoService.eliminarTecnico(idTecnico);
    }
    
    /**
     * Buscar tecnico
     * @param tecnico
     * @return 
     */
    @PostMapping("/buscar-tecnico")
    public ResponseEntity<TecnicoResponseRest> buscarTecnico(@RequestBody Tecnico tecnico) {
        return tecnicoService.buscarTecnico(tecnico);
    }
}
