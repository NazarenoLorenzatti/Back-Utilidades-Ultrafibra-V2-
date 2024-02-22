package net.ultrafibra.utilidades.dao;

import java.util.List;
import net.ultrafibra.utilidades.model.BaseHomebanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface iHomebankingDao extends JpaRepository<BaseHomebanking, Long> {

    public List<BaseHomebanking> findAllByIdCliente(String idCliente);

       @Modifying
    @Transactional
    @Query(value = "ALTER TABLE table_banelco AUTO_INCREMENT = 1", nativeQuery = true)
    public void reiniciarIndices();
}
