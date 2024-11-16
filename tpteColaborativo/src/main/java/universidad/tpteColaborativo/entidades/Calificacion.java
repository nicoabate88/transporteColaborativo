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
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCalificacion;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private Integer puntuacion;
    private String comentario;
    private String tipo;
    @OneToOne
    private Usuario usuarioConductor;
    @OneToOne
    private Usuario usuarioViajero;

    public Calificacion() {
    }

    public Calificacion(Long idCalificacion, Date fecha, Integer puntuacion, String comentario, String tipo, Usuario usuarioConductor, Usuario usuarioViajero) {
        this.idCalificacion = idCalificacion;
        this.fecha = fecha;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.tipo = tipo;
        this.usuarioConductor = usuarioConductor;
        this.usuarioViajero = usuarioViajero;
    }

    public Long getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(Long idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuarioConductor() {
        return usuarioConductor;
    }

    public void setUsuarioConductor(Usuario usuarioConductor) {
        this.usuarioConductor = usuarioConductor;
    }

    public Usuario getUsuarioViajero() {
        return usuarioViajero;
    }

    public void setUsuarioViajero(Usuario usuarioViajero) {
        this.usuarioViajero = usuarioViajero;
    }

}
