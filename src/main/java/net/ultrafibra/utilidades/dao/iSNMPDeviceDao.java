package net.ultrafibra.utilidades.dao;

import net.ultrafibra.utilidades.model.SNMPDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iSNMPDeviceDao extends JpaRepository<SNMPDevice, Long>{
    
    public SNMPDevice findByIpDispositivo(String ipDispositivo);
}
