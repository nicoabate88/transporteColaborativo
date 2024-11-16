package universidad.tpteColaborativo.controladores;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import universidad.tpteColaborativo.entidades.Coordinacion;
import universidad.tpteColaborativo.entidades.Usuario;
import universidad.tpteColaborativo.servicios.CoordinacionServicio;
import universidad.tpteColaborativo.servicios.ViajeServicio;

@Controller
@RequestMapping("/coordinacion")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public class CoordinacionControlador {

    @Autowired
    private ViajeServicio viajeServicio;
    @Autowired
    private CoordinacionServicio coordinacionServicio;

    @GetMapping("/mostrar")
    public String mostrar(HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        Integer pendiente = viajeServicio.buscarCantidadReservaPendiente(logueado.getIdUsuario());
        Integer confirmado = viajeServicio.buscarCantidadViajesReservaConfirmada(logueado.getIdUsuario());
        Integer mensaje = viajeServicio.buscarCantidadViajesReservaConfirmadaViajero(logueado.getIdUsuario());
        Boolean flag = false;
        if (pendiente == 0 && confirmado == 0 && mensaje == 0) {
            flag = true;
        }

        modelo.put("pendiente", pendiente);
        modelo.put("confirmado", confirmado);
        modelo.put("mensaje", mensaje);
        modelo.put("flag", flag);

        return "mensaje_mostrar.html";

    }

    @GetMapping("/registrarConductor/{idViaje}")
    public String registrarConductor(@PathVariable Long idViaje, ModelMap modelo) {

        boolean flag = false;
        List<Coordinacion> lista = coordinacionServicio.buscarCoordinacionConductor(idViaje);
        if (!lista.isEmpty()) {
            flag = true;
        }

        modelo.put("viaje", viajeServicio.buscarViaje(idViaje));
        modelo.addAttribute("coordinaciones", lista);
        modelo.put("flag", flag);

        return "coordinar_registrarConductor.html";

    }

    @PostMapping("/registraConductor")
    public String registraConductor(@RequestParam Long idViaje, @RequestParam String mensaje, HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        coordinacionServicio.registrarCoordinacion(idViaje, mensaje, logueado);

        return "coordinacion_mostrar.html";
    }

    @PostMapping("/registraViajero")
    public String registraViajero(@RequestParam Long idViaje, @RequestParam String mensaje, HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        coordinacionServicio.registrarCoordinacion(idViaje, mensaje, logueado);

        return "coordinacion_mostrar.html";

    }

}
