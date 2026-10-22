package co.edu.unbosque.apirest17.service;

import co.edu.unbosque.apirest17.dto.AvistamientoRequest;
import co.edu.unbosque.apirest17.model.Avistamiento;
import co.edu.unbosque.apirest17.repository.AvistamientoRepository;
import co.edu.unbosque.apirest17.validacion.AvistamientoInvalidoException;
import co.edu.unbosque.apirest17.validacion.RelojFijo;
import co.edu.unbosque.apirest17.validacion.ValidadorDeAvistamiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias del servicio. El repositorio es un mock de Mockito (no hay base de datos)
 * y el reloj es el stub RelojFijo. Lo que se verifica es la colaboración: qué le pide el
 * servicio al repositorio y, sobre todo, qué NO le pide cuando los datos son inválidos.
 */
class AvistamientoServiceTest {

    private AvistamientoRepository repository;
    private AvistamientoService service;

    @BeforeEach
    void preparar() {
        repository = mock(AvistamientoRepository.class);
        ValidadorDeAvistamiento validador = new ValidadorDeAvistamiento(new RelojFijo(LocalDate.of(2026, 10, 21)));
        service = new AvistamientoService(repository, validador);
    }

    @Test
    void crear_conDatosValidos_guardaElAvistamientoConLaFechaConvertida() {
        // Arrange
        AvistamientoRequest request = new AvistamientoRequest("  Mirla  ", "Usaquén", "2026-10-03", "Santiago");
        when(repository.save(any(Avistamiento.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        service.crear(request);

        // Assert
        ArgumentCaptor<Avistamiento> guardado = ArgumentCaptor.forClass(Avistamiento.class);
        verify(repository).save(guardado.capture());
        assertEquals("Mirla", guardado.getValue().getEspecie());
        assertEquals(LocalDate.of(2026, 10, 3), guardado.getValue().getFecha());
    }

    @Test
    void crear_conFechaFutura_noLlegaAlRepositorio() {
        // Arrange
        AvistamientoRequest request = new AvistamientoRequest("Mirla", "Usaquén", "2026-10-22", "Santiago");

        // Act
        assertThrows(AvistamientoInvalidoException.class, () -> service.crear(request));

        // Assert
        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_conIdInexistente_devuelveVacioYNoGuarda() {
        // Arrange
        AvistamientoRequest request = new AvistamientoRequest("Mirla", "Usaquén", "2026-10-03", "Santiago");
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Avistamiento> resultado = service.actualizar(99L, request);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(repository, never()).save(any());
    }

    @Test
    void eliminar_conIdInexistente_devuelveFalseYNoBorra() {
        // Arrange
        when(repository.existsById(99L)).thenReturn(false);

        // Act
        boolean eliminado = service.eliminar(99L);

        // Assert
        assertFalse(eliminado);
        verify(repository, never()).deleteById(any());
    }
}
