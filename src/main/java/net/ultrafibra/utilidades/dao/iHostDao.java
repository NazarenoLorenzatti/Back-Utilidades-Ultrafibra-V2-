package net.ultrafibra.utilidades.dao;

import java.util.Optional;
import net.ultrafibra.utilidades.model.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iHostDao extends JpaRepository<Host, Long>{
    
    public Optional<Host> findByIpHost(String ipHost);
    
}
