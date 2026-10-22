package co.edu.unbosque.apirest17.validacion;

import lombok.Getter;

@Getter
public class AvistamientoInvalidoException extends RuntimeException {

    private final String campo;

    public AvistamientoInvalidoException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }
}
