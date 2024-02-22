package net.ultrafibra.utilidades.model;

import lombok.Data;

@Data
public class Item {
    private String Barra;
    private String Importe;
    private String Adicional;
    private String Clave1;
    private String Clave2;
    private String Clave3;
    private String Descripcion;

    public String getClave1() {
        return Clave1;
    }
}