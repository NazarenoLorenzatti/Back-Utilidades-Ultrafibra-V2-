package net.ultrafibra.utilidades.dao;

import java.util.List;
import net.ultrafibra.utilidades.model.Host;
import net.ultrafibra.utilidades.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iLogDao extends JpaRepository<Log, Long>{

    public List<Log> findByHost(Host host);
     
    public void deleteByHost(Host host);
      
}
