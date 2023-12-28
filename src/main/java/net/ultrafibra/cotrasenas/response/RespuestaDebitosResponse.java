package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.RespuestaDebitos;

@Data
public class RespuestaDebitosResponse {
     private List<RespuestaDebitos> respuestaDebitos;
}
