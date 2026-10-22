package co.edu.unbosque.apirest17.dto;

// Datos que llegan en el cuerpo de POST y PUT. La fecha llega como texto
// para que el validador sea quien decida si el formato AAAA-MM-DD es correcto.
public record AvistamientoRequest(String especie, String lugar, String fecha, String observador) {
}
