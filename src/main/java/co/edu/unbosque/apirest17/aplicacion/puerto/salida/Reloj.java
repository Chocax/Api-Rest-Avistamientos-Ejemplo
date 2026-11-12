package co.edu.unbosque.apirest17.aplicacion.puerto.salida;

import java.time.LocalDate;

/**
 * Puerto de salida (lado conducido): la aplicación necesita saber qué día es "hoy",
 * pero no le importa de dónde sale ese dato. En producción lo responde el reloj del
 * sistema; en las pruebas, un reloj fijo.
 */
public interface Reloj {

    LocalDate hoy();
}
