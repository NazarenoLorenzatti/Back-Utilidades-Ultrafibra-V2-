package net.ultrafibra.utilidades.dao;

import net.ultrafibra.utilidades.model.EstadoCredencial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iEstadoCredencialDao extends JpaRepository<EstadoCredencial, Long>{
    
    public EstadoCredencial findByNombreEstado(String estadoCredencial);
}
