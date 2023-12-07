package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.SNMPDevice;

@Data
public class SNMPDeviceResponse {
    
    private List<SNMPDevice> SNMPDevices;
}
