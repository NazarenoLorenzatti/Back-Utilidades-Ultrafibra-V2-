package net.ultrafibra.cotrasenas.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Entity
@Data
@Table(name = "estado_credenciales")
public class EstadoCredencial implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long idEstado;
    
    @Column(name = "nombre_credencial")
    private String nombreEstado;
    
}
