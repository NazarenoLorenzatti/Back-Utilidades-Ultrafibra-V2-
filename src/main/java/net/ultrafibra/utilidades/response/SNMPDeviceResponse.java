package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.SNMPDevice;

@Data
public class SNMPDeviceResponse {
    
    private List<SNMPDevice> SNMPDevices;
}
