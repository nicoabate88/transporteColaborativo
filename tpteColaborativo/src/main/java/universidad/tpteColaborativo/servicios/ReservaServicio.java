
package universidad.tpteColaborativo.servicios;

import java.util.ArrayList;
import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import universidad.tpteColaborativo.entidades.Calificacion;
import universidad.tpteColaborativo.entidades.Reserva;
import universidad.tpteColaborativo.entidades.Usuario;
import universidad.tpteColaborativo.entidades.Viaje;
import universidad.tpteColaborativo.excepciones.MiException;
import universidad.tpteColaborativo.repositorios.ReservaRepositorio;

@Service
public class ReservaServicio {
    
    @Autowired
    private ReservaRepositorio reservaRepositorio;
    @Autowired
    private ViajeServicio viajeServicio;
    
    @Transactional
    public void reservarAsiento(Long idViaje, Integer cantidad, Usuario usuario) throws MiException{
        
        Viaje viaje = viajeServicio.buscarViaje(idViaje);
        
        validarUsuario(viaje, usuario);
        
        Reserva reserva = new Reserva();
        
        reserva.setFecha(new Date());
        reserva.setFechaViaje(viaje.getFecha());
        reserva.setUsuarioViajero(usuario);
        reserva.setUsuarioConductor(viaje.getUsuarioConductor());
        reserva.setAsientoReservado(cantidad);
        reserva.setEstado("PENDIENTE");
        
        reservaRepositorio.save(reserva);
        
        viajeServicio.reservarViaje(idViaje, reserva);
        
    }
    
    @Transactional
    public void confirmarReserva(Long idReserva){
        
        Reserva reserva = reservaRepositorio.getById(idReserva);

        reserva.setEstado("CONFIRMADO");
        
        reservaRepositorio.save(reserva); 
 
    }
    
    @Transactional
    public void rechazarReserva(Long idReserva){
        
        Reserva reserva = reservaRepositorio.getById(idReserva);
        
        viajeServicio.rechazarReservaViaje(reserva);
        
        reservaRepositorio.delete(reserva);
        
    }
    
    public ArrayList<Reserva> buscarReservaConductor(Long idUsuario){
        
        return reservaRepositorio.buscarReservaConductor(idUsuario);
        
    }
    
    public Reserva buscarReserva(Long idReserva){
    
        return reservaRepositorio.getById(idReserva);
        
    }
    
    public Integer buscarCantidadConfirmadoViajero(Long idUsuario){
        
        Date fechaActual = new Date();
        
        return reservaRepositorio.buscarCantidadReservaConfirmadoViajero(fechaActual, idUsuario);
    }
    
    @Transactional
    public void agregarCalificacionViajero(Long idReserva, Calificacion calificacion){
        
        Reserva reserva = reservaRepositorio.getById(idReserva);
        
        reserva.setCalificacionViajero(calificacion);
        
        reservaRepositorio.save(reserva);
        
    }
    
    @Transactional
    public void agregarCalificacionConductor(Long idReserva, Calificacion calificacion){
        
        Reserva reserva = reservaRepositorio.getById(idReserva);
        
        reserva.setCalificacionConductor(calificacion);
        
        reservaRepositorio.save(reserva);
        
    }
    
    public void validarUsuario(Viaje viaje, Usuario usuario) throws MiException{
        
        if(viaje.getUsuarioConductor().getIdUsuario() == usuario.getIdUsuario()){
            throw new MiException("Usted es conductor de este viaje");
        }
        
        if((viaje.getReserva() != null)){
              for(Reserva reserva : viaje.getReserva()){
                  if(reserva.getUsuarioViajero().getIdUsuario() == usuario.getIdUsuario()){
                      throw new MiException("Usted ya tiene Reserva de este viaje");
                  }
        }
    }    
    
}
    
}