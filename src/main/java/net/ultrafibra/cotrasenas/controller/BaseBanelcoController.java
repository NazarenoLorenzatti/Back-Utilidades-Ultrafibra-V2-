package net.ultrafibra.cotrasenas.controller;

import java.io.IOException;
import net.ultrafibra.cotrasenas.response.ResponseRest;
import net.ultrafibra.cotrasenas.service.impl.BaseBanelcoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = {
    "http://45.230.65.207",
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1/banelco")
public class BaseBanelcoController {
    
    @Autowired 
    private BaseBanelcoServiceImpl banelcoService;
    
    /**
     * Vaciar toda la tabla de la BBDD cuando se termine el proceso
     * @return 
     */
    @DeleteMapping("/vaciar")
    public ResponseEntity<ResponseRest> eliminarTodo() {
        return banelcoService.eliminarTodo();
    }
    
    /**
     * Cargar el Excel con la informacion par ala base de deuda
     * @param fileExcel
     * @return 
     */
    @PostMapping("/importar-excel")
    public ResponseEntity<ResponseRest> leerExcel(@RequestParam("fileExcel") MultipartFile fileExcel) {
        return banelcoService.leerExcel(fileExcel);
    }
    
    /**
     * Descargar la base de deuda en formato TXT
     * @return
     * @throws IOException 
     */
    @GetMapping("/descargar-base/{nombreArchivo}")
    public ResponseEntity<byte[]> downloadTxt(@PathVariable String nombreArchivo) throws IOException {
        return banelcoService.downloadTxt(nombreArchivo);
    }
}
