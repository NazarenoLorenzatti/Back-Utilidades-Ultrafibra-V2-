package net.ultrafibra.utilidades.excepciones;

public class SMSException extends RuntimeException {
    public SMSException(String message) {
        super(message);
    }

    public SMSException(String message, Throwable cause) {
        super(message, cause);
    }
}
