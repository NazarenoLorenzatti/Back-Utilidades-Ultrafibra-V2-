package net.ultrafibra.utilidades.dao;

import net.ultrafibra.utilidades.model.Aplicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iAplicacionDao extends JpaRepository<Aplicacion, Long>{
    
}
