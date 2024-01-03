package net.ultrafibra.utilidades.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.utilidades.model.BaseHomebanking;

@Data
public class BaseBanelcoResponse {
    private List<BaseHomebanking> baseBanelco;
}
