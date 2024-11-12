
package universidad.tpteColaborativo.controladores;

import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import universidad.tpteColaborativo.entidades.Usuario;
import universidad.tpteColaborativo.entidades.Viaje;
import universidad.tpteColaborativo.servicios.ViajeServicio;

@Controller
@RequestMapping("/")
public class PortalControlador {
    
    @Autowired
    private ViajeServicio viajeServicio;
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            
            modelo.put("error", "Usuario o Contraseña incorrecto");
        }

        return "login.html";
    }
    
    
    @GetMapping("/index")
    public String index(HttpSession session, ModelMap modelo) {
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Usuario usuario = new Usuario();
        
        if(logueado.getIdUsuario() != null){
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
}
