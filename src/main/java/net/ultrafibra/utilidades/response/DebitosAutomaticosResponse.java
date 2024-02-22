package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.DebitosAutomaticos;

@Data
public class DebitosAutomaticosResponse {
     private List<DebitosAutomaticos> debitosAutomaticos;
}
