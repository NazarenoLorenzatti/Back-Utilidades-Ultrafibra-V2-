package net.ultrafibra.cotrasenas.dao;

import java.util.Optional;
import net.ultrafibra.cotrasenas.model.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iHostDao extends JpaRepository<Host, Long>{
    
    public Optional<Host> findByIpHost(String ipHost);
    
}
