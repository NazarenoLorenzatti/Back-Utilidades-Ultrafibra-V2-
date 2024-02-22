package net.ultrafibra.utilidades.model;

import lombok.*;

@Data
@AllArgsConstructor
public class MessageAlert {
    
    private String device;
    private String dateAlert;
    private Long idDevice;
    private String batteryStatus;
    private String outputStatus;
    private int batteryCharge;
    private int temp;
    private int timeCharge;
    private int input1;
    private int input2;
    private int input3;
    private int output1;
    private int output2;
    private int output3;
    private int bypass1;
    private int bypass2;
    private int bypass3;
    
    public MessageAlert() {
    }
    
    
}
