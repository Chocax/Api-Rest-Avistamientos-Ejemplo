package co.edu.unbosque.apirest17.dominio.excepcion;

/**
 * Error de negocio: el avistamiento no cumple la regla. Es Java puro (sin Lombok ni Spring)
 * porque vive en el centro del hexágono y no debe depender de ninguna tecnología.
 */
public class AvistamientoInvalidoException extends RuntimeException {

    private final String campo;

    public AvistamientoInvalidoException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
