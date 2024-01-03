package net.ultrafibra.utilidades.service;

import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface iBaseLinkService {
    
    public ResponseEntity<byte[]> downloadTxt(String nombreArchivo) throws IOException;
}
