package co.edu.unbosque.apirest17.adaptador.entrada.web;

import co.edu.unbosque.apirest17.aplicacion.puerto.entrada.GestionarAvistamientos;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Adaptador de entrada (lado que conduce): recibe HTTP/JSON, lo traduce a una llamada al
 * puerto GestionarAvistamientos y traduce el resultado de vuelta a HTTP/JSON. No valida ni
 * guarda: eso es de la aplicación.
 */
@RestController
@RequestMapping("/avistamientos")
@RequiredArgsConstructor
@Tag(name = "Avistamientos", description = "API para la gestión de registro de avistamientos de aves")
public class AvistamientoController {

    private final GestionarAvistamientos avistamientos;

    @Operation(summary = "Obtener todos los avistamientos registrados")
    @GetMapping
    public ResponseEntity<List<AvistamientoResponse>> listarTodos() {
        return ResponseEntity.ok(avistamientos.listarTodos().stream()
                .map(AvistamientoResponse::desde)
                .toList());
    }

    @Operation(summary = "Obtener un avistamiento por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<AvistamientoResponse> obtenerPorId(@PathVariable Long id) {
        return avistamientos.buscarPorId(id)
                .map(AvistamientoResponse::desde)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Obtener un resumen con la cantidad de avistamientos por especie")
    @GetMapping("/resumen")
    public ResponseEntity<List<Object[]>> resumenPorEspecie() {
        // Mismo JSON que antes del refactor: una lista de pares [especie, cantidad]
        List<Object[]> resumen = avistamientos.resumenPorEspecie().stream()
                .map(r -> new Object[]{r.especie(), r.cantidad()})
                .toList();
        return ResponseEntity.ok(resumen);
    }

    @Operation(summary = "Registrar un nuevo avistamiento")
    @PostMapping
    public ResponseEntity<AvistamientoResponse> crear(@RequestBody AvistamientoRequest nuevo) {
        AvistamientoResponse guardado = AvistamientoResponse.desde(avistamientos.crear(nuevo.aDatos()));
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @Operation(summary = "Actualizar un avistamiento existente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<AvistamientoResponse> actualizar(@PathVariable Long id,
                                                           @RequestBody AvistamientoRequest datosNuevos) {
        return avistamientos.actualizar(id, datosNuevos.aDatos())
                .map(AvistamientoResponse::desde)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Eliminar un avistamiento por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (avistamientos.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
