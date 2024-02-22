package net.ultrafibra.utilidades.model;

import java.sql.Timestamp;
import lombok.*;

@Data
@AllArgsConstructor
public class MessaggePing {
    
    private Long idHost;
    private String ipHost;
    private String nombreHost;
    private boolean alcanzable;
}
