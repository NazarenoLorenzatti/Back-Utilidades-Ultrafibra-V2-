package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.Host;

@Data
public class HostResponse {
    
    private List<Host> Hosts;
}
