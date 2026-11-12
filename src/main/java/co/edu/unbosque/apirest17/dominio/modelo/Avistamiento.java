package co.edu.unbosque.apirest17.dominio.modelo;

import java.time.LocalDate;

/**
 * Entidad del dominio: un avistamiento de un ave. Es Java puro, sin anotaciones de JPA,
 * de Spring ni de Lombok, porque el centro del hexágono no debe conocer la base de datos
 * ni el framework.
 */
public class Avistamiento {

    private final Long id;
    private String especie;
    private String lugar;
    private LocalDate fecha;
    private String observador;

    public Avistamiento(Long id, String especie, String lugar, LocalDate fecha, String observador) {
        this.id = id;
        this.especie = especie;
        this.lugar = lugar;
        this.fecha = fecha;
        this.observador = observador;
    }

    /** Crea un avistamiento que todavía no se ha guardado (sin id). */
    public static Avistamiento nuevo(String especie, String lugar, LocalDate fecha, String observador) {
        return new Avistamiento(null, especie, lugar, fecha, observador);
    }

    /** Reemplaza los datos del avistamiento, conservando su id. */
    public void actualizarDatos(String especie, String lugar, LocalDate fecha, String observador) {
        this.especie = especie;
        this.lugar = lugar;
        this.fecha = fecha;
        this.observador = observador;
    }

    public Long getId() {
        return id;
    }

    public String getEspecie() {
        return especie;
    }

    public String getLugar() {
        return lugar;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getObservador() {
        return observador;
    }
}
