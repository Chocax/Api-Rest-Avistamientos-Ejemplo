package co.edu.unbosque.apirest17.adaptador.entrada.web;

import co.edu.unbosque.apirest17.aplicacion.puerto.entrada.DatosAvistamiento;

// Datos que llegan en el cuerpo de POST y PUT. La fecha llega como texto
// para que el validador sea quien decida si el formato AAAA-MM-DD es correcto.
public record AvistamientoRequest(String especie, String lugar, String fecha, String observador) {

    /** Traduce el JSON recibido a los datos que espera el puerto de entrada. */
    public DatosAvistamiento aDatos() {
        return new DatosAvistamiento(especie, lugar, fecha, observador);
    }
}
