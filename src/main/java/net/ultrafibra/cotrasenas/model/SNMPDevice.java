package net.ultrafibra.cotrasenas.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "SNMP_DEVICE")
public class SNMPDevice implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_device")
    private Long idDispositivo;
    
    @Column(name="snmp_ip_device")
    private String ipDispositivo;
    
    @Column(name =  "device_name")
    private String nombreDispositivo;
    
    @OneToMany
    private List<Eventos> eventos;
    
    @ManyToMany
    @JoinTable(
        name = "SNMP_DEVICE_has_table_oids",
        joinColumns = @JoinColumn(name = "SNMP_DEVICE_id_device"),
        inverseJoinColumns = @JoinColumn(name = "table_oids_id_oid")
    )
    private Set<OidClass> oids = new HashSet<>();
    
    public SNMPDevice() {
    }

    public SNMPDevice(String ipDispositivo, String nombreDispositivo) {
        this.ipDispositivo = ipDispositivo;
        this.nombreDispositivo = nombreDispositivo;
    }
}
