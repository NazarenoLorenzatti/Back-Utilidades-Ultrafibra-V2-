package net.ultrafibra.utilidades.dao;

import net.ultrafibra.utilidades.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iTecnicoDao  extends JpaRepository<Tecnico, Long>{
    
}
