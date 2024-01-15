package net.ultrafibra.utilidades.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "table_variables_oid")
public class VariableOid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variable_oid")
    private Long idVariable;

    @Column(name = "value_variable")
    private String valorVariable;

    @Column(name = "syntax_variable")
    private String sintaxisVariable;

    @Column(name = "priority")
    private String prioridad;

    @ManyToOne
    @JsonBackReference
    private OidClass oid;

    public VariableOid() {
    }

    public VariableOid(String valorVariable, String sintaxisVariable, String prioridad, OidClass oid) {
        this.valorVariable = valorVariable;
        this.sintaxisVariable = sintaxisVariable;
        this.prioridad = prioridad;
        this.oid = oid;
    }


}
