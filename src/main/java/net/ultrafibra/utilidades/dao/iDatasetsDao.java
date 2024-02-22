package net.ultrafibra.utilidades.dao;

import java.util.List;
import net.ultrafibra.utilidades.model.Datasets;
import net.ultrafibra.utilidades.model.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iDatasetsDao extends JpaRepository<Datasets, Long>{
    
    public List<Datasets> findByHost(Host host);
}
