package net.ultrafibra.utilidades.dao;

import java.util.Optional;
import net.ultrafibra.utilidades.model.Administrativo;
import net.ultrafibra.utilidades.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iAdministrativoDao extends JpaRepository<Administrativo, Long>{
    
    public Optional<Administrativo> findByUsuario(Usuario usuario);
}
