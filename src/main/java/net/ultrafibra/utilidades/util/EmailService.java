package net.ultrafibra.utilidades.util;

import lombok.extern.slf4j.Slf4j;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private Properties prop;
    private String remitente; 
    private String password; 
    private MimeMessage mCorreo;
    private Session mSession;

    public EmailService() {
        prop = new Properties();
        
        this.remitente = "alertas.ups.ultrafibra@gmail.com";
        this.password = "spgsodpzqyxfqjxh";
          
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.setProperty("mail.smtp.starttls.enable", "true");
        prop.setProperty("mail.smtp.port", "587");
        prop.setProperty("mail.smtp.user",this.remitente);
        prop.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        prop.setProperty("mail.smtp.auth", "true");
        
        this.mSession = Session.getDefaultInstance(prop);
    }

    public void enviarMail(String email, String asunto, String mensaje) {
        
       try {
            mCorreo = new MimeMessage(mSession);
            mCorreo.setFrom(new InternetAddress("nl.loragro@gmail.com"));
            mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mCorreo.setSubject(asunto);
            mCorreo.setText(mensaje, "ISO-8859-1", "html");
            
             Transport mTransport = mSession.getTransport("smtp");
            mTransport.connect(this.remitente, this.password);
            mTransport.sendMessage(mCorreo, mCorreo.getRecipients(Message.RecipientType.TO));
            mTransport.close();
            
            System.out.println("Correo Enviado = " + mCorreo.toString());                    
            
        } catch (AddressException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
        }  
       
    }
}
