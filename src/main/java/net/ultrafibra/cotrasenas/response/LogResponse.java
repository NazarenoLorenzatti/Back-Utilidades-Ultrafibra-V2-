package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.Log;

@Data
public class LogResponse {
    
    private List<Log> logs;
}
