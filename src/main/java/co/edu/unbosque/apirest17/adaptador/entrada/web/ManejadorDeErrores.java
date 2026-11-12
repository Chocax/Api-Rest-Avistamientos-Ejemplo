package co.edu.unbosque.apirest17.adaptador.entrada.web;

import co.edu.unbosque.apirest17.dominio.excepcion.AvistamientoInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Parte del adaptador web: traduce el error de negocio del dominio a la respuesta HTTP.
 * El dominio no sabe qué es un 400; eso lo decide este adaptador.
 */
@RestControllerAdvice
public class ManejadorDeErrores {

    // 400 con el campo que falló y el motivo
    @ExceptionHandler(AvistamientoInvalidoException.class)
    public ResponseEntity<Map<String, String>> avistamientoInvalido(AvistamientoInvalidoException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("campo", e.getCampo(), "mensaje", e.getMessage()));
    }
}
