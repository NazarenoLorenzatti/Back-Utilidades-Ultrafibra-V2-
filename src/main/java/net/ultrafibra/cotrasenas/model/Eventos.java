package net.ultrafibra.cotrasenas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "table_events")
public class Eventos implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event")
    private Long idEvento;
    
    @Column(name = "event_date")
    private Timestamp fechaEvento;
    
    @Column(name = "event_log")
    private String logEvento;
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "SNMP_DEVICE_id_device")
    private SNMPDevice snmpDevice;

    public Eventos() {
    }

    public Eventos(Timestamp fechaEvento, String logEvento, SNMPDevice snmpDevice) {
        this.fechaEvento = fechaEvento;
        this.logEvento = logEvento;
        this.snmpDevice = snmpDevice;
    }
    
    
}
