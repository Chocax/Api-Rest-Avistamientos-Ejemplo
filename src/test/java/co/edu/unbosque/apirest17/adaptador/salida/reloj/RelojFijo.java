package co.edu.unbosque.apirest17.adaptador.salida.reloj;

import co.edu.unbosque.apirest17.aplicacion.puerto.salida.Reloj;

import java.time.LocalDate;

// Adaptador de prueba (stub) del puerto Reloj: siempre responde la misma fecha, para que "hoy" y "mañana" no dependan del día en que se corre la prueba
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
