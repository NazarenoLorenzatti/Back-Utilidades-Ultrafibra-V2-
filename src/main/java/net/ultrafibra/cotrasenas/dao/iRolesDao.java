package net.ultrafibra.cotrasenas.dao;

import net.ultrafibra.cotrasenas.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iRolesDao extends JpaRepository<Rol, Long> {
    
    public Rol findByNombreRol(String nombreRol);
}
