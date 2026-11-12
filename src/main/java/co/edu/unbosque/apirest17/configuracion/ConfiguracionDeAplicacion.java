package co.edu.unbosque.apirest17.configuracion;

import co.edu.unbosque.apirest17.aplicacion.puerto.entrada.GestionarAvistamientos;
import co.edu.unbosque.apirest17.aplicacion.puerto.salida.Reloj;
import co.edu.unbosque.apirest17.aplicacion.puerto.salida.RepositorioDeAvistamientos;
import co.edu.unbosque.apirest17.aplicacion.servicio.AvistamientoService;
import co.edu.unbosque.apirest17.aplicacion.servicio.ValidadorDeAvistamiento;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configurador: conecta los adaptadores con los puertos al arrancar. Es el único lugar
 * que conoce a la vez el framework y las clases de la aplicación, y está afuera del hexágono.
 * Spring inyecta aquí los adaptadores (RelojDelSistema, AdaptadorDePersistenciaAvistamientos)
 * como implementaciones de los puertos de salida.
 */
@Configuration
public class ConfiguracionDeAplicacion {

    @Bean
    public ValidadorDeAvistamiento validadorDeAvistamiento(Reloj reloj) {
        return new ValidadorDeAvistamiento(reloj);
    }

    @Bean
    public GestionarAvistamientos gestionarAvistamientos(RepositorioDeAvistamientos repositorio,
                                                         ValidadorDeAvistamiento validador) {
        return new AvistamientoService(repositorio, validador);
    }
}
