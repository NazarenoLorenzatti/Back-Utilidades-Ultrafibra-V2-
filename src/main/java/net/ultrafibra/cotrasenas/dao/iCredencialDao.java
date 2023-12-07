package net.ultrafibra.cotrasenas.dao;

import java.util.List;
import net.ultrafibra.cotrasenas.model.Administrativo;
import net.ultrafibra.cotrasenas.model.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iCredencialDao extends JpaRepository<Credencial, Long>{
    
    public List<Credencial> findByAdministrativo(Administrativo administrativo);
}
