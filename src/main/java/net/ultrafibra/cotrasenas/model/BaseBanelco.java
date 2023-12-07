package net.ultrafibra.cotrasenas.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import lombok.Data;

@Entity
@Data
@Table(name = "table_banelco")
public class BaseBanelco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_linea")
    private Long idLinea;
    
    @Column(name = "id_cliente")
    private String idCliente;
    
    @Column(name = "dni_cliente")
    private String dniCliente;
    
    @Column(name = "total_sin_recargo")
    private double totalSinRecargo;
    
    @Column(name = "total_con_recargo")
    private double totalConRecargo;
    
    @Column(name = "importe_adeudado")
    private double importeAdeudado;
    
    @Column(name = "fecha_factura")
    private Date fechaFactura;
    
    @Column(name = "fecha_vencimiento")
    private Date fechaVencimiento;
    
    @Column(name = "numero_factura")
    private String numeroFactura;
    
    @Column(name = "documento_origen")
    private String documentoOrigen;

    public BaseBanelco() {
    }
    
    public BaseBanelco(String idCliente, String dniCliente, double totalSinRecargo, 
            double totalConRecargo, double importeAdeudado, Date fechaFactura, 
            Date fechaVencimiento, String numeroFactura, String documentoOrigen) {
        
        this.idCliente = idCliente;
        this.dniCliente = dniCliente;
        this.totalSinRecargo = totalSinRecargo;
        this.totalConRecargo = totalConRecargo;
        this.importeAdeudado = importeAdeudado;
        this.fechaFactura = fechaFactura;
        this.fechaVencimiento = fechaVencimiento;
        this.numeroFactura = numeroFactura;
        this.documentoOrigen = documentoOrigen;
    }
    
}
