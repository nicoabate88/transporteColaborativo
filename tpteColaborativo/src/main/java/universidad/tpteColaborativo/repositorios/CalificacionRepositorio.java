
package universidad.tpteColaborativo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import universidad.tpteColaborativo.entidades.Calificacion;

@Repository
public interface CalificacionRepositorio extends JpaRepository<Calificacion, Long> {
    
}
