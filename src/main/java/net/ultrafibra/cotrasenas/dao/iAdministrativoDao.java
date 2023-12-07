package net.ultrafibra.cotrasenas.dao;

import java.util.Optional;
import net.ultrafibra.cotrasenas.model.Administrativo;
import net.ultrafibra.cotrasenas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iAdministrativoDao extends JpaRepository<Administrativo, Long>{
    
    public Optional<Administrativo> findByUsuario(Usuario usuario);
}
