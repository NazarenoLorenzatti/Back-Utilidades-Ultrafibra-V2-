package net.ultrafibra.cotrasenas.dao;

import java.util.List;
import net.ultrafibra.cotrasenas.model.Eventos;
import net.ultrafibra.cotrasenas.model.SNMPDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iEventosDao extends JpaRepository<Eventos, Long> {

    public void deleteAllBySnmpDevice(SNMPDevice dispositivo);

    public List<Eventos> findAllBySnmpDevice(SNMPDevice dispositivo);
}
