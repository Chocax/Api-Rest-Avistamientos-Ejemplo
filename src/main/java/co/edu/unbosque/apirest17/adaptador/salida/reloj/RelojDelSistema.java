package co.edu.unbosque.apirest17.adaptador.salida.reloj;

import co.edu.unbosque.apirest17.aplicacion.puerto.salida.Reloj;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Adaptador de salida: implementa el puerto Reloj con la hora real del sistema.
 */
@Component
public class RelojDelSistema implements Reloj {

    @Override
    public LocalDate hoy() {
        return LocalDate.now();
    }
}
