package net.ultrafibra.cotrasenas.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import net.ultrafibra.cotrasenas.dao.iUsuarioDao;
import net.ultrafibra.cotrasenas.model.Jwt;
import net.ultrafibra.cotrasenas.model.Usuario;
import net.ultrafibra.cotrasenas.response.JWTResponseRest;
import net.ultrafibra.cotrasenas.response.ResponseRest;
import net.ultrafibra.cotrasenas.security.JwtUtil;
import net.ultrafibra.cotrasenas.security.UserDetailsServiceImp;
import net.ultrafibra.cotrasenas.service.iLoginService;
import net.ultrafibra.cotrasenas.util.EmailService;
import net.ultrafibra.cotrasenas.util.PasswordGeneratorService;
import net.ultrafibra.cotrasenas.util.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Data
public class LoginServiceImpl implements iLoginService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImp userDetails;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SMSService smsService;

    @Autowired
    private iUsuarioDao usuarioDao;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordGeneratorService passwordGenerator;

    @Override
    public ResponseEntity<ResponseRest> primerFactor(String userName, String password) {
        ResponseRest respuesta = new ResponseRest();
        try {
            UserDetails userDetails = this.userDetails.loadUserByUsername(userName);
            if (this.passwordEncoder.matches(password, userDetails.getPassword())) {
                Usuario usuario = usuarioDao.findByUsername(userName);
                usuario.setPin(passwordGenerator.generarPin());
                usuario = usuarioDao.save(usuario);

                smsService.enviarMensaje("Este es su Pin para ingresar al gestor de contraseñas:"
                        + " " + usuario.getPin(), usuario.getAdministrativo().getTelefono());
                emailService.enviarMail(usuario.getAdministrativo().getEmail(), "PIN PAR AUTENTICAR", usuario.getPin().toString());
                respuesta.setMetadata("Respuesta ok", "00", "Login correcto");
                return new ResponseEntity<>(respuesta, HttpStatus.OK);
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "credenciales incorrectas");
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
            }

        } catch (UsernameNotFoundException e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al itentar en el servidor al intentar acceder");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<JWTResponseRest> login(String userName, String password, int pin) {
        JWTResponseRest respuesta = new JWTResponseRest();
        List<Jwt> listaJWT = new ArrayList<>();
        try {
            UserDetails userDetails = this.userDetails.loadUserByUsername(userName);

            if (pin == usuarioDao.findByUsername(userName).getPin()) {
                listaJWT.add(new Jwt(this.jwtUtil.encode(userDetails.getUsername()), this.jwtUtil.getExpiresDate()));
                respuesta.getJwtResponse().setJwt(listaJWT);
                respuesta.setMetadata("Respuesta ok", "00", "Login correcto");
                return new ResponseEntity<>(respuesta, HttpStatus.OK);
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "PIN INCORRECTO");
                return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
            }

        } catch (UsernameNotFoundException e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al itentar en el servidor al intentar acceder");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
