package co.edu.unbosque.apirest17.adaptador.salida.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * Repositorio de Spring Data. Solo lo usa el adaptador de persistencia: la aplicación
 * no lo conoce, conoce el puerto RepositorioDeAvistamientos.
 */
public interface AvistamientoJpaRepository extends JpaRepository<AvistamientoEntity, Long> {

    @Query("SELECT a.especie, COUNT(a) FROM Avistamiento a GROUP BY a.especie")
    List<Object[]> contarAvistamientosPorEspecie();
}
