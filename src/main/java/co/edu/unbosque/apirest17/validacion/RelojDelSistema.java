package co.edu.unbosque.apirest17.validacion;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RelojDelSistema implements Reloj {

    @Override
    public LocalDate hoy() {
        return LocalDate.now();
    }
}
