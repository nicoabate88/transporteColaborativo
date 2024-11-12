
package universidad.tpteColaborativo.controladores;

import java.text.ParseException;
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
import universidad.tpteColaborativo.entidades.Usuario;
import universidad.tpteColaborativo.entidades.Viaje;
import universidad.tpteColaborativo.excepciones.MiException;
import universidad.tpteColaborativo.servicios.ViajeServicio;

@Controller
@RequestMapping("/viaje")
public class ViajeControlador {
    
    @Autowired
    private ViajeServicio viajeServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/busca")
    public String busca(@RequestParam(required = false) String origen, @RequestParam(required = false) String destino, 
            @RequestParam(required = false) String desde, @RequestParam(required = false) String hasta, ModelMap modelo) throws ParseException{
        
        if(origen.isEmpty()){
            origen = "";
        }
        if(destino.isEmpty()){
            destino = "";
        }
        
        ArrayList<Viaje> viajes = viajeServicio.buscarViajes(origen, destino, desde, hasta);
        
        boolean flag = false;
        if(viajes.isEmpty()){
            flag = true;
        }
        
        modelo.addAttribute("viajes", viajes);
        modelo.put("flag", flag);
        
        return "viaje_mostrar.html";
        
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/publicar")
    public String publicar(){
        
        return "viaje_publicar.html";
        
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/publica")
    public String publica(@RequestParam String origen, @RequestParam String destino, @RequestParam String fecha,
    @RequestParam Integer asiento, @RequestParam(required = false) String detalle, HttpSession session, ModelMap modelo) throws ParseException{
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        viajeServicio.publicarViaje(origen, destino, fecha, asiento, detalle, logueado);
        
        Long idViaje = viajeServicio.buscarUltimoViaje();
        Viaje viaje = viajeServicio.buscarViaje(idViaje);
        
        modelo.put("viaje", viaje);
        modelo.put("fecha", fecha);
        
        return "viaje_publicado.html";
        
    }
    
    @GetMapping("/mostrarConductorReserva")
    public String mostrarConductorReserva(HttpSession session, ModelMap modelo){
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        modelo.addAttribute("viajes", viajeServicio.buscarViajesReservaPendiente(logueado.getIdUsuario()));
        
        return "reserva_mostrarConductor.html";
        
    }
    
    @GetMapping("/mostrarCoordinarConductor")
    public String mostrarCoordinarConductor(HttpSession session, ModelMap modelo){
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        modelo.addAttribute("viajes", viajeServicio.buscarViajesReservaConfirmado(logueado.getIdUsuario()));
        
        return "coordinar_mostrarConductor.html";
        
    }
    
    @GetMapping("/mostrarCoordinarViajero")
    public String mostrarCoordinarViajero(HttpSession session, ModelMap modelo){
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        modelo.addAttribute("viajes", viajeServicio.buscarViajesReservaConfirmadoViajero(logueado.getIdUsuario()));
        
        return "coordinar_mostrarViajero.html";
        
    }
    
    @GetMapping("/modificar/{idViaje}")
    public String modificar(@PathVariable Long idViaje, ModelMap modelo){
        
        modelo.put("viaje", viajeServicio.buscarViaje(idViaje));
        
        return "viaje_modificar.html";
        
    }
    
    @PostMapping("/modifica")
    public String modifica(@RequestParam Long idViaje, @RequestParam String origen, @RequestParam String destino,
            @RequestParam String fecha, @RequestParam Integer asiento, @RequestParam String detalle, ModelMap modelo) throws ParseException{
        
        viajeServicio.modificar(idViaje, origen, destino, fecha, asiento, detalle);
        
        modelo.put("viaje", viajeServicio.buscarViaje(idViaje));
        modelo.put("fecha", fecha);
        
        return "viaje_modificado.html";
        
    }
    
    @GetMapping("/eliminar/{idViaje}")
    public String eliminar(@PathVariable Long idViaje, ModelMap modelo){
        
        modelo.put("viaje", viajeServicio.buscarViaje(idViaje));
        
        return "viaje_eliminar.html";
    }
    
    @GetMapping("/elimina/{idViaje}")
    public String elimina(@PathVariable Long idViaje, ModelMap modelo){
        
        viajeServicio.eliminar(idViaje);
        
        return "viaje_eliminado.html";
    }
    
}
