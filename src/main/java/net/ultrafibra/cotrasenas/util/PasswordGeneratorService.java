package net.ultrafibra.cotrasenas.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.cotrasenas.model.Password;
import net.ultrafibra.cotrasenas.response.PasswordResponseRest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PasswordGeneratorService {

    private static final String UPPER_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%&()=+.";

    @Transactional(readOnly = true)
    public ResponseEntity<PasswordResponseRest> generarPassword() {
        PasswordResponseRest respuesta = new PasswordResponseRest();
        List<Password> passwordList = new ArrayList();
        try {
            passwordList.add(generarContraseña());
            respuesta.getPasswordResponse().setPassword(passwordList);
            respuesta.setMetadata("Respuesta ok", "00", "Contraseña Generada");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "No se pudo generar la contraseña");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    public Password generarContraseña() {
        int largoContraseña = 12;
        String caracteresValidos = UPPER_CHARACTERS + LOWER_CHARACTERS + NUMBERS + SPECIAL_CHARACTERS;
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < largoContraseña; i++) {
            int randomIndex = random.nextInt(caracteresValidos.length());
            password.append(caracteresValidos.charAt(randomIndex));
        }
        return new Password(password.toString());
    }

    public Integer generarPin() {
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900000) + 100000;
        return numeroAleatorio;
    }

}
