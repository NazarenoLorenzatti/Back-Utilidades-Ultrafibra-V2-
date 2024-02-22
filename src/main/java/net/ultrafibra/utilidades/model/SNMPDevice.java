package net.ultrafibra.utilidades.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "snmp_ip_device")
    private String ipDispositivo;

    @Column(name = "device_name")
    private String nombreDispositivo;

    @Column(name = "community")
    private String comunidad;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "SNMP_DEVICE_id_device")
    private List<Eventos> eventos;
    
    @OneToOne
    @JoinColumn(name = "table_hosts_id_host")
    private Host host;

    public SNMPDevice() {
    }

    public SNMPDevice(String ipDispositivo, String nombreDispositivo) {
        this.ipDispositivo = ipDispositivo;
        this.nombreDispositivo = nombreDispositivo;
    }
}
