
package universidad.tpteColaborativo.repositorios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import universidad.tpteColaborativo.entidades.Viaje;

@Repository
public interface ViajeRepositorio extends JpaRepository<Viaje, Long> {
    
    @Query(value = "SELECT v.* FROM Viaje v WHERE v.fecha BETWEEN :fechaDesde " +
                   " AND :fechaHasta AND v.estado != 'EJECUTADO'", nativeQuery = true)
    public ArrayList<Viaje> buscarTodos(@Param("fechaDesde") LocalDate fechaDesde, 
                                        @Param("fechaHasta") LocalDate fechaHasta);
    
    @Query(value = "SELECT v.* FROM Viaje v WHERE v.fecha BETWEEN :fechaDesde " +
                   " AND :fechaHasta AND v.destino = :destino AND v.estado != 'EJECUTADO'", nativeQuery = true)
    public ArrayList<Viaje> buscarDestino(@Param("fechaDesde") LocalDate fechaDesde, 
                                          @Param("fechaHasta") LocalDate fechaHasta, @Param("destino") String destino);
    
    @Query(value = "SELECT v.* FROM Viaje v WHERE v.fecha BETWEEN :fechaDesde " +
                   " AND :fechaHasta AND v.origen = :origen AND v.estado != 'EJECUTADO'", nativeQuery = true)
    public ArrayList<Viaje> buscarOrigen(@Param("fechaDesde") LocalDate fechaDesde, 
                                         @Param("fechaHasta") LocalDate fechaHasta, @Param("origen") String origen);
    
    @Query(value = "SELECT v.* FROM Viaje v WHERE v.fecha BETWEEN :fechaDesde " +
                   " AND :fechaHasta AND v.origen = :origen AND v.destino = :destino " +
                   " AND v.estado != 'EJECUTADO'", nativeQuery = true)
    public ArrayList<Viaje> buscarOrigenDestino(@Param("fechaDesde") LocalDate fechaDesde, 
            @Param("fechaHasta") LocalDate fechaHasta, @Param("origen") String origen, @Param("destino") String destino);
    
    @Query("SELECT v FROM Viaje v WHERE usuario_conductor_id_usuario = :id AND v.estado != 'EJECUTADO'")
    public ArrayList<Viaje> buscarViajesConductor(@Param("id") Long id);
    
    @Query(value = "SELECT v.* FROM Viaje v " +
                   "JOIN viaje_reserva vr ON v.id_viaje = vr.viaje_id_viaje " +
                   "WHERE vr.reserva_id_reserva = :idReserva", nativeQuery = true)
    Viaje findViajeByReservaId(@Param("idReserva") Long idReserva);
    
    @Query(value = "SELECT v.id_viaje FROM Viaje v " +
                   "JOIN viaje_reserva vr ON v.id_viaje = vr.viaje_id_viaje " +
                   "WHERE vr.reserva_id_reserva = :idReserva", nativeQuery = true)
    Long findIdViajeByReservaId(@Param("idReserva") Long idReserva);
    
    @Query("SELECT MAX(id) FROM Viaje")
    public Long ultimoViaje();
    
    @Query(value = "SELECT DISTINCT v.* FROM Viaje v " +
                   "JOIN viaje_reserva vr ON v.id_viaje = vr.viaje_id_viaje " +
                   "JOIN Reserva r ON r.id_reserva = vr.reserva_id_reserva " +
                   "WHERE v.fecha >= :fechaActual " +
                   "AND r.estado = 'CONFIRMADO' " +
                   "AND v.usuario_conductor_id_usuario = :idUsuario", nativeQuery = true)
    ArrayList<Viaje> findViajesConReservasConfirmadasDesdeFechaPorUsuario(@Param("fechaActual") LocalDate fechaActual, 
                                                                     @Param("idUsuario") Long idUsuario);
    
    @Query(value = "SELECT DISTINCT v.* FROM Viaje v " +
                   "JOIN viaje_reserva vr ON v.id_viaje = vr.viaje_id_viaje " +
                   "JOIN Reserva r ON r.id_reserva = vr.reserva_id_reserva " +
                   "WHERE v.fecha >= :fechaActual " +
                   "AND r.estado = 'PENDIENTE' " +
                   "AND v.usuario_conductor_id_usuario = :idUsuario", nativeQuery = true)
    ArrayList<Viaje> findViajesConReservasPendienteDesdeFechaPorUsuario(@Param("fechaActual") LocalDate fechaActual, 
                                                                     @Param("idUsuario") Long idUsuario);
    
    @Query(value = "SELECT DISTINCT v.* FROM Viaje v " +
                   "JOIN viaje_reserva vr ON v.id_viaje = vr.viaje_id_viaje " +
                   "JOIN Reserva r ON r.id_reserva = vr.reserva_id_reserva " +
                   "WHERE v.fecha >= :fechaActual " +
                   "AND r.usuario_viajero_id_usuario = :idUsuario", nativeQuery = true)
    ArrayList<Viaje> findViajesConReservasConfirmadasDesdeFechaPorUsuarioViajero(@Param("fechaActual") LocalDate fechaActual, 
                                                                     @Param("idUsuario") Long idUsuario);
    
    @Query(value = "SELECT DISTINCT v.* FROM Viaje v " +
                   "JOIN viaje_reserva vr ON v.id_viaje = vr.viaje_id_viaje " +
                   "JOIN Reserva r ON r.id_reserva = vr.reserva_id_reserva " +
                   "WHERE v.fecha >= :fechaActual " +
                   "AND r.estado = 'CONFIRMADO' " +
                   "AND r.usuario_viajero_id_usuario = :idUsuario", nativeQuery = true)
    ArrayList<Viaje> findViajesConReservasConfirmadasUsuarioViajero(@Param("fechaActual") LocalDate fechaActual, 
                                                                     @Param("idUsuario") Long idUsuario);
    
    @Query(value = "SELECT DISTINCT v.* FROM Viaje v " +
                   "JOIN viaje_reserva vr ON v.id_viaje = vr.viaje_id_viaje " +
                   "JOIN Reserva r ON r.id_reserva = vr.reserva_id_reserva " +
                   "WHERE v.fecha < :fechaActual " +
                   "AND r.estado = 'CONFIRMADO' " +
                   "AND r.usuario_viajero_id_usuario = :idUsuario", nativeQuery = true)
    ArrayList<Viaje> buscarViajesReservasConfirmadasViajero(@Param("fechaActual") LocalDate fechaActual, 
                                                                     @Param("idUsuario") Long idUsuario);
    
    @Query("SELECT COUNT(v) FROM Viaje v WHERE fecha < :fechaActual AND estado != 'DISPONIBLE' AND usuario_conductor_id_usuario = :idUsuario")
    Integer buscarCantidadViajesEjecutadoUsuario(@Param("fechaActual") Date fechaActual, @Param("idUsuario") Long idUsuario);
    
    @Query(value = "SELECT v.* FROM Viaje v " +
                   "WHERE v.fecha < :fechaActual " +
                   "AND v.estado != 'DISPONIBLE' " +
                   "AND v.usuario_conductor_id_usuario = :idUsuario", nativeQuery = true)
    ArrayList<Viaje> buscarViajesEjecutadoUsuario(@Param("fechaActual") LocalDate fechaActual, 
                                                   @Param("idUsuario") Long idUsuario);
    
    @Query(value = "SELECT v.* FROM Viaje v " +
                   "WHERE v.fecha >= :fechaActual " +
                   "AND v.usuario_conductor_id_usuario = :idUsuario", nativeQuery = true)
    ArrayList<Viaje> buscarViajesUsuarioCondctor(@Param("fechaActual") LocalDate fechaActual, 
                                                   @Param("idUsuario") Long idUsuario);
    
    @Query(value = "SELECT DISTINCT v.* FROM Viaje v " +
                   "JOIN viaje_reserva vr ON v.id_viaje = vr.viaje_id_viaje " +
                   "JOIN Reserva r ON r.id_reserva = vr.reserva_id_reserva " +
                   "WHERE v.fecha < :fechaActual " +
                   "AND r.estado = 'CONFIRMADO' " +
                   "AND r.calificacion_viajero_id_calificacion is NULL " +
                   "AND r.usuario_viajero_id_usuario = :idUsuario" , nativeQuery = true)
    ArrayList<Viaje> buscarViajesCalificacionViajero(@Param("fechaActual") LocalDate fechaActual, 
                                                                     @Param("idUsuario") Long idUsuario);
    
    @Query(value = "SELECT DISTINCT v.* FROM Viaje v " +
                   "JOIN viaje_reserva vr ON v.id_viaje = vr.viaje_id_viaje " +
                   "JOIN Reserva r ON r.id_reserva = vr.reserva_id_reserva " +
                   "WHERE v.fecha < :fechaActual " +
                   "AND r.estado = 'CONFIRMADO' " +
                   "AND r.calificacion_conductor_id_calificacion is NULL " +
                   "AND r.usuario_conductor_id_usuario = :idUsuario" , nativeQuery = true)
    ArrayList<Viaje> buscarViajesCalificacionConductor(@Param("fechaActual") LocalDate fechaActual, 
                                                                     @Param("idUsuario") Long idUsuario);
    
    
}
