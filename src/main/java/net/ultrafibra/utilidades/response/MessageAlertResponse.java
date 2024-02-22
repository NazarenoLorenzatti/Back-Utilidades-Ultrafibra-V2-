package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.MessageAlert;

@Data
public class MessageAlertResponse {
    
    private List<MessageAlert> messageAlert;
}
