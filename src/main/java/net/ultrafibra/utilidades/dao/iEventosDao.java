package net.ultrafibra.utilidades.dao;


import java.util.List;
import net.ultrafibra.utilidades.model.Eventos;
import net.ultrafibra.utilidades.model.SNMPDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface iEventosDao extends JpaRepository<Eventos, Long> {

    public void deleteAllBySnmpDevice(SNMPDevice dispositivo);

    public List<Eventos> findAllBySnmpDevice(SNMPDevice dispositivo);

    @Query("SELECT e FROM Eventos e WHERE e.fechaEvento = (SELECT MAX(e2.fechaEvento) FROM Eventos e2 WHERE e2.snmpDevice = :dispositivo)")
    List<Eventos> findLatestEvents(@Param("dispositivo") SNMPDevice dispositivo);
    
    @Query("SELECT e FROM Eventos e WHERE e.fechaEvento = (SELECT MAX(e2.fechaEvento) FROM Eventos e2 WHERE e2.variableEvento = :variable)")
    List<Eventos> findLatestEvents(@Param("variable") String variable);
}
