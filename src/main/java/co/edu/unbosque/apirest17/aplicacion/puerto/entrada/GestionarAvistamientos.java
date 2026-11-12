package co.edu.unbosque.apirest17.aplicacion.puerto.entrada;

import co.edu.unbosque.apirest17.dominio.modelo.Avistamiento;
import co.edu.unbosque.apirest17.dominio.modelo.ResumenPorEspecie;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada (lado que conduce): las operaciones que la aplicación ofrece a quien
 * la quiera usar. Hoy la conduce el controlador REST; mañana podría ser una prueba,
 * un script por lotes o una consola, sin cambiar la aplicación.
 */
public interface GestionarAvistamientos {

    List<Avistamiento> listarTodos();

    Optional<Avistamiento> buscarPorId(Long id);

    List<ResumenPorEspecie> resumenPorEspecie();

    Avistamiento crear(DatosAvistamiento datos);

    Optional<Avistamiento> actualizar(Long id, DatosAvistamiento datos);

    boolean eliminar(Long id);
}
