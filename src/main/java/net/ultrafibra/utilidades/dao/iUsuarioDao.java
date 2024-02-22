package net.ultrafibra.utilidades.dao;

import net.ultrafibra.utilidades.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

// No es necesario crear una implementacion de esta interface ya que spring la crea automaticamente
public interface iUsuarioDao extends JpaRepository<Usuario, Long>{
    
    Usuario findByUsername(String username);
    
    public boolean existsByUsername(String username);   
    
    
}
