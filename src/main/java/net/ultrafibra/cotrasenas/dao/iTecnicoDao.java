package net.ultrafibra.cotrasenas.dao;

import net.ultrafibra.cotrasenas.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iTecnicoDao  extends JpaRepository<Tecnico, Long>{
    
}
