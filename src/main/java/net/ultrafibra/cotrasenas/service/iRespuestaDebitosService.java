package net.ultrafibra.cotrasenas.service;

import java.io.IOException;
import net.ultrafibra.cotrasenas.response.ResponseRest;
import net.ultrafibra.cotrasenas.response.RespuestaDebitosResponseRest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface iRespuestaDebitosService {

    public ResponseEntity<ResponseRest> eliminarTodo();

    public ResponseEntity<ResponseRest> leerTxt(MultipartFile fileExcel);

    public ResponseEntity<byte[]> downloadXLS() throws IOException;

    public ResponseEntity<RespuestaDebitosResponseRest> obtenerTabla();
}
