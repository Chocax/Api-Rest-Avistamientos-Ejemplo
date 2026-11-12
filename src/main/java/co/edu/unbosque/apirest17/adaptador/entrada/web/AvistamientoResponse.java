package co.edu.unbosque.apirest17.adaptador.entrada.web;

import co.edu.unbosque.apirest17.dominio.modelo.Avistamiento;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * JSON que devuelve la API. Tiene los mismos campos, en el mismo orden y con la misma forma
 * (clase con Lombok @Data y constructor vacío) que la entidad que se devolvía antes del
 * refactor, para que la respuesta sea idéntica. Así el dominio no se serializa directamente.
 */
@Data
@NoArgsConstructor
public class AvistamientoResponse {

    private Long id;
    private String especie;
    private String lugar;
    private LocalDate fecha;
    private String observador;

    public static AvistamientoResponse desde(Avistamiento a) {
        AvistamientoResponse r = new AvistamientoResponse();
        r.setId(a.getId());
        r.setEspecie(a.getEspecie());
        r.setLugar(a.getLugar());
        r.setFecha(a.getFecha());
        r.setObservador(a.getObservador());
        return r;
    }
}
