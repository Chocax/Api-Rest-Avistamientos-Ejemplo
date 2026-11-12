package co.edu.unbosque.apirest17.aplicacion.servicio;

import co.edu.unbosque.apirest17.adaptador.salida.reloj.RelojFijo;
import co.edu.unbosque.apirest17.aplicacion.puerto.entrada.DatosAvistamiento;
import co.edu.unbosque.apirest17.dominio.excepcion.AvistamientoInvalidoException;
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

    private DatosAvistamiento conEspecie(String especie) {
        return new DatosAvistamiento(especie, "Humedal La Conejera", "2026-10-01", "Santiago");
    }

    private DatosAvistamiento conFecha(String fecha) {
        return new DatosAvistamiento("Mirla patiamarilla", "Humedal La Conejera", fecha, "Santiago");
    }

    // ---------- Especie: partición [vacía] [1-2] [3-50] [51+] ----------

    @Test
    void especieDe7Caracteres_esValida() {
        // Arrange
        DatosAvistamiento request = conEspecie("Colibrí");

        // Act
        LocalDate fecha = validador.validar(request);

        // Assert
        assertEquals(LocalDate.of(2026, 10, 1), fecha);
    }

    @Test
    void especieDe2Caracteres_esRechazadaPorEspecie() {
        // Arrange
        DatosAvistamiento request = conEspecie("Ab");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("especie", e.getCampo());
    }

    @Test
    void especieDe3Caracteres_esValida() {
        // Arrange
        DatosAvistamiento request = conEspecie("Ave");

        // Act
        LocalDate fecha = validador.validar(request);

        // Assert
        assertEquals(LocalDate.of(2026, 10, 1), fecha);
    }

    @Test
    void especieDe50Caracteres_esValida() {
        // Arrange
        DatosAvistamiento request = conEspecie("a".repeat(50));

        // Act
        LocalDate fecha = validador.validar(request);

        // Assert
        assertEquals(LocalDate.of(2026, 10, 1), fecha);
    }

    @Test
    void especieDe51Caracteres_esRechazadaPorEspecie() {
        // Arrange
        DatosAvistamiento request = conEspecie("a".repeat(51));

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("especie", e.getCampo());
    }

    @Test
    void especieConEspaciosAlrededor_cuentaSoloLosCaracteresReales() {
        // Arrange: "  Ab  " son 6 caracteres pero 2 reales
        DatosAvistamiento request = conEspecie("  Ab  ");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("especie", e.getCampo());
    }

    @Test
    void especieNula_esRechazadaPorEspecie() {
        // Arrange
        DatosAvistamiento request = conEspecie(null);

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
        DatosAvistamiento request = conFecha("2026-10-21");

        // Act
        LocalDate fecha = validador.validar(request);

        // Assert
        assertEquals(HOY, fecha);
    }

    @Test
    void fechaDeManana_esRechazadaPorFecha() {
        // Arrange
        DatosAvistamiento request = conFecha("2026-10-22");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("fecha", e.getCampo());
    }

    @Test
    void fechaPasadaLejana_esValida() {
        // Arrange
        DatosAvistamiento request = conFecha("2025-03-15");

        // Act
        LocalDate fecha = validador.validar(request);

        // Assert
        assertEquals(LocalDate.of(2025, 3, 15), fecha);
    }

    @Test
    void fechaConFormatoDiaMesAnio_esRechazadaPorFecha() {
        // Arrange
        DatosAvistamiento request = conFecha("21/10/2026");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("fecha", e.getCampo());
    }

    @Test
    void fechaConFormatoCorrectoPeroInexistente_esRechazadaPorFecha() {
        // Arrange: 30 de febrero, no es valida
        DatosAvistamiento request = conFecha("2026-02-30");

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
        DatosAvistamiento request = new DatosAvistamiento("Mirla", "   ", "2026-10-01", "Santiago");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("lugar", e.getCampo());
    }

    @Test
    void observadorVacio_esRechazadoPorObservador() {
        // Arrange
        DatosAvistamiento request = new DatosAvistamiento("Mirla", "Usaquén", "2026-10-01", "");

        // Act
        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        // Assert
        assertEquals("observador", e.getCampo());
    }
}
