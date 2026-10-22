package co.edu.unbosque.apirest17.controller;

import co.edu.unbosque.apirest17.validacion.AvistamientoInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ManejadorDeErrores {

    // 400 con el campo que falló y el motivo
    @ExceptionHandler(AvistamientoInvalidoException.class)
    public ResponseEntity<Map<String, String>> avistamientoInvalido(AvistamientoInvalidoException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("campo", e.getCampo(), "mensaje", e.getMessage()));
    }
}
