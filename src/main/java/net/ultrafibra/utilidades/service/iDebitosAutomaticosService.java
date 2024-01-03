package net.ultrafibra.utilidades.service;

import java.io.IOException;
import net.ultrafibra.utilidades.response.DebitosAutomaticosResponseRest;
import net.ultrafibra.utilidades.response.ResponseRest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface iDebitosAutomaticosService {

    public ResponseEntity<ResponseRest> eliminarTodo();

    public ResponseEntity<ResponseRest> leerExcel(MultipartFile fileExcel);

    public  ResponseEntity<byte[]> downloadXLS (String nombreArchivo, String tipoDebito, String fecha) throws IOException;

    public ResponseEntity<DebitosAutomaticosResponseRest> obtenerTabla();
}
