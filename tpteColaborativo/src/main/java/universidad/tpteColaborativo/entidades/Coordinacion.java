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
public class Coordinacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCoordinacion;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private String mensaje;
    @OneToOne
    private Usuario usuario;

    public Coordinacion() {
    }

    public Coordinacion(Long idCoordinacion, Date fecha, String mensaje, Usuario usuario) {
        this.idCoordinacion = idCoordinacion;
        this.fecha = fecha;
        this.mensaje = mensaje;
        this.usuario = usuario;
    }

    public Long getIdCoordinacion() {
        return idCoordinacion;
    }

    public void setIdCoordinacion(Long idCoordinacion) {
        this.idCoordinacion = idCoordinacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
