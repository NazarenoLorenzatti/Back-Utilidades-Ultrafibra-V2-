package net.ultrafibra.cotrasenas.dao;

import java.util.List;
import net.ultrafibra.cotrasenas.model.BaseHomebanking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iBaseBanelcoDao extends JpaRepository<BaseHomebanking, Long>{
    
    public List<BaseHomebanking> findAllByIdCliente(String idCliente);
    
}
