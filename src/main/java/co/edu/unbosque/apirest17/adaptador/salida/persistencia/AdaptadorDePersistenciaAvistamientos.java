package co.edu.unbosque.apirest17.adaptador.salida.persistencia;

import co.edu.unbosque.apirest17.aplicacion.puerto.salida.RepositorioDeAvistamientos;
import co.edu.unbosque.apirest17.dominio.modelo.Avistamiento;
import co.edu.unbosque.apirest17.dominio.modelo.ResumenPorEspecie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida (lado conducido): implementa el puerto RepositorioDeAvistamientos
 * con JPA y H2. Traduce entre el modelo de dominio y la entidad JPA, para que el formato
 * de la base de datos no cruce hacia el centro del hexágono.
 */
@Component
@RequiredArgsConstructor
public class AdaptadorDePersistenciaAvistamientos implements RepositorioDeAvistamientos {

    private final AvistamientoJpaRepository jpa;

    @Override
    public List<Avistamiento> listarTodos() {
        return jpa.findAll().stream().map(this::aDominio).toList();
    }

    @Override
    public Optional<Avistamiento> buscarPorId(Long id) {
        return jpa.findById(id).map(this::aDominio);
    }

    @Override
    public Avistamiento guardar(Avistamiento avistamiento) {
        return aDominio(jpa.save(aEntidad(avistamiento)));
    }

    @Override
    public boolean existePorId(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public void eliminarPorId(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public List<ResumenPorEspecie> contarPorEspecie() {
        // La consulta devuelve filas [especie, cantidad]; aquí se convierten a un objeto del dominio
        return jpa.contarAvistamientosPorEspecie().stream()
                .map(fila -> new ResumenPorEspecie((String) fila[0], ((Number) fila[1]).longValue()))
                .toList();
    }

    private Avistamiento aDominio(AvistamientoEntity e) {
        return new Avistamiento(e.getId(), e.getEspecie(), e.getLugar(), e.getFecha(), e.getObservador());
    }

    private AvistamientoEntity aEntidad(Avistamiento a) {
        AvistamientoEntity e = new AvistamientoEntity();
        e.setId(a.getId());
        e.setEspecie(a.getEspecie());
        e.setLugar(a.getLugar());
        e.setFecha(a.getFecha());
        e.setObservador(a.getObservador());
        return e;
    }
}
