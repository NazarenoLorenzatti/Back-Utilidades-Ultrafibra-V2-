package net.ultrafibra.cotrasenas.service;

import net.ultrafibra.cotrasenas.model.Usuario;
import net.ultrafibra.cotrasenas.response.UsuarioResponseRest;
import org.springframework.http.ResponseEntity;

public interface iUsuarioService {
    
   public ResponseEntity<UsuarioResponseRest> listarUsuarios();
   public ResponseEntity<UsuarioResponseRest> buscarUsuarioPorId(Long idUsuario);
   public ResponseEntity<UsuarioResponseRest> buscarUsuario(String username);
   public ResponseEntity<UsuarioResponseRest> guardarUsuario(Usuario usuario);
   public ResponseEntity<UsuarioResponseRest> actualizarUsuario(Usuario usuario);
   public ResponseEntity<UsuarioResponseRest> eliminarUsuario(Usuario usuario);
   public ResponseEntity<UsuarioResponseRest> eliminarUsuarioPorId(Long idUsuario);  
   public ResponseEntity<UsuarioResponseRest> subirFoto(String username, byte[] img);
   
}
