package net.ultrafibra.cotrasenas.security;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.cotrasenas.dao.iUsuarioDao;
import net.ultrafibra.cotrasenas.model.Rol;
import net.ultrafibra.cotrasenas.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private iUsuarioDao usuarioDao;

   @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Rol rol : usuario.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(rol.getNombreRol()));
        }
        return new org.springframework.security.core.userdetails.User(
            usuario.getUsername(), usuario.getPassword(), authorities);
    }

}
