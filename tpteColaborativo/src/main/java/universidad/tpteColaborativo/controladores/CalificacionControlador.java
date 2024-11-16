package universidad.tpteColaborativo.controladores;

import java.util.ArrayList;
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
import universidad.tpteColaborativo.entidades.Calificacion;
import universidad.tpteColaborativo.entidades.Usuario;
import universidad.tpteColaborativo.entidades.Viaje;
import universidad.tpteColaborativo.servicios.CalificacionServicio;
import universidad.tpteColaborativo.servicios.ViajeServicio;

@Controller
@RequestMapping("/calificacion")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public class CalificacionControlador {

    @Autowired
    private CalificacionServicio calificacionServicio;
    @Autowired
    private ViajeServicio viajeServicio;

    @PostMapping("/registraViajero")
    public String registrarViajero(@RequestParam Long idReserva, @RequestParam Integer puntuacion, @RequestParam String comentario,
            HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        calificacionServicio.crearCalificacionViajero(idReserva, puntuacion, comentario, logueado);

        Usuario usuario = new Usuario();

        if (logueado.getIdUsuario() != null) {
            usuario = logueado;
        }

        ArrayList<Viaje> viajes = viajeServicio.buscarViajesUsuarioConductor(logueado.getIdUsuario());
        ArrayList<Viaje> viajeReserva = viajeServicio.buscarViajesReservaConfirmadoViajero(logueado.getIdUsuario());

        modelo.put("usuario", usuario);
        modelo.addAttribute("viajes", viajes);
        modelo.addAttribute("viajeReserva", viajeReserva);
        modelo.addAttribute("calificacionViajero", viajeServicio.buscarViajesCalificacionViajero(logueado.getIdUsuario()));
        modelo.addAttribute("calificacionConductor", viajeServicio.buscarViajesCalificacionConductor(logueado.getIdUsuario()));

        return "index.html";

    }

    @PostMapping("/registraConductor")
    public String registrarConductor(@RequestParam Long idReserva, @RequestParam Long idViajero, @RequestParam Integer puntuacion, @RequestParam String comentario,
            HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        calificacionServicio.crearCalificacionConductor(idReserva, idViajero, puntuacion, comentario, logueado);

        Usuario usuario = new Usuario();

        if (logueado.getIdUsuario() != null) {
            usuario = logueado;
        }

        ArrayList<Viaje> viajes = viajeServicio.buscarViajesUsuarioConductor(logueado.getIdUsuario());
        ArrayList<Viaje> viajeReserva = viajeServicio.buscarViajesReservaConfirmadoViajero(logueado.getIdUsuario());

        modelo.put("usuario", usuario);
        modelo.addAttribute("viajes", viajes);
        modelo.addAttribute("viajeReserva", viajeReserva);
        modelo.addAttribute("calificacionViajero", viajeServicio.buscarViajesCalificacionViajero(logueado.getIdUsuario()));
        modelo.addAttribute("calificacionConductor", viajeServicio.buscarViajesCalificacionConductor(logueado.getIdUsuario()));

        return "index.html";

    }

    @GetMapping("/mostrarViajero/{idViajero}")
    public String mostrarCalificacionViajero(@PathVariable Long idViajero, ModelMap modelo) {

        ArrayList<Calificacion> lista = calificacionServicio.buscarCalificacionViajero(idViajero);
        boolean flag = false;

        if (!lista.isEmpty()) {
            flag = true;
        }

        modelo.addAttribute("calificaciones", lista);
        modelo.put("flag", flag);

        return "calificacion_mostrarViajero.html";
    }

    @PostMapping("/mostrarConductor")
    public String mostrarComentario(@RequestParam Long idConductor, @RequestParam(required = false) String origen,
            @RequestParam(required = false) String destino, @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta, ModelMap modelo) {

        ArrayList<Calificacion> lista = calificacionServicio.buscarCalificacionConductor(idConductor);
        boolean flag = false;

        for (Calificacion c : lista) {
            System.out.println("Calificaccion id " + c.getIdCalificacion());

        }

        if (!lista.isEmpty()) {
            flag = true;
        }

        modelo.addAttribute("calificaciones", lista);
        modelo.put("flag", flag);
        modelo.put("origen", origen);
        modelo.put("destino", destino);
        modelo.put("desde", desde);
        modelo.put("hasta", hasta);

        return "calificacion_mostrarConductor.html";
    }

}
