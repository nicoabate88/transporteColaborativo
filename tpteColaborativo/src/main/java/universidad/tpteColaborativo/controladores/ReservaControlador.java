
package universidad.tpteColaborativo.controladores;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import universidad.tpteColaborativo.entidades.Usuario;
import universidad.tpteColaborativo.excepciones.MiException;
import universidad.tpteColaborativo.servicios.ReservaServicio;
import universidad.tpteColaborativo.servicios.ViajeServicio;

@Controller
@RequestMapping("/reserva")
public class ReservaControlador {
    
    @Autowired
    private ReservaServicio reservaServicio;
    @Autowired
    private ViajeServicio viajeServicio;
    
    @GetMapping("/reservarAsiento/{idViaje}")
    public String reservar(@PathVariable Long idViaje, ModelMap modelo){
        
        modelo.put("viaje", viajeServicio.buscarViaje(idViaje));
        
        return "viaje_reservar.html";
    }
    
    @PostMapping("/reservaAsiento")
    public String reserva(@RequestParam Long idViaje, @RequestParam Integer cantidadAsiento, HttpSession session, ModelMap modelo){
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        try {
        
        reservaServicio.reservarAsiento(idViaje, cantidadAsiento, logueado);
        
        modelo.put("viaje", viajeServicio.buscarViaje(idViaje));
        modelo.put("asiento", cantidadAsiento);
        
        return "reserva_mostrar.html";
        
        } catch (MiException ex) {
            
            modelo.put("error", ex.getMessage());

            return "reserva_mostrarError.html";
            
        }
    }
    
    @GetMapping("/mostrarReservaConductor")
    public String mostrarReservaConductor(HttpSession session, ModelMap modelo){
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        modelo.addAttribute("reservas", reservaServicio.buscarReservaConductor(logueado.getIdUsuario()));
        
        return "reserva_mostrarConductor.html";
    }

    @GetMapping("/confirma/{idReserva}")
    public String confirma(@PathVariable Long idReserva, ModelMap modelo){
        
        reservaServicio.confirmarReserva(idReserva);
        
        modelo.put("reserva", reservaServicio.buscarReserva(idReserva));
        modelo.put("viaje", viajeServicio.buscarViajeIdReserva(idReserva));
        
        return "reserva_confirmada.html";
    }
    
    @GetMapping("/rechazar/{idReserva}")
    public String rechazar(@PathVariable Long idReserva, ModelMap modelo){
        
        modelo.put("reserva", reservaServicio.buscarReserva(idReserva));
        modelo.put("viaje", viajeServicio.buscarViajeIdReserva(idReserva));
        
        return "reserva_rechazar.html";
    }
    
    @GetMapping("/rechaza/{idReserva}")
    public String rechaza(@PathVariable Long idReserva){
        
        reservaServicio.rechazarReserva(idReserva);
        
        return "reserva_rechazada.html";
            
    }
    
}
