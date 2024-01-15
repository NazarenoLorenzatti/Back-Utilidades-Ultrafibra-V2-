package net.ultrafibra.utilidades.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.dao.iEventosDao;
import net.ultrafibra.utilidades.model.Eventos;
import net.ultrafibra.utilidades.model.SNMPDevice;
import net.ultrafibra.utilidades.model.VariableOid;
import net.ultrafibra.utilidades.response.EventosResponseRest;
import net.ultrafibra.utilidades.service.iEventosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class EventosServiceImpl implements iEventosService {

     @Autowired
    private iEventosDao eventoDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<EventosResponseRest> listarEventos() {
        EventosResponseRest respuesta = new EventosResponseRest();
        try {
            List<Eventos> eventos = eventoDao.findAll();
            respuesta.getEventosResponse().setEventos(eventos);
            respuesta.setMetadata("Respuesta ok", "00", "Logs cargados");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<EventosResponseRest> registrarEvento(Eventos evento) {
        EventosResponseRest respuesta = new EventosResponseRest();
        List<Eventos> listaEventos = new ArrayList<>();
        try {
            if (evento != null) {
                Eventos eventoGuardado = eventoDao.save(evento);
                if (eventoGuardado != null) {
                    listaEventos.add(eventoGuardado);
                    respuesta.getEventosResponse().setEventos(listaEventos);
                    respuesta.setMetadata("Respuesta ok", "00", "Evento Guardado");                   
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Evento");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }

            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudo guardar el Evento, por que no se envio ninguno");
                return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<EventosResponseRest> eliminarEvento(Long idEvento) {
        EventosResponseRest respuesta = new EventosResponseRest();
        try {
            if (idEvento != null) {
                eventoDao.deleteById(idEvento);
                respuesta.setMetadata("Respuesta ok", "00", "Eventos eliminados");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudieron eliminar los Eventos");
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<EventosResponseRest> eliminarEventos(SNMPDevice dispositivo) {
        EventosResponseRest respuesta = new EventosResponseRest();
        try {
            if (dispositivo != null) {
                eventoDao.deleteAllBySnmpDevice(dispositivo);
                respuesta.setMetadata("Respuesta ok", "00", "Eventos eliminados");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se pudieron eliminar los Eventos");
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<EventosResponseRest> buscarEventosPorDispositivo(SNMPDevice dispositivo) {
        EventosResponseRest respuesta = new EventosResponseRest();
        try {
            if (dispositivo != null) {
                List<Eventos> eventosEncontrados = eventoDao.findAllBySnmpDevice(dispositivo);
                if (eventosEncontrados != null) {
                    respuesta.getEventosResponse().setEventos(eventosEncontrados);
                    respuesta.setMetadata("Respuesta ok", "00", "Eventos listados");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "Eventos no Encontrados");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }

            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontraron elementos por que Dispositivos es Null");
                return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
    
    @Transactional(readOnly = true)
    public Eventos ultimoEvento(SNMPDevice dispositivo){
        Eventos ultimoEvento = null;
        try{
          List<Eventos> listaEventos =  eventoDao.findLatestEvents(dispositivo);
          if(listaEventos != null){
              ultimoEvento = listaEventos.get(0);
          }
        } catch (Exception e) {            
             e.getStackTrace();
             System.out.println("Ocurrio un problema al intentar obtener el ultimo evento");
             return null;
        }
        return ultimoEvento;
    }
    
    @Transactional(readOnly = true)
     public Eventos ultimoEvento(String variable){
        Eventos ultimoEvento = null;
        try{
          List<Eventos> listaEventos =  eventoDao.findLatestEvents(variable);
          if(listaEventos != null){
              ultimoEvento = listaEventos.get(0);
          }
        } catch (Exception e) {            
             e.getStackTrace();
             System.out.println("Ocurrio un problema al intentar obtener el ultimo evento dee la variable");
             return null;
        }
        return ultimoEvento;
    }

}
