package net.ultrafibra.cotrasenas.dao;

import java.util.List;
import net.ultrafibra.cotrasenas.model.Host;
import net.ultrafibra.cotrasenas.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iLogDao extends JpaRepository<Log, Long>{

    public List<Log> findByHost(Host host);
     
    public void deleteByHost(Host host);
      
}
