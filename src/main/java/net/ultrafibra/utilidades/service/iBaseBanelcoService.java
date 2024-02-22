package net.ultrafibra.utilidades.service;

import java.io.IOException;
import net.ultrafibra.utilidades.response.BaseBanelcoResponseRest;
import net.ultrafibra.utilidades.response.ResponseRest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface iBaseBanelcoService {
       
    public ResponseEntity<ResponseRest> eliminarTodo();
    
    public ResponseEntity<ResponseRest> leerExcel(MultipartFile fileExcel);
    
    public ResponseEntity<byte[]> downloadTxt(String nombreArchivo) throws IOException;
    
    public ResponseEntity<BaseBanelcoResponseRest> obtenerTabla();
    
    public ResponseEntity<ResponseRest> leerExcelRespuesta(MultipartFile fileExcel);
    
     public ResponseEntity<byte[]> downloadXLS(String nombreArchivo) throws IOException;
     
      public ResponseEntity<byte[]> downloadCsv(String nombreArchivo) throws IOException;
    
}
