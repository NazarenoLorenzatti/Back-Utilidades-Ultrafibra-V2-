package net.ultrafibra.cotrasenas.response;

import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.model.BaseHomebanking;

@Data
public class BaseBanelcoResponse {
    private List<BaseHomebanking> baseBanelco;
}
