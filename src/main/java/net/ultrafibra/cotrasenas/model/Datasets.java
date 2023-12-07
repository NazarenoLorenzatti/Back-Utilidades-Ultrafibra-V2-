package net.ultrafibra.cotrasenas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import lombok.Data;

@Entity
@Data
@Table(name = "table_datasets_host")
public class Datasets implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dataset")
    private Long idDataset;

    @Column(name = "data_date")
    private Date fecha;

    @Column(name = "data_value")
    private int valor;

    @ManyToOne
    @JsonIgnore
    private Host host;

    public Datasets() {
    }

    public Datasets(Date fecha, int valor, Host host) {
        this.fecha = fecha;
        this.valor = valor;
        this.host = host;
    }
    
    

}
