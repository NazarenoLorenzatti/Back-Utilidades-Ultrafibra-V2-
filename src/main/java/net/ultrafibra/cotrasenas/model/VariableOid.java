package net.ultrafibra.cotrasenas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    
    @ManyToOne
    @JsonIgnore
    private OidClass oid;

    public VariableOid() {
    }

    public VariableOid(String valorVariable, String sintaxisVariable) {
        this.valorVariable = valorVariable;
        this.sintaxisVariable = sintaxisVariable;
    }
   
    
}
