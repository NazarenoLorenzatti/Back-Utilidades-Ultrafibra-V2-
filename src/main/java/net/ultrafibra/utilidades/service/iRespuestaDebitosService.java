package net.ultrafibra.utilidades.service;

import java.io.IOException;
import net.ultrafibra.utilidades.response.ResponseRest;
import net.ultrafibra.utilidades.response.RespuestaDebitosResponseRest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface iRespuestaDebitosService {

    public ResponseEntity<ResponseRest> eliminarTodo();

    public ResponseEntity<ResponseRest> leerTxt(MultipartFile fileExcel);

    public ResponseEntity<byte[]> downloadXLS() throws IOException;

    public ResponseEntity<RespuestaDebitosResponseRest> obtenerTabla();
}
