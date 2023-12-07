package net.ultrafibra.cotrasenas.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.Data;


@Data
@Entity
@Table(name = "administrativos")
public class Administrativo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_administrativo")
    private Long idAdministrativo;

    private String nombre;

    private String apellido;

    private String email;

    private String telefono;

    @OneToOne
    @JoinColumn(name = "usuario_id_usuario")
    private Usuario usuario;
    
     @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "administrativos_has_aplicaciones",
            joinColumns = @JoinColumn(name = "administrativos_id_administrativo"),
            inverseJoinColumns = @JoinColumn(name = "aplicaciones_id_aplicacion")
    )
     private List<Aplicacion> aplicaciones;
    
     public Administrativo() {
    }

    public Administrativo(String nombre, String apellido, String email, String telefono, Usuario usuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.usuario = usuario;
    }   

}
