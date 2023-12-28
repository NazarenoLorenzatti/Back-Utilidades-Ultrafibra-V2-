/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.ultrafibra.cotrasenas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import lombok.Data;

@Entity
@Data
@Table(name = "table_debits_response")
public class RespuestaDebitos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_response")
    private Long idLinea;

    @Column(name = "id_client")
    private String idCliente;
    
    @Column(name = "pay_date")
    private Date fechaDePago;
    
    @Column(name= "label")
    private String etiqueta;

    @Column(name = "mount")
    private double totalPagado;
    
    @Column(name= "reason")
    private String motivo;

}
