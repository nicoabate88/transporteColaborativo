
package universidad.tpteColaborativo.entidades;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Viaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idViaje;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private String origen;
    private String destino;
    private Integer asientoDisponible;
    private String detalle;
    private String estado;
    @OneToOne
    private Usuario usuarioConductor;
    @OneToMany
    private List<Reserva> reserva;
    @OneToMany
    private List<Coordinacion> coordinacion;

    public Viaje() {
    }

    public Viaje(Long idViaje, Date fecha, String origen, String destino, Integer asientoDisponible, String detalle, String estado, Usuario usuarioConductor, List<Reserva> reserva, List<Coordinacion> coordinacion) {
        this.idViaje = idViaje;
        this.fecha = fecha;
        this.origen = origen;
        this.destino = destino;
        this.asientoDisponible = asientoDisponible;
        this.detalle = detalle;
        this.estado = estado;
        this.usuarioConductor = usuarioConductor;
        this.reserva = reserva;
        this.coordinacion = coordinacion;
    }

    public Long getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Long idViaje) {
        this.idViaje = idViaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Integer getAsientoDisponible() {
        return asientoDisponible;
    }

    public void setAsientoDisponible(Integer asientoDisponible) {
        this.asientoDisponible = asientoDisponible;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuarioConductor() {
        return usuarioConductor;
    }

    public void setUsuarioConductor(Usuario usuarioConductor) {
        this.usuarioConductor = usuarioConductor;
    }

    public List<Reserva> getReserva() {
        return reserva;
    }

    public void setReserva(List<Reserva> reserva) {
        this.reserva = reserva;
    }

    public List<Coordinacion> getCoordinacion() {
        return coordinacion;
    }

    public void setCoordinacion(List<Coordinacion> coordinacion) {
        this.coordinacion = coordinacion;
    }
    
    
    
    
    
    
}
