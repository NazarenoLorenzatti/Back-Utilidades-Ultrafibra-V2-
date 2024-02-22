package net.ultrafibra.utilidades.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Data;

@Entity
@Data
@Table(name = "table_debits")
public class DebitosAutomaticos implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_table_debits")
    private Long idLinea;
    
    @Column(name = "cbu")
    private String cbu;
    
    @Column(name = "id_client")
    private String idCliente;
    
    @Column(name = "account_name")
    private String titularCuenta;
    
    @Column(name = "mount")
    private double totalAdeudado;
    
    @Column(name = "invoice_name")
    private String nombreFactura;
    
}
