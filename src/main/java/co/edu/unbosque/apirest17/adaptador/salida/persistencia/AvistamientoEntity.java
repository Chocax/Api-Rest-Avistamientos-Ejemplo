package co.edu.unbosque.apirest17.adaptador.salida.persistencia;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Fila de la tabla en H2. Es un detalle de la tecnología de persistencia (JPA) y por eso
 * vive en el adaptador de salida, no en el dominio. El nombre "Avistamiento" se conserva
 * para que la tabla y la consulta JPQL sigan siendo las mismas que antes del refactor.
 */
@Data // Lombok
@NoArgsConstructor // Lombok
@Entity(name = "Avistamiento")
public class AvistamientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String especie;

    @NotBlank
    private String lugar;

    @NotNull
    private LocalDate fecha;

    @NotBlank
    private String observador;
}
