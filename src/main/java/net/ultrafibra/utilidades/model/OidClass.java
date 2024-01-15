package net.ultrafibra.utilidades.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "table_oids")
public class OidClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oid")
    private Long idOid;

    private String oid;

    @Column(name = "event")
    private String evento;

    @Column(name = "oid_input")
    private boolean isInput;

    @OneToMany(mappedBy = "oid", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<VariableOid> variables = new ArrayList<>();

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "SNMP_DEVICE_id_device")
    private SNMPDevice snmpDevice;

    public OidClass() {
    }

    public OidClass(String oid, String evento, List<VariableOid> variables) {
        this.oid = oid;
        this.evento = evento;
        this.variables = variables;
    }

}
