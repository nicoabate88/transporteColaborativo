
package universidad.tpteColaborativo.servicios;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import universidad.tpteColaborativo.entidades.Coordinacion;
import universidad.tpteColaborativo.entidades.Usuario;
import universidad.tpteColaborativo.repositorios.CoordinacionRepositorio;

@Service
public class CoordinacionServicio {
    
    @Autowired
    private CoordinacionRepositorio coordinacionRepositorio;
    @Autowired
    private ViajeServicio viajeServicio;
    
    @Transactional
    public void registrarCoordinacion(Long idViaje, String mensaje, Usuario usuario){
        
        Coordinacion coordinar = new Coordinacion();
        
        coordinar.setFecha(new Date());
        coordinar.setMensaje(mensaje);
        coordinar.setUsuario(usuario);
        
        coordinacionRepositorio.save(coordinar);
        
        viajeServicio.coordinarViaje(idViaje, coordinar);
        
    }
    
    public List<Coordinacion> buscarCoordinacionConductor(Long idViaje){
        
        return coordinacionRepositorio.findCoordinacionByViajeId(idViaje);
        
    }
    
}
