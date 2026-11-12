package co.edu.unbosque.apirest17.aplicacion.servicio;

import co.edu.unbosque.apirest17.aplicacion.puerto.entrada.DatosAvistamiento;
import co.edu.unbosque.apirest17.aplicacion.puerto.salida.Reloj;
import co.edu.unbosque.apirest17.dominio.excepcion.AvistamientoInvalidoException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Un avistamiento es válido si: la especie tiene entre 3 y 50 caracteres, 
 * la fecha tiene el formato AAAA-MM-DD y no es posterior a hoy, y el lugar y 
 * el observador no están vacíos. Si no es válido, lanza AvistamientoInvalidoException
 * con el campo que falló (el adaptador web la traduce a un 400).
 *
 * Es Java puro: el "hoy" lo pide al puerto de salida Reloj, no a LocalDate.now().
 */
public class ValidadorDeAvistamiento {

    public static final int ESPECIE_MIN = 3;
    public static final int ESPECIE_MAX = 50;
    // Fechas validas
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private final Reloj reloj;

    public ValidadorDeAvistamiento(Reloj reloj) {
        this.reloj = reloj;
    }

    public LocalDate validar(DatosAvistamiento datos) {
        validarEspecie(datos.especie());
        LocalDate fecha = validarFecha(datos.fecha());
        validarNoVacio("lugar", datos.lugar());
        validarNoVacio("observador", datos.observador());
        return fecha;
    }

    private void validarEspecie(String especie) {
        validarNoVacio("especie", especie);
        int longitud = especie.strip().length();
        if (longitud < ESPECIE_MIN || longitud > ESPECIE_MAX) {
            throw new AvistamientoInvalidoException("especie",
                    "La especie debe tener entre " + ESPECIE_MIN + " y " + ESPECIE_MAX
                            + " caracteres (tiene " + longitud + ")");
        }
    }

    private LocalDate validarFecha(String texto) {
        validarNoVacio("fecha", texto);
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(texto, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new AvistamientoInvalidoException("fecha",
                    "La fecha debe tener el formato AAAA-MM-DD y ser una fecha real");
        }
        if (fecha.isAfter(reloj.hoy())) {
            throw new AvistamientoInvalidoException("fecha", "La fecha no puede ser posterior a hoy");
        }
        return fecha;
    }

    private void validarNoVacio(String campo, String valor) {
        if (valor == null || valor.isBlank()) {
            throw new AvistamientoInvalidoException(campo, "El campo " + campo + " no puede estar vacío");
        }
    }
}
