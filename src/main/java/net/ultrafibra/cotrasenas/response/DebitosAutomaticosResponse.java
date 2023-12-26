package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.DebitosAutomaticos;

@Data
public class DebitosAutomaticosResponse {
     private List<DebitosAutomaticos> debitosAutomaticos;
}
