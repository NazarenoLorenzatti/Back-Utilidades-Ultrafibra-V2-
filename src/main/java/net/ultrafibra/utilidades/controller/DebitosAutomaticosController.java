package net.ultrafibra.utilidades.controller;

import java.io.IOException;
import net.ultrafibra.utilidades.response.DebitosAutomaticosResponseRest;
import net.ultrafibra.utilidades.response.ResponseRest;
import net.ultrafibra.utilidades.response.RespuestaDebitosResponseRest;
import net.ultrafibra.utilidades.service.impl.DebitosAutomaticosServiceImpl;
import net.ultrafibra.utilidades.service.impl.RespuestaDebitosServiceImpl;
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
@RequestMapping("/api/v1/debitos")
public class DebitosAutomaticosController {

    @Autowired
    private DebitosAutomaticosServiceImpl debitosService;

    @Autowired
    private RespuestaDebitosServiceImpl respuestaService;

    // ------------------------------- Extracto Debitos ------------------------------------------
    /**
     * Vaciar toda la tabla de la BBDD cuando se termine el proceso
     *
     * @return
     */
    @DeleteMapping("/vaciar")
    public ResponseEntity<ResponseRest> eliminarTodo() {
        return debitosService.eliminarTodo();
    }

    /**
     * Cargar el Excel con la informacion par ala base de deuda
     *
     * @param fileExcel
     * @return
     */
    @PostMapping("/importar-excel")
    public ResponseEntity<ResponseRest> leerExcel(@RequestParam("fileExcel") MultipartFile fileExcel) {
        return debitosService.leerExcel(fileExcel);
    }

    /**
     * Descargar la base de deuda en formato TXT
     *
     * @param nombreArchivo
     * @param tipoDebito
     * @param fecha
     * @return
     * @throws IOException
     */
    @PostMapping("/descargar-extracto")
    public ResponseEntity<byte[]> downloadExtracto(@RequestParam("fileName") String nombreArchivo,
            @RequestParam("debit") String tipoDebito,
            @RequestParam("date") String fecha) throws IOException {
        return debitosService.downloadXLS(nombreArchivo, tipoDebito, fecha);
    }

    /**
     * Obtener todas las celdas de la tabla cargada
     *
     * @return
     */
    @GetMapping("/obtener-tabla")
    public ResponseEntity<DebitosAutomaticosResponseRest> obtenerTabla() {
        return debitosService.obtenerTabla();
    }

    // ------------------------------- Respuesta Debitos ------------------------------------------
    
    /**
     * Importar respuesta bancaria en Txt
     *
     * @param fileTxt
     * @return
     */
    @PostMapping("/importar-txt")
    public ResponseEntity<ResponseRest> leerTxt(@RequestParam("fileTxt") MultipartFile fileTxt) {
        return respuestaService.leerTxt(fileTxt);
    }

    /**
     * Descargar Respuesta procesada en Excel
     *
     * @return
     * @throws IOException
     */
    @GetMapping("/descargar-respuesta")
    public ResponseEntity<byte[]> downloadExtracto() throws IOException {
        return respuestaService.downloadXLS();
    }

    /**
     * Obtener todas las celdas de la tabla cargada
     *
     * @return
     */
    @GetMapping("/obtener-tabla-respuesta")
    public ResponseEntity<RespuestaDebitosResponseRest> obtenerTablaRespuesta() {
        return respuestaService.obtenerTabla();
    }
    
        /**
     * Vaciar toda la tabla de la BBDD cuando se termine el proceso
     *
     * @return
     */
    @DeleteMapping("/vaciar-respuesta")
    public ResponseEntity<ResponseRest> eliminarTodoRespuesta() {
        return respuestaService.eliminarTodo();
    }
}
