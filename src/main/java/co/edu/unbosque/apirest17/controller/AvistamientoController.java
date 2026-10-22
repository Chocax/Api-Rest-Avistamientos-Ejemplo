package co.edu.unbosque.apirest17.controller;

import co.edu.unbosque.apirest17.dto.AvistamientoRequest;
import co.edu.unbosque.apirest17.model.Avistamiento;
import co.edu.unbosque.apirest17.service.AvistamientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avistamientos")
@RequiredArgsConstructor
@Tag(name = "Avistamientos", description = "API para la gestión de registro de avistamientos de aves")
public class AvistamientoController {

    private final AvistamientoService service;

    @Operation(summary = "Obtener todos los avistamientos registrados")
    @GetMapping
    public ResponseEntity<List<Avistamiento>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Obtener un avistamiento por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Avistamiento> obtenerPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Obtener un resumen con la cantidad de avistamientos por especie")
    @GetMapping("/resumen")
    public ResponseEntity<List<Object[]>> resumenPorEspecie() {
        return ResponseEntity.ok(service.resumenPorEspecie());
    }

    @Operation(summary = "Registrar un nuevo avistamiento")
    @PostMapping
    public ResponseEntity<Avistamiento> crear(@RequestBody AvistamientoRequest nuevo) {
        Avistamiento guardado = service.crear(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @Operation(summary = "Actualizar un avistamiento existente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<Avistamiento> actualizar(@PathVariable Long id, @RequestBody AvistamientoRequest datosNuevos) {
        return service.actualizar(id, datosNuevos)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Eliminar un avistamiento por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
