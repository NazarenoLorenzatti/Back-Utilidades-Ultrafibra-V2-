package net.ultrafibra.utilidades.dao;

import java.util.List;
import net.ultrafibra.utilidades.model.DebitosAutomaticos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iDebitosAutomaticosDao extends JpaRepository<DebitosAutomaticos, Long>{
    
    public List<DebitosAutomaticos> findAllByIdCliente(String idCliente);
    
}
