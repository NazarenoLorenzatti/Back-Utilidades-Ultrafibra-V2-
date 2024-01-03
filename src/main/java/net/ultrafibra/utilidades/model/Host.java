package net.ultrafibra.utilidades.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "table_hosts")
public class Host implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_host")
    private Long idHost;

    @Column(name = "ip_host")
    private String ipHost;

    @Column(name = "name_host")
    private String nombreHost;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL)
    private List<Log> logs = new ArrayList<>();

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL)
    private List<Datasets> datasets = new ArrayList<>();

    public Host() {
    }

    public Host(String ipHost, String nombreHost) {
        this.ipHost = ipHost;
        this.nombreHost = nombreHost;
    }

}
