package net.ultrafibra.cotrasenas.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "table_technicians")
public class Tecnico implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tech")
    private Long idTecnico;
    
    @Column(name = "name_tech")
    private String nombreTecnico;
    
    @Column(name = "tel_tech")
    private String telefono;

    public Tecnico() {
    }

    public Tecnico(String nombreTecnico, String telefono) {
        this.nombreTecnico = nombreTecnico;
        this.telefono = telefono;
    }


    
}
