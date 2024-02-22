package net.ultrafibra.utilidades.model;

import lombok.Data;

@Data
public class Password {
    
    private String password;

    public Password(String password) {
        this.password = password;
    }

    public Password() {
    }
    
    
}
