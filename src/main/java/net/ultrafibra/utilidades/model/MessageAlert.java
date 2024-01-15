package net.ultrafibra.utilidades.model;

import lombok.*;

@Data
@AllArgsConstructor
public class MessageAlert {
    
    private String alert;
    private String device;
    private String dateAlert;
    private Long idDevice;
    private String priority;
}
