package net.ultrafibra.utilidades.util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.excepciones.SMSException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SMSService {

    private static String token;

    public int enviarMensaje(String aviso, String celular) {
        int ret = 0;
        try {
            nuevoToken();
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post("https://mayten.cloud/api/Mensajes/Texto")
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .body(bodyResponse(aviso, celular))
                    .asString();
            ret = response.getStatus();
            System.out.println("retorno = " + ret);

        } catch (UnirestException ex) {
             throw new SMSException("Error al enviar el mensaje SMS.", ex);
        }
        return ret;
    }

    //SOLICITAR TOKEN NUEVO
    private static void nuevoToken() {
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post("http://mayten.cloud/auth")
                    .header("Content-Type", "application/json")
                    .body("{\r\n\t\"username\": \"megalink\",\r\n\t\"password\": \"megalink123\"\r\n}")
                    .asString();
            String body = response.getBody();
            token = body.substring(10, 314);

        } catch (UnirestException ex) {
            Logger.getLogger(SMSService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String bodyResponse(String aviso, String celular) {

        String body = 
                "{\r\n    \""
                + "origen\": \"SMS_CORTO\",\r\n    \""                
                + "mensajes\": ["
                                    + "\r\n{"
                                        + "\r\n\"mensaje\": \"" + aviso + "\","
                                        + "\r\n\"telefono\": \""+ celular + "\","
                                        + "\r\n\"identificador\":\"\"\r\n"
                                    + "},\r\n    "
                        + "]\r\n"
                + "}";
        
        return body;
    }
}
