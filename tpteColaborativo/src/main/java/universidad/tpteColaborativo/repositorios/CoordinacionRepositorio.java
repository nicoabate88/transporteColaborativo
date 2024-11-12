
package universidad.tpteColaborativo.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import universidad.tpteColaborativo.entidades.Coordinacion;

@Repository
public interface CoordinacionRepositorio extends JpaRepository<Coordinacion, Long>{
    
    @Query(value = "SELECT c.* FROM Coordinacion c " +
                   "JOIN viaje_coordinacion vc ON c.id_coordinacion = vc.coordinacion_id_coordinacion " +
                   "WHERE vc.viaje_id_viaje = :viajeId", nativeQuery = true)
    List<Coordinacion> findCoordinacionByViajeId(@Param("viajeId") Long viajeId);
    
}
