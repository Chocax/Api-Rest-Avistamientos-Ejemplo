package co.edu.unbosque.apirest17.controller;

import co.edu.unbosque.apirest17.model.Avistamiento;
import co.edu.unbosque.apirest17.repository.AvistamientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/avistamientos")
@RequiredArgsConstructor // Lombok
public class AvistamientoController {

    private final AvistamientoRepository repository;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Avistamiento>> listarTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    // GET ID
    @GetMapping("/{id}")
    public ResponseEntity<Avistamiento> obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // PE: Resumen
    @GetMapping("/resumen")
    public ResponseEntity<List<Object[]>> resumenPorEspecie() {
        return ResponseEntity.ok(repository.contarAvistamientosPorEspecie());
    }

    // Post registrar
    @PostMapping
    public ResponseEntity<Avistamiento> crear(@Validated @RequestBody Avistamiento nuevo) {
        Avistamiento guardado = repository.save(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // Put actualizar
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

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}