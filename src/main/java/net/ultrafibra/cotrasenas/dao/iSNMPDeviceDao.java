package net.ultrafibra.cotrasenas.dao;

import net.ultrafibra.cotrasenas.model.SNMPDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iSNMPDeviceDao extends JpaRepository<SNMPDevice, Long>{
    
    public SNMPDevice findByIpDispositivo(String ipDispositivo);
}
