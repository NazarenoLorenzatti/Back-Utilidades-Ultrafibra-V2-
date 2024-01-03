package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.model.Datasets;
import net.ultrafibra.utilidades.model.Host;
import net.ultrafibra.utilidades.response.DatasetsResponseRest;
import net.ultrafibra.utilidades.service.impl.DatasetsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {
    "http://45.230.65.207", 
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1/datasets")
public class DatasetsController {
    
     @Autowired
     private DatasetsServiceImpl dataService;
     
     /**
      * Listar todos los datos guardados
      * @return 
      */
     @GetMapping("/listar-datos")
     public ResponseEntity<DatasetsResponseRest> listarDatos(){
         return dataService.listarData();
     }
     
     /**
      * Listar todos los datos de un Host para graficar
      * @param host
      * @return 
      */
     @PostMapping("/listar-datos")
     public ResponseEntity<DatasetsResponseRest> listarDatosDeHost(@RequestBody Host host){
         return dataService.listarDataHost(host);
     }
     
     /**
      * Guardar nuevo dato para graficar
      * @param data
      * @return 
      */
     @PostMapping("/registrar-dato")
     public ResponseEntity<DatasetsResponseRest> registrarData(@RequestBody Datasets data){
         return dataService.registrarData(data);
     }
}
