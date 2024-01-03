package net.ultrafibra.utilidades.dao;

import java.util.List;
import net.ultrafibra.utilidades.model.BaseHomebanking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iHomebankingDao extends JpaRepository<BaseHomebanking, Long> {

    public List<BaseHomebanking> findAllByIdCliente(String idCliente);

}
