package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.Log;

@Data
public class LogResponse {
    
    private List<Log> logs;
}
