package net.ultrafibra.utilidades.response;

import lombok.*;

@Getter
@Setter
public class MessageAlertResponseRest extends ResponseRest{
    
    private MessageAlertResponse messageAlertResponse = new MessageAlertResponse();
}
