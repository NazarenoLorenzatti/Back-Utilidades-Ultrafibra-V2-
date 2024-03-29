package net.ultrafibra.utilidades.controller;

import net.ultrafibra.utilidades.model.Host;
import net.ultrafibra.utilidades.response.HostResponseRest;
import net.ultrafibra.utilidades.service.impl.HostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {
    "*",
    "http://45.230.65.207",
    "http://localhost",
    "http://192.168.1.77"})
@RequestMapping("/api/v1/hosts")
public class HostController {

    @Autowired
    private HostServiceImpl hostService;

    /**
     * Todas las listas de host cargadas
     *
     * @return
     */
    @GetMapping("/listar-host")
    public ResponseEntity<HostResponseRest> listarHosts() {
        return hostService.listarHosts();
    }

    /**
     * Guardar nuevo host
     *
     * @param host
     * @return
     */
    @PostMapping("/guardar-host")
    public ResponseEntity<HostResponseRest> guardarHost(@RequestBody Host host) {
        return hostService.guardarHost(host);
    }

    /**
     * Eliminar el host en base a su ID
     *
     * @param idHost
     * @return
     */
    @DeleteMapping("/eliminar-host/{idHost}")
    public ResponseEntity<HostResponseRest> eliminarHost(@PathVariable Long idHost) {
        return hostService.eliminarHost(idHost);
    }

    /**
     * Editar el dispositivo guardado
     *
     * @param host
     * @return
     */
    @PutMapping("/editar-host")
    public ResponseEntity<HostResponseRest> editarHost(@RequestBody Host host) {
        return hostService.editarHost(host);
    }

    /**
     * Buscar el Host por el Ip
     *
     * @param ipHost
     * @return
     */
    @GetMapping("/buscar-host-ip/{ipHost}")
    public ResponseEntity<HostResponseRest> buscarHostPorIp(@PathVariable String ipHost) {
        return hostService.buscarHostPorIp(ipHost);
    }

    /**
     *Hacer un Ping individual
     * @param host
     */
    @PostMapping("/hacer-ping")
    public void hacerPing(@RequestBody Host host) {
        hostService.pingHost(host);
    }
}
