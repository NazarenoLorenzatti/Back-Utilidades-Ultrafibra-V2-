package net.ultrafibra.cotrasenas.dao;

import java.util.List;
import net.ultrafibra.cotrasenas.model.Datasets;
import net.ultrafibra.cotrasenas.model.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iDatasetsDao extends JpaRepository<Datasets, Long>{
    
    public List<Datasets> findByHost(Host host);
}
