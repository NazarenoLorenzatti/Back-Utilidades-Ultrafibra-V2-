package net.ultrafibra.utilidades.dao;

import java.util.List;
import java.util.Optional;
import net.ultrafibra.utilidades.model.OidClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iOidDao  extends JpaRepository<OidClass, Long>{
    
    public OidClass findByEvento(String evento);
    
    public Optional<OidClass> findByOid(String oid);
    
    public List<OidClass> findByEventoContaining(String contiene);
}
