package co.edu.unbosque.apirest17.validacion;

import co.edu.unbosque.apirest17.dto.AvistamientoRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas unitarias de la regla de negocio
 */
class ValidadorDeAvistamientoTest {

    private static final LocalDate HOY = LocalDate.of(2026, 10, 21);

    private final ValidadorDeAvistamiento validador = new ValidadorDeAvistamiento(new RelojFijo(HOY));

    private AvistamientoRequest conEspecie(String especie) {
        return new AvistamientoRequest(especie, "Humedal La Conejera", "2026-10-01", "Santiago");
    }

    private AvistamientoRequest conFecha(String fecha) {
        return new AvistamientoRequest("Mirla patiamarilla", "Humedal La Conejera", fecha, "Santiago");
    }

    // ---------- Especie: partición [vacía] [1-2] [3-50] [51+] ----------

    @Test
    void especieDe7Caracteres_esValida() {
        // Arrange
        AvistamientoRequest request = conEspecie("Colibrí");

        // Act
        LocalDate fecha = validador.validar(request);

        // Assert
        assertEquals(LocalDate.of(2026, 10, 1), fecha);
    }

    @Test
    void especieDe2Caracteres_esRechazadaPorEspecie() {
        // Arrange
        AvistamientoRequest request = conEspecie("Ab");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("especie", e.getCampo());
    }

    @Test
    void especieDe3Caracteres_esValida() {
        // Arrange
        AvistamientoRequest request = conEspecie("Ave");

        // Act
        LocalDate fecha = validador.validar(request);

        // Assert
        assertEquals(LocalDate.of(2026, 10, 1), fecha);
    }

    @Test
    void especieDe50Caracteres_esValida() {
        // Arrange
        AvistamientoRequest request = conEspecie("a".repeat(50));

        // Act
        LocalDate fecha = validador.validar(request);

        // Assert
        assertEquals(LocalDate.of(2026, 10, 1), fecha);
    }

    @Test
    void especieDe51Caracteres_esRechazadaPorEspecie() {
        // Arrange
        AvistamientoRequest request = conEspecie("a".repeat(51));

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("especie", e.getCampo());
    }

    @Test
    void especieConEspaciosAlrededor_cuentaSoloLosCaracteresReales() {
        // Arrange: "  Ab  " son 6 caracteres pero 2 reales
        AvistamientoRequest request = conEspecie("  Ab  ");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("especie", e.getCampo());
    }

    @Test
    void especieNula_esRechazadaPorEspecie() {
        // Arrange
        AvistamientoRequest request = conEspecie(null);

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("especie", e.getCampo());
    }

    // ---------- Fecha: partición [formato inválido] [pasada o hoy] [futura] ----------

    @Test
    void fechaDeHoy_esValida() {
        // Arrange
        AvistamientoRequest request = conFecha("2026-10-21");

        // Act
        LocalDate fecha = validador.validar(request);

        // Assert
        assertEquals(HOY, fecha);
    }

    @Test
    void fechaDeManana_esRechazadaPorFecha() {
        // Arrange
        AvistamientoRequest request = conFecha("2026-10-22");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("fecha", e.getCampo());
    }

    @Test
    void fechaPasadaLejana_esValida() {
        // Arrange
        AvistamientoRequest request = conFecha("2025-03-15");

        // Act
        LocalDate fecha = validador.validar(request);

        // Assert
        assertEquals(LocalDate.of(2025, 3, 15), fecha);
    }

    @Test
    void fechaConFormatoDiaMesAnio_esRechazadaPorFecha() {
        // Arrange
        AvistamientoRequest request = conFecha("21/10/2026");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("fecha", e.getCampo());
    }

    @Test
    void fechaConFormatoCorrectoPeroInexistente_esRechazadaPorFecha() {
        // Arrange: 30 de febrero, no es valida
        AvistamientoRequest request = conFecha("2026-02-30");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("fecha", e.getCampo());
    }

    // ---------- Lugar y observador: partición [vacío] [con texto] ----------

    @Test
    void lugarSoloConEspacios_esRechazadoPorLugar() {
        // Arrange
        AvistamientoRequest request = new AvistamientoRequest("Mirla", "   ", "2026-10-01", "Santiago");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("lugar", e.getCampo());
    }

    @Test
    void observadorVacio_esRechazadoPorObservador() {
        // Arrange
        AvistamientoRequest request = new AvistamientoRequest("Mirla", "Usaquén", "2026-10-01", "");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("observador", e.getCampo());
    }
}
