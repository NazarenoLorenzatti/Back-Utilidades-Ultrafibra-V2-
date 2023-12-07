package net.ultrafibra.cotrasenas.dao;

import net.ultrafibra.cotrasenas.model.Aplicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iAplicacionDao extends JpaRepository<Aplicacion, Long>{
    
}
