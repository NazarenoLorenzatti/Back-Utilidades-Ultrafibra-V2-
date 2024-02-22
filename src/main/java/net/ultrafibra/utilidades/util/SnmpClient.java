package net.ultrafibra.utilidades.util;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.security.*;
import org.snmp4j.security.USM;
import org.snmp4j.security.SecurityModels;

import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Service;
import org.snmp4j.mp.MPv3;

@Service
public class SnmpClient {

    private Snmp snmp;
    private static final int SNMP_TRAP_PORT = 162;
    private static final String SNMP_COMMUNITY = "MegalinkRo";

    public SnmpClient() throws Exception {
        // Inicializa el cliente SNMP
        TransportMapping<?> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    public ResponseEvent getSnmpData(String targetAddress, String community, String oid) throws Exception {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setVersion(SnmpConstants.version2c); // Utiliza la versión SNMP adecuada
        target.setAddress(new UdpAddress(targetAddress + "/161")); // Especifica la dirección y el puerto SNMP del dispositivo

        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid))); // Agrega el OID que deseas consultar

        pdu.setType(PDU.GET);

        // Realiza la consulta SNMP
        ResponseEvent response = snmp.send(pdu, target);
        
        if (response != null && response.getResponse() != null 
                && response.getResponse().get(0).getVariable().toString() != "noSuchInstance") {            
            return response;
        } else {
            return null;
        }
    }
}
