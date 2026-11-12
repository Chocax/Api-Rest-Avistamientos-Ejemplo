package co.edu.unbosque.apirest17.aplicacion.servicio;

import co.edu.unbosque.apirest17.aplicacion.puerto.entrada.DatosAvistamiento;
import co.edu.unbosque.apirest17.aplicacion.puerto.entrada.GestionarAvistamientos;
import co.edu.unbosque.apirest17.aplicacion.puerto.salida.RepositorioDeAvistamientos;
import co.edu.unbosque.apirest17.dominio.modelo.Avistamiento;
import co.edu.unbosque.apirest17.dominio.modelo.ResumenPorEspecie;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementa el puerto de entrada. Solo conoce puertos y el dominio: no hay anotaciones de
 * Spring ni imports de JPA o web. Spring lo crea desde ConfiguracionDeAplicacion.
 */
public class AvistamientoService implements GestionarAvistamientos {

    private final RepositorioDeAvistamientos repositorio;
    private final ValidadorDeAvistamiento validador;

    public AvistamientoService(RepositorioDeAvistamientos repositorio, ValidadorDeAvistamiento validador) {
        this.repositorio = repositorio;
        this.validador = validador;
    }

    @Override
    public List<Avistamiento> listarTodos() {
        return repositorio.listarTodos();
    }

    @Override
    public Optional<Avistamiento> buscarPorId(Long id) {
        return repositorio.buscarPorId(id);
    }

    @Override
    public List<ResumenPorEspecie> resumenPorEspecie() {
        return repositorio.contarPorEspecie();
    }

    @Override
    public Avistamiento crear(DatosAvistamiento datos) {
        LocalDate fecha = validador.validar(datos);
        Avistamiento nuevo = Avistamiento.nuevo(
                datos.especie().strip(), datos.lugar().strip(), fecha, datos.observador().strip());
        return repositorio.guardar(nuevo);
    }

    @Override
    public Optional<Avistamiento> actualizar(Long id, DatosAvistamiento datos) {
        LocalDate fecha = validador.validar(datos);
        return repositorio.buscarPorId(id).map(existente -> {
            existente.actualizarDatos(
                    datos.especie().strip(), datos.lugar().strip(), fecha, datos.observador().strip());
            return repositorio.guardar(existente);
        });
    }

    @Override
    public boolean eliminar(Long id) {
        if (!repositorio.existePorId(id)) {
            return false;
        }
        repositorio.eliminarPorId(id);
        return true;
    }
}
