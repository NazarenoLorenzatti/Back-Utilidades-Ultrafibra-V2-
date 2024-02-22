package net.ultrafibra.utilidades.model;

import jakarta.persistence.*;
import java.io.Serializable;
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
    
    @Column(name = "alarm")
    private boolean isAlarm;

    public OidClass() {
    }

    public OidClass(String oid, String evento) {
        this.oid = oid;
        this.evento = evento;
    }

}
