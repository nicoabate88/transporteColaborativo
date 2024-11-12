
package universidad.tpteColaborativo.servicios;

import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import universidad.tpteColaborativo.entidades.Calificacion;
import universidad.tpteColaborativo.entidades.Reserva;
import universidad.tpteColaborativo.entidades.Usuario;
import universidad.tpteColaborativo.repositorios.CalificacionRepositorio;
import universidad.tpteColaborativo.repositorios.UsuarioRepositorio;

@Service
public class CalificacionServicio {
    
    @Autowired
    private ReservaServicio reservaServicio;
    @Autowired
    private CalificacionRepositorio calificacionRepositorio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Transactional
    public void crearCalificacionViajero(Long idReserva, Integer puntuacion, String comentario, Usuario usuario){
        
        Reserva reserva = reservaServicio.buscarReserva(idReserva);
        
        Calificacion calificacion = new Calificacion();
        
        calificacion.setPuntuacion(puntuacion);
        calificacion.setComentario(comentario);
        calificacion.setFecha(new Date());
        calificacion.setUsuarioViajero(usuario);
        calificacion.setUsuarioConductor(reserva.getUsuarioConductor());
        calificacion.setTipo("VIAJERO");
        
        calificacionRepositorio.save(calificacion);
        
        reservaServicio.agregarCalificacionViajero(idReserva, calificacion);
        
        usuarioServicio.agregarCalificacionConductor(calificacion);
        
    }
    
    @Transactional
    public void crearCalificacionConductor(Long idReserva, Long idViajero, Integer puntuacion, String comentario, Usuario usuario){
        
        Usuario viajero = usuarioServicio.buscarUsuario(idViajero);
        
        Calificacion calificacion = new Calificacion();
        
        calificacion.setPuntuacion(puntuacion);
        calificacion.setComentario(comentario);
        calificacion.setFecha(new Date());
        calificacion.setUsuarioViajero(viajero);
        calificacion.setUsuarioConductor(usuario);
        calificacion.setTipo("CONDUCTOR");
        
        calificacionRepositorio.save(calificacion);
        
        reservaServicio.agregarCalificacionConductor(idReserva, calificacion);
        
        usuarioServicio.agregarCalificacionViajero(calificacion);
        
    }
    
}
