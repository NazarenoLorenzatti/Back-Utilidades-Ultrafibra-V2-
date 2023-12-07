package net.ultrafibra.cotrasenas.service;

import java.io.IOException;
import net.ultrafibra.cotrasenas.response.ResponseRest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface iBaseBanelcoService {
       
    public ResponseEntity<ResponseRest> eliminarTodo();
    
    public ResponseEntity<ResponseRest> leerExcel(MultipartFile fileExcel);
    
    public ResponseEntity<byte[]> downloadTxt(String nombreArchivo) throws IOException;
}
