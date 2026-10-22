package co.edu.unbosque.apirest17.validacion;

import java.time.LocalDate;

// Abstracción del reloj del sistema: permite fijar "hoy" en las pruebas
public interface Reloj {

    LocalDate hoy();
}
