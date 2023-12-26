package net.ultrafibra.cotrasenas.controller;

import java.io.IOException;
import net.ultrafibra.cotrasenas.response.BaseBanelcoResponseRest;
import net.ultrafibra.cotrasenas.response.ResponseRest;
import net.ultrafibra.cotrasenas.service.impl.BaseBanelcoServiceImpl;
import net.ultrafibra.cotrasenas.service.impl.BaseLinkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = {
    "*",
    "http://45.230.65.207",
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1/homebanking")
public class HomebankingController {
    
    @Autowired 
    private BaseBanelcoServiceImpl banelcoService;
    
    @Autowired 
    private BaseLinkServiceImpl linkService;
    
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
     * @param nombreArchivo
     * @return
     * @throws IOException 
     */
    @GetMapping("/descargar-base/{nombreArchivo}")
    public ResponseEntity<byte[]> downloadTxt(@PathVariable String nombreArchivo) throws IOException {
        return banelcoService.downloadTxt(nombreArchivo);
    }
    
    /**
     * Obtener todas las celdas de la tabla cargada
     * @return 
     */
    @GetMapping("/obtener-tabla")
    public ResponseEntity<BaseBanelcoResponseRest> obtenerTabla() {
        return banelcoService.obtenerTabla();
    }
    
        
    /**
     * Decargar la base de deudad de la red link en formato TXT
     * @param nombreArchivo
     * @return
     * @throws IOException 
     */
    @GetMapping("/descargar-baselink/{nombreArchivo}")
    public ResponseEntity<byte[]> downloadTxtLink(@PathVariable String nombreArchivo) throws IOException {
        return linkService.downloadTxt(nombreArchivo);
    }
    
}
