package co.edu.unbosque.apirest17.dominio.modelo;

/**
 * Cantidad de avistamientos de una especie. Reemplaza al Object[] que devolvía la consulta,
 * que era un formato propio de la base de datos.
 */
public record ResumenPorEspecie(String especie, long cantidad) {
}
