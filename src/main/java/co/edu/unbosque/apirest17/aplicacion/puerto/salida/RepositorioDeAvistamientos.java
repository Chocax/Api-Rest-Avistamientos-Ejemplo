package co.edu.unbosque.apirest17.aplicacion.puerto.salida;

import co.edu.unbosque.apirest17.dominio.modelo.Avistamiento;
import co.edu.unbosque.apirest17.dominio.modelo.ResumenPorEspecie;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (lado conducido): lo que la aplicación necesita para guardar y
 * consultar avistamientos. Lo define la aplicación, con sus propios tipos, y lo implementa
 * un adaptador afuera (hoy JPA + H2). Así la dependencia apunta hacia adentro.
 */
public interface RepositorioDeAvistamientos {

    List<Avistamiento> listarTodos();

    Optional<Avistamiento> buscarPorId(Long id);

    Avistamiento guardar(Avistamiento avistamiento);

    boolean existePorId(Long id);

    void eliminarPorId(Long id);

    List<ResumenPorEspecie> contarPorEspecie();
}
