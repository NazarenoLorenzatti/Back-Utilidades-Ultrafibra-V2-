package net.ultrafibra.cotrasenas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

@Entity
@Data
@Table(name = "table_logs")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Long idLog;

    @Column(name = "log_run_star")
    private Timestamp inicio;

    @Column(name = "log_run_end")
    private Timestamp fin;

    @Column(name="log_hours_run", nullable = true)
    private Double diferenciaEnHoras;

    @ManyToOne
    @JsonIgnore
    private Host host;

    public Log() {
    }

    public Log(Timestamp inicio, Timestamp fin, Double diferenciaEnHoras, Host host) {
        this.inicio = inicio;
        this.fin = fin;
        this.diferenciaEnHoras = diferenciaEnHoras;
        this.host = host;
    }
    
    

}
