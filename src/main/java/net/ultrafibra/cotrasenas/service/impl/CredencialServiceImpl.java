package net.ultrafibra.cotrasenas.service.impl;

import net.ultrafibra.cotrasenas.util.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.cotrasenas.dao.*;
import net.ultrafibra.cotrasenas.excepciones.*;
import net.ultrafibra.cotrasenas.model.Administrativo;
import net.ultrafibra.cotrasenas.model.Credencial;
import net.ultrafibra.cotrasenas.model.Password;
import net.ultrafibra.cotrasenas.response.CredencialResponseRest;
import net.ultrafibra.cotrasenas.response.PasswordResponseRest;
import net.ultrafibra.cotrasenas.service.iCredencialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CredencialServiceImpl implements iCredencialService {

    @Autowired
    private iCredencialDao credencialDao;

    @Autowired
    private iEstadoCredencialDao estadoDao;

    @Autowired
    private iAdministrativoDao administrativoDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private PasswordGeneratorService passwordGenerator;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CredencialResponseRest> listarCredenciales() {
        CredencialResponseRest respuesta = new CredencialResponseRest();
        try {
            List<Credencial> credenciales = (List<Credencial>) credencialDao.findAll();
            respuesta.getCredencialResponse().setCredencial(credenciales);
            respuesta.setMetadata("Respuesta ok", "00", "Lista de Credenciales cargados");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CredencialResponseRest> buscarCredencial(Credencial credencial) {
        CredencialResponseRest respuesta = new CredencialResponseRest();
        List<Credencial> listaCredenciales = new ArrayList<>();
        try {
            Optional<Credencial> credencialOptional = credencialDao.findById(credencial.getIdCredencial());
            if (credencialOptional.isPresent()) {
                listaCredenciales.add(credencialOptional.get());
                respuesta.getCredencialResponse().setCredencial(listaCredenciales);
                respuesta.setMetadata("Respuesta ok", "00", "Credencial por Id encontrada");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro la Credencial");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CredencialResponseRest> nuevaCredencial(Credencial credencial) {
        CredencialResponseRest respuesta = new CredencialResponseRest();
        List<Credencial> listaCredenciales = new ArrayList<>();
        try {
            credencial.setEstadoCredencial(estadoDao.findByNombreEstado("VIGENTE"));
            credencial.setUltimaActualizacion(Date.valueOf(LocalDate.now()));
            credencial.setProximaActualizacion(Date.valueOf(LocalDate.now().plusDays(60)));
            Credencial credencialNueva = credencialDao.save(credencial);
            if (credencialNueva != null) {
                listaCredenciales.add(credencialNueva);
                respuesta.getCredencialResponse().setCredencial(listaCredenciales);
                respuesta.setMetadata("Respuesta ok", "00", "Credencial por Id encontrada");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro la Credencial");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CredencialResponseRest> editarCredencial(Credencial credencial) {
        CredencialResponseRest respuesta = new CredencialResponseRest();
        List<Credencial> listaCredenciales = new ArrayList<>();
        try {
            Optional<Credencial> credencialOptional = credencialDao.findById(credencial.getIdCredencial());
            if (credencialOptional.isPresent()) {

                credencialOptional.get().setUsuario(credencial.getUsuario());
                credencialOptional.get().setContra(credencial.getContra());
                credencialOptional.get().setEstadoCredencial(estadoDao.findByNombreEstado("VIGENTE"));
                credencialOptional.get().setUltimaActualizacion(Date.valueOf(LocalDate.now()));
                credencialOptional.get().setProximaActualizacion(Date.valueOf(LocalDate.now().plusDays(60)));

                Credencial credencialEditada = credencialDao.save(credencialOptional.get());

                if (credencialEditada != null) {
                    listaCredenciales.add(credencialEditada);
                    respuesta.getCredencialResponse().setCredencial(listaCredenciales);
                    respuesta.setMetadata("Respuesta ok", "00", "Se edito la credencial");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se pudo editar la Credencial");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro la Credencial");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CredencialResponseRest> actualizarContraseña(Credencial credencial) {
        CredencialResponseRest respuesta = new CredencialResponseRest();
        List<Credencial> listaCredenciales = new ArrayList<>();
        try {
            Optional<Credencial> credencialOptional = credencialDao.findById(credencial.getIdCredencial());
            if (credencialOptional.isPresent()) {

                credencialOptional.get().setContra(credencial.getContra());
                credencialOptional.get().setEstadoCredencial(estadoDao.findByNombreEstado("VIGENTE"));
                credencialOptional.get().setUltimaActualizacion(Date.valueOf(LocalDate.now()));
                credencialOptional.get().setProximaActualizacion(Date.valueOf(LocalDate.now().plusDays(60)));

                Credencial credencialEditada = credencialDao.save(credencialOptional.get());

                if (credencialEditada != null) {
                    listaCredenciales.add(credencialEditada);
                    respuesta.getCredencialResponse().setCredencial(listaCredenciales);
                    respuesta.setMetadata("Respuesta ok", "00", "Se edito la credencial");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se pudo editar la Credencial");
                    return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                }
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro la Credencial");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CredencialResponseRest> eliminarCredencial(Long idCredencial) {
        CredencialResponseRest respuesta = new CredencialResponseRest();
        try {
            credencialDao.deleteById(idCredencial);
            respuesta.setMetadata("Respuesta ok", "00", "Credencial eliminada");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al eliminar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CredencialResponseRest> cambiarEstado() {
        List<Credencial> credenciales = credencialDao.findAll();
        CredencialResponseRest respuesta = new CredencialResponseRest();
        List<Credencial> listaCredenciales = new ArrayList<>();
        try {
            for (Credencial c : credenciales) {
                if (c.getProximaActualizacion().before(c.getUltimaActualizacion())) {
                    c.setEstadoCredencial(estadoDao.findByNombreEstado("CAMBIAR"));

                    // ENVIO DE LA ALERTA POR MAIL
                    try {
                        emailService.enviarMail(c.getAdministrativo().getEmail(), "Actualizacion de Contraseña: " + c.getAplicacion().getNombreAplicacion(),
                                "Su contraseña de la aplicacion " + c.getAplicacion().getNombreAplicacion() + " Requiere una actualizacion");
                    } catch (EmailException e) {
                        respuesta.setMetadata("Respuesta nok", "-1", "Error al enviar el correo electrónico.");
                        e.printStackTrace();
                        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    // ENVIO DE ALERTA POR SMS
                    try {
                        smsService.enviarMensaje("Su contraseña de la aplicacion " + c.getAplicacion().getNombreAplicacion() + " Requiere una actualizacion", c.getAdministrativo().getTelefono());
                    } catch (SMSException e) {
                        respuesta.setMetadata("Respuesta nok", "-1", "Error al enviar el SMS");
                        e.printStackTrace();
                        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    Credencial credencialAlerta = credencialDao.save(c);
                    if (credencialAlerta != null) {
                        listaCredenciales.add(credencialAlerta);
                        respuesta.getCredencialResponse().setCredencial(listaCredenciales);
                        respuesta.setMetadata("Respuesta ok", "00", "Se cambio el estado de la credencial y se envio alerta");
                    } else {
                        respuesta.setMetadata("Respuesta nok", "-1", "No se pudo cambiar el estado de la Credencial");
                        return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
                    }
                }
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Se produjo un error al momento de cambiar de estado");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CredencialResponseRest> buscarCredencialPorId(Long idCredencial) {
        CredencialResponseRest respuesta = new CredencialResponseRest();
        List<Credencial> listaCredenciales = new ArrayList<>();
        try {
            Optional<Credencial> credencialOptional = credencialDao.findById(idCredencial);
            if (credencialOptional.isPresent()) {
                listaCredenciales.add(credencialOptional.get());
                respuesta.getCredencialResponse().setCredencial(listaCredenciales);
                respuesta.setMetadata("Respuesta ok", "00", "Credencial por Id encontrada");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro la Credencial");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CredencialResponseRest> buscarCredencialPorAdministrativo(Long idAdministrativo) {
        CredencialResponseRest respuesta = new CredencialResponseRest();
        Administrativo adm = administrativoDao.findById(idAdministrativo).get();
        try {
            List<Credencial> credenciales = credencialDao.findByAdministrativo(adm);
            respuesta.getCredencialResponse().setCredencial(credenciales);
            respuesta.setMetadata("Respuesta ok", "00", "Lista de Credenciales cargados");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PasswordResponseRest> generarPassword() {
        PasswordResponseRest respuesta = new PasswordResponseRest();
        List<Password> passwords = new ArrayList<>();
        try {
            passwords.add(this.passwordGenerator.generarContraseña());
            respuesta.getPasswordResponse().setPassword(passwords);
            respuesta.setMetadata("Respuesta ok", "00", "Contraseña Generada");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al generar contraseña");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Scheduled(cron = "0 0 * * * *") // Ejecutar cada hora
    public void comprobarVigencia() {
        List<Credencial> credenciales = credencialDao.findAll();
        for (Credencial c : credenciales) {
            if (c.getEstadoCredencial().getNombreEstado().equals("VIGENTE")) {
                if (c.getUltimaActualizacion().after(c.getProximaActualizacion())) {
                    
                    c.setEstadoCredencial(estadoDao.findByNombreEstado("CAMBIAR"));
                    credencialDao.save(c);
                    
                    smsService.enviarMensaje("Sus credenciales de la APP: "+ c.getAplicacion().getNombreAplicacion() 
                            + " Necesitan un cambio", c.getAdministrativo().getTelefono());
                    
                    emailService.enviarMail(c.getAdministrativo().getEmail(),"Cambiar credenciales "
                            +  c.getAplicacion().getNombreAplicacion(),"Sus credenciales de la APP: "
                                    + c.getAplicacion().getNombreAplicacion()+ " Necesitan un cambio");
                    
                    System.out.println("EJECUTADO" + LocalDate.now().toString());
                }
            }
        }
    }
}
