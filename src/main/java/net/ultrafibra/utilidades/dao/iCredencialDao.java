package net.ultrafibra.utilidades.dao;

import java.util.List;
import net.ultrafibra.utilidades.model.Administrativo;
import net.ultrafibra.utilidades.model.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iCredencialDao extends JpaRepository<Credencial, Long>{
    
    public List<Credencial> findByAdministrativo(Administrativo administrativo);
}
