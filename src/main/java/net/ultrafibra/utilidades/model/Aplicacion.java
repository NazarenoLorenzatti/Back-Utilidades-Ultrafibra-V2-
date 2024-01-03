package net.ultrafibra.utilidades.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "aplicaciones")
public class Aplicacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aplicacion")
    private Long idAplicacion;

    @Column(name = "nombre_aplicacion")
    private String nombreAplicacion;
    
    private String url;   
    
    @ManyToMany()
    @JsonIgnore
    @JoinTable(
            name = "administrativos_has_aplicaciones",
            joinColumns = @JoinColumn(name = "aplicaciones_id_aplicacion"),
            inverseJoinColumns = @JoinColumn(name = "administrativos_id_administrativo")
    )
     private List<Administrativo> administrativos;

    public Aplicacion() {
    }

    public Aplicacion(Long idAplicacion, String nombreAplicacion, String url) {
        this.idAplicacion = idAplicacion;
        this.nombreAplicacion = nombreAplicacion;
        this.url = url;
    }
    
}
