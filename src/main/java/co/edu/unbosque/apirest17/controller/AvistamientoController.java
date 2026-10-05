package co.edu.unbosque.apirest17.controller;

import co.edu.unbosque.apirest17.model.Avistamiento;
import co.edu.unbosque.apirest17.repository.AvistamientoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avistamientos")
@RequiredArgsConstructor
@Tag(name = "Avistamientos", description = "API para la gestión de registro de avistamientos de aves")
public class AvistamientoController {

    private final AvistamientoRepository repository;

    @Operation(summary = "Obtener todos los avistamientos registrados")
    @GetMapping
    public ResponseEntity<List<Avistamiento>> listarTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(summary = "Obtener un avistamiento por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Avistamiento> obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Obtener un resumen con la cantidad de avistamientos por especie")
    @GetMapping("/resumen")
    public ResponseEntity<List<Object[]>> resumenPorEspecie() {
        return ResponseEntity.ok(repository.contarAvistamientosPorEspecie());
    }

    @Operation(summary = "Registrar un nuevo avistamiento")
    @PostMapping
    public ResponseEntity<Avistamiento> crear(@Validated @RequestBody Avistamiento nuevo) {
        Avistamiento guardado = repository.save(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @Operation(summary = "Actualizar un avistamiento existente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<Avistamiento> actualizar(@PathVariable Long id, @RequestBody Avistamiento datosNuevos) {
        return repository.findById(id).map(existente -> {
            existente.setEspecie(datosNuevos.getEspecie());
            existente.setLugar(datosNuevos.getLugar());
            existente.setFecha(datosNuevos.getFecha());
            existente.setObservador(datosNuevos.getObservador());
            return ResponseEntity.ok(repository.save(existente));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Eliminar un avistamiento por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}