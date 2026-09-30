package co.edu.unbosque.apirest17.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data // Lombok
@NoArgsConstructor // Lombok
@Entity
public class Avistamiento {

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

