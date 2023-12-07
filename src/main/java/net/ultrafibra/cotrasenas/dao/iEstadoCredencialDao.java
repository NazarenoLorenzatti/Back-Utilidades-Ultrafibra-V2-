package net.ultrafibra.cotrasenas.dao;

import net.ultrafibra.cotrasenas.model.EstadoCredencial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iEstadoCredencialDao extends JpaRepository<EstadoCredencial, Long>{
    
    public EstadoCredencial findByNombreEstado(String estadoCredencial);
}
