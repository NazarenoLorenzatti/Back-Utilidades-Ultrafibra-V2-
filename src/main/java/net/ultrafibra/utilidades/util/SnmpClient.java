package net.ultrafibra.utilidades.util;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Service;

@Service
public class SnmpClient {

    private Snmp snmp;

    public SnmpClient() throws Exception {
        // Inicializa el cliente SNMP
        TransportMapping<?> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    public  ResponseEvent getSnmpData(String targetAddress, String community, String oid) throws Exception {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setVersion(SnmpConstants.version2c); // Utiliza la versión SNMP adecuada
        target.setAddress(new UdpAddress(targetAddress + "/161")); // Especifica la dirección y el puerto SNMP del dispositivo

        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid))); // Agrega el OID que deseas consultar

        pdu.setType(PDU.GET);

        // Realiza la consulta SNMP
        ResponseEvent response = snmp.send(pdu, target);
                
        if (response != null && response.getResponse() != null) {
            //return response.getResponse().get(0).getVariable().toString();
            return response;
        } else {
            return null;
        }
    }
}
