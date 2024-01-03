package net.ultrafibra.utilidades.dao;

import net.ultrafibra.utilidades.model.RespuestaDebitos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iRespuestaDebitosDao extends JpaRepository<RespuestaDebitos, Long>{
    
}
