
package universidad.tpteColaborativo.servicios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import universidad.tpteColaborativo.entidades.Coordinacion;
import universidad.tpteColaborativo.entidades.Reserva;
import universidad.tpteColaborativo.entidades.Usuario;
import universidad.tpteColaborativo.entidades.Viaje;
import universidad.tpteColaborativo.repositorios.CoordinacionRepositorio;
import universidad.tpteColaborativo.repositorios.ViajeRepositorio;

@Service
public class ViajeServicio {

    @Autowired
    private ViajeRepositorio viajeRepositorio;
    @Autowired
    private CoordinacionRepositorio coordinacionRepositorio;

    @Transactional
    public void publicarViaje(String origen, String destino, String fecha, Integer asiento, String detalle, Usuario usuario) throws ParseException {

        Date fechaViaje = convertirFecha(fecha);

        Viaje viaje = new Viaje();

        viaje.setOrigen(origen);
        viaje.setDestino(destino);
        viaje.setAsientoDisponible(asiento);
        viaje.setDetalle(detalle);
        viaje.setFecha(fechaViaje);
        viaje.setUsuarioConductor(usuario);
        viaje.setEstado("DISPONIBLE");

        viajeRepositorio.save(viaje);

    }

    public Viaje buscarViaje(Long idViaje) {

        return viajeRepositorio.getById(idViaje);

    }

    public ArrayList<Viaje> buscarViajes(String origen, String destino, String desde, String hasta) throws ParseException {

        if (desde.isEmpty()) {
            desde = obtenerFechaActual();
        }
        if (hasta.isEmpty()) {
            hasta = obtenerUltimoDia();
        }

        LocalDate fechaDesde = LocalDate.parse(desde);
        LocalDate fechaHasta = LocalDate.parse(hasta);

        ArrayList<Viaje> lista = new ArrayList();

        if (origen.isEmpty() && destino.isEmpty()) {

            lista = viajeRepositorio.buscarTodos(fechaDesde, fechaHasta);

        } else if (origen.isEmpty()) {

            lista = viajeRepositorio.buscarDestino(fechaDesde, fechaHasta, destino);

        } else if (destino.isEmpty()) {

            lista = viajeRepositorio.buscarOrigen(fechaDesde, fechaHasta, origen);

        } else {

            lista = viajeRepositorio.buscarOrigenDestino(fechaDesde, fechaHasta, origen, destino);

        }

        return lista;

    }

    public Integer buscarCantidadReservaPendiente(Long idUsuario) {

        Integer cantidad = 0;

        LocalDate fechaActual = LocalDate.now();

        ArrayList<Viaje> lista = viajeRepositorio.findViajesConReservasPendienteDesdeFechaPorUsuario(fechaActual, idUsuario);

        for (Viaje viaje : lista) {
            for (Reserva reserva : viaje.getReserva()) {
                if (reserva.getEstado().equalsIgnoreCase("PENDIENTE")) {
                    cantidad = cantidad + 1;
                }
            }
        }

        return cantidad;

    }

    public ArrayList<Viaje> buscarViajesReservaPendiente(Long idUsuario) {

        LocalDate fechaActual = LocalDate.now();

        return viajeRepositorio.findViajesConReservasPendienteDesdeFechaPorUsuario(fechaActual, idUsuario);

    }

    public Integer buscarCantidadViajesReservaConfirmada(Long idUsuario) {

        LocalDate fechaActual = LocalDate.now();

        ArrayList<Viaje> lista = viajeRepositorio.findViajesConReservasConfirmadasDesdeFechaPorUsuario(fechaActual, idUsuario);

        return lista.size();

    }

    public ArrayList<Viaje> buscarViajesReservaConfirmado(Long idUsuario) {

        LocalDate fechaActual = LocalDate.now();

        return viajeRepositorio.findViajesConReservasConfirmadasDesdeFechaPorUsuario(fechaActual, idUsuario);

    }

    public Integer buscarCantidadViajesReservaConfirmadaViajero(Long idUsuario) {

        Integer cantidad = 0;
        List<Coordinacion> listaC1 = new ArrayList();
        List<Coordinacion> listaC2 = new ArrayList();

        LocalDate fechaActual = LocalDate.now();

        ArrayList<Viaje> lista = viajeRepositorio.findViajesConReservasConfirmadasUsuarioViajero(fechaActual, idUsuario);
        cantidad = lista.size();

        if (!lista.isEmpty()) {
            for (Viaje viaje : lista) {
                listaC1 = coordinacionRepositorio.findCoordinacionByViajeId(viaje.getIdViaje());
                if (!listaC1.isEmpty()) {  //si el viaje contiene Coordinacion
                    for (Coordinacion c : listaC1) {
                        listaC2.add(c);
                    }
                    cantidad = listaC2.size();
                }
            }
        }

        return cantidad;

    }

    public ArrayList<Viaje> buscarViajesReservaConfirmadoViajero(Long idUsuario) {

        LocalDate fechaActual = LocalDate.now();

        return viajeRepositorio.findViajesConReservasConfirmadasDesdeFechaPorUsuarioViajero(fechaActual, idUsuario);

    }

    public Integer buscarCantidadViajesEjecutadoUsuario(Long idUsuario) {

        Date fechaActual = new Date();

        return viajeRepositorio.buscarCantidadViajesEjecutadoUsuario(fechaActual, idUsuario);

    }

    public ArrayList<Viaje> buscarViajesEjecutadoUsuario(Long idUsuario) {

        LocalDate fechaActual = LocalDate.now();

        ArrayList<Viaje> viajes = viajeRepositorio.buscarViajesEjecutadoUsuario(fechaActual, idUsuario);

        Collections.reverse(viajes);

        return viajes;

    }

    public ArrayList<Viaje> buscarViajesUsuarioConductor(Long idUsuario) {

        LocalDate fechaActual = LocalDate.now();

        ArrayList<Viaje> viajes = viajeRepositorio.buscarViajesUsuarioCondctor(fechaActual, idUsuario);

        return viajes;

    }

    public ArrayList<Viaje> buscarViajesEjecutadoUsuarioViajero(Long idUsuario) {

        LocalDate fechaActual = LocalDate.now();

        ArrayList<Viaje> viajes = viajeRepositorio.buscarViajesReservasConfirmadasViajero(fechaActual, idUsuario);

        Collections.reverse(viajes);

        return viajes;

    }

    public ArrayList<Viaje> buscarViajesCalificacionViajero(Long idUsuario) {

        LocalDate fechaActual = LocalDate.now();

        return viajeRepositorio.buscarViajesCalificacionViajero(fechaActual, idUsuario);

    }

    public ArrayList<Viaje> buscarViajesCalificacionConductor(Long idUsuario) {

        LocalDate fechaActual = LocalDate.now();

        return viajeRepositorio.buscarViajesCalificacionConductor(fechaActual, idUsuario);

    }

    public Viaje buscarViajeIdReserva(Long idReserva) {

        return viajeRepositorio.findViajeByReservaId(idReserva);

    }

    public Long buscarIdViajeIdReserva(Long idReserva) {

        return viajeRepositorio.findIdViajeByReservaId(idReserva);

    }

    public Long buscarUltimoViaje() {

        return viajeRepositorio.ultimoViaje();

    }

    @Transactional
    public void reservarViaje(Long idViaje, Reserva reserva) {

        Viaje viaje = new Viaje();
        Optional<Viaje> vje = viajeRepositorio.findById(idViaje);
        if (vje.isPresent()) {
            viaje = vje.get();
        }

        Integer asientoDisponible = viaje.getAsientoDisponible() - reserva.getAsientoReservado();
        viaje.setAsientoDisponible(asientoDisponible);

        if (asientoDisponible == 0) {
            viaje.setEstado("EJECUTADO");
        } else {
            viaje.setEstado("PENDIENTE");
        }

        List<Reserva> reservas = viaje.getReserva();
        reservas.add(reserva);
        viaje.setReserva(reservas);

        viajeRepositorio.save(viaje);

    }

    @Transactional
    public void coordinarViaje(Long idViaje, Coordinacion coordinacion) {

        Viaje viaje = viajeRepositorio.getById(idViaje);

        List<Coordinacion> coordinaciones = viaje.getCoordinacion();
        coordinaciones.add(coordinacion);
        viaje.setCoordinacion(coordinaciones);

        viajeRepositorio.save(viaje);

    }

    @Transactional
    public void ultimaReservaViaje(Long idViaje) {

        Viaje viaje = viajeRepositorio.getById(idViaje);

        viaje.setEstado("EJECUTADO");

        viajeRepositorio.save(viaje);
    }

    @Transactional
    public void rechazarReservaViaje(Reserva reserva) {

        Viaje viaje = viajeRepositorio.findViajeByReservaId(reserva.getIdReserva());

        Integer asientoDisponible = viaje.getAsientoDisponible() + reserva.getAsientoReservado();

        if (viaje.getEstado().equalsIgnoreCase("EJECUTADO")) {
            viaje.setEstado("PENDIENTE");
        }

        viaje.setAsientoDisponible(asientoDisponible);

        if (viaje.getReserva() != null && viaje.getReserva().contains(reserva)) {
            viaje.getReserva().remove(reserva);
        }

        viajeRepositorio.save(viaje);

    }

    @Transactional
    public void modificar(Long idViaje, String origen, String destino, String fecha, Integer asiento, String detalle) throws ParseException {

        Viaje viaje = viajeRepositorio.getById(idViaje);
        Date fechaViaje = convertirFecha(fecha);

        viaje.setOrigen(origen);
        viaje.setDestino(destino);
        viaje.setFecha(fechaViaje);
        viaje.setAsientoDisponible(asiento);
        viaje.setDetalle(detalle);

        viajeRepositorio.save(viaje);

    }

    @Transactional
    public void eliminar(Long idViaje) {

        viajeRepositorio.deleteById(idViaje);

    }

    public Date convertirFecha(String fecha) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        return formato.parse(fecha);
    }

    public String obtenerFechaActual() {

        LocalDate now = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedToday = now.format(formatter);

        return formattedToday;

    }

    public String obtenerUltimoDia() {

        int nextYear = LocalDate.now().getYear() + 1;

        LocalDate lastDayOfNextYear = LocalDate.of(nextYear, 12, 31);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedDate = lastDayOfNextYear.format(formatter);

        return formattedDate;

    }

}
