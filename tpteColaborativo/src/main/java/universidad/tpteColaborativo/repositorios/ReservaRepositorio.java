
package universidad.tpteColaborativo.repositorios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import universidad.tpteColaborativo.entidades.Reserva;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, Long> {
 
    @Query(value = "SELECT r.* FROM reserva r " +
                   "JOIN viaje_reserva vr ON r.id_reserva = vr.reserva_id_reserva " +
                   "WHERE vr.viaje_id_viaje = :viajeId AND r.estado = 'PENDIENTE'", nativeQuery = true)
    List<Reserva> findReservasPendientesByViajeId(@Param("viajeId") Long viajeId);
    
    @Query(value = "SELECT COUNT(*) FROM reserva r " +
                   "JOIN viaje_reserva vr ON r.id_reserva = vr.reserva_id_reserva " +
                   "WHERE vr.viaje_id_viaje = :idViaje AND r.estado = 'PENDIENTE'", nativeQuery = true)
    Long countReservasPendientesByViajeId(@Param("idViaje") Long idViaje);
    
    @Query("SELECT r FROM Reserva r WHERE usuario_conductor_id_usuario = :id AND r.estado = 'PENDIENTE'")
    public ArrayList<Reserva> buscarReservaConductor(@Param("id") Long id);
    
    @Query("SELECT COUNT(r) FROM Reserva r WHERE usuario_conductor_id_usuario = :id AND r.estado = 'PENDIENTE'")
    Long reservaPendiente(@Param("id") Long id);
    
    @Query("SELECT COUNT(r) FROM Reserva r WHERE usuario_conductor_id_usuario = :id AND r.estado = 'CONFIRMADO'")
    Long reservaConfirmado(@Param("id") Long id);
    
    @Query("SELECT COUNT(r) FROM Reserva r WHERE fechaViaje < :fechaActual AND usuario_viajero_id_usuario = :idUsuario AND r.estado = 'CONFIRMADO'")
    Integer buscarCantidadReservaConfirmadoViajero(@Param("fechaActual") Date fechaActual, @Param("idUsuario") Long idUsuario);
    
}
    

