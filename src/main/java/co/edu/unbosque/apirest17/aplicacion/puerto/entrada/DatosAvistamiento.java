package co.edu.unbosque.apirest17.aplicacion.puerto.entrada;

/**
 * Datos que recibe el puerto de entrada para crear o actualizar un avistamiento.
 * La fecha llega como texto para que la aplicación decida si el formato AAAA-MM-DD es correcto.
 * Es un tipo de la aplicación, no del adaptador web: así la aplicación no depende del JSON.
 */
public record DatosAvistamiento(String especie, String lugar, String fecha, String observador) {
}
