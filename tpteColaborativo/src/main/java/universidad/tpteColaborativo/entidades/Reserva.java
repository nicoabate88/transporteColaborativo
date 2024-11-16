package universidad.tpteColaborativo.entidades;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Temporal(TemporalType.DATE)
    private Date fechaViaje;
    private Integer asientoReservado;
    private String estado;
    @OneToOne
    private Usuario usuarioViajero;
    @OneToOne
    private Usuario usuarioConductor;
    @OneToOne
    private Calificacion calificacionConductor;
    @OneToOne
    private Calificacion calificacionViajero;

    public Reserva() {
    }

    public Reserva(Long idReserva, Date fecha, Date fechaViaje, Integer asientoReservado, String estado, Usuario usuarioViajero, Usuario usuarioConductor, Calificacion calificacionConductor, Calificacion calificacionViajero) {
        this.idReserva = idReserva;
        this.fecha = fecha;
        this.fechaViaje = fechaViaje;
        this.asientoReservado = asientoReservado;
        this.estado = estado;
        this.usuarioViajero = usuarioViajero;
        this.usuarioConductor = usuarioConductor;
        this.calificacionConductor = calificacionConductor;
        this.calificacionViajero = calificacionViajero;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(Date fechaViaje) {
        this.fechaViaje = fechaViaje;
    }

    public Integer getAsientoReservado() {
        return asientoReservado;
    }

    public void setAsientoReservado(Integer asientoReservado) {
        this.asientoReservado = asientoReservado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuarioViajero() {
        return usuarioViajero;
    }

    public void setUsuarioViajero(Usuario usuarioViajero) {
        this.usuarioViajero = usuarioViajero;
    }

    public Usuario getUsuarioConductor() {
        return usuarioConductor;
    }

    public void setUsuarioConductor(Usuario usuarioConductor) {
        this.usuarioConductor = usuarioConductor;
    }

    public Calificacion getCalificacionConductor() {
        return calificacionConductor;
    }

    public void setCalificacionConductor(Calificacion calificacionConductor) {
        this.calificacionConductor = calificacionConductor;
    }

    public Calificacion getCalificacionViajero() {
        return calificacionViajero;
    }

    public void setCalificacionViajero(Calificacion calificacionViajero) {
        this.calificacionViajero = calificacionViajero;
    }

}
