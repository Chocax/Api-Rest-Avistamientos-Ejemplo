package co.edu.unbosque.apirest17.repository;

import co.edu.unbosque.apirest17.model.Avistamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long> {

    @Query("SELECT a.especie, COUNT(a) FROM Avistamiento a GROUP BY a.especie")
    List<Object[]> contarAvistamientosPorEspecie();
}