package universidad.tpteColaborativo.repositorios;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import universidad.tpteColaborativo.entidades.Calificacion;

@Repository
public interface CalificacionRepositorio extends JpaRepository<Calificacion, Long> {

    @Query("SELECT c FROM Calificacion c WHERE usuario_conductor_id_usuario = :id AND c.tipo = 'VIAJERO'")
    public ArrayList<Calificacion> buscarCalificacionConductor(@Param("id") Long id);

    @Query("SELECT c FROM Calificacion c WHERE usuario_viajero_id_usuario = :id AND c.tipo = 'CONDUCTOR'")
    public ArrayList<Calificacion> buscarCalificacionViajero(@Param("id") Long id);

}
