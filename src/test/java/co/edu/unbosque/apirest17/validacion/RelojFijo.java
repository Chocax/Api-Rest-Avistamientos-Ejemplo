package co.edu.unbosque.apirest17.validacion;

import java.time.LocalDate;

// Stub: siempre responde la misma fecha, para que "hoy" y "mañana" no dependan del día en que se corre la prueba
public class RelojFijo implements Reloj {

    private final LocalDate hoy;

    public RelojFijo(LocalDate hoy) {
        this.hoy = hoy;
    }

    @Override
    public LocalDate hoy() {
        return hoy;
    }
}
