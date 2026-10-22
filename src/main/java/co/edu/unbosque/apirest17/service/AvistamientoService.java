package co.edu.unbosque.apirest17.service;

import co.edu.unbosque.apirest17.dto.AvistamientoRequest;
import co.edu.unbosque.apirest17.model.Avistamiento;
import co.edu.unbosque.apirest17.repository.AvistamientoRepository;
import co.edu.unbosque.apirest17.validacion.ValidadorDeAvistamiento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvistamientoService {

    private final AvistamientoRepository repository;
    private final ValidadorDeAvistamiento validador;

    public List<Avistamiento> listarTodos() {
        return repository.findAll();
    }

    public Optional<Avistamiento> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public List<Object[]> resumenPorEspecie() {
        return repository.contarAvistamientosPorEspecie();
    }

    public Avistamiento crear(AvistamientoRequest request) {
        LocalDate fecha = validador.validar(request);
        Avistamiento nuevo = new Avistamiento();
        copiarDatos(request, fecha, nuevo);
        return repository.save(nuevo);
    }

    public Optional<Avistamiento> actualizar(Long id, AvistamientoRequest request) {
        LocalDate fecha = validador.validar(request);
        return repository.findById(id).map(existente -> {
            copiarDatos(request, fecha, existente);
            return repository.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    private void copiarDatos(AvistamientoRequest request, LocalDate fecha, Avistamiento destino) {
        destino.setEspecie(request.especie().strip());
        destino.setLugar(request.lugar().strip());
        destino.setFecha(fecha);
        destino.setObservador(request.observador().strip());
    }
}
