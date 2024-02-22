package net.ultrafibra.utilidades.dao;

import net.ultrafibra.utilidades.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iRolesDao extends JpaRepository<Rol, Long> {
    
    public Rol findByNombreRol(String nombreRol);
}
