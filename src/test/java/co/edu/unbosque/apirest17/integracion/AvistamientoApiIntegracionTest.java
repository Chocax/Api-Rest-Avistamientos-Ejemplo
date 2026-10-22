package co.edu.unbosque.apirest17.integracion;

import co.edu.unbosque.apirest17.model.Avistamiento;
import co.edu.unbosque.apirest17.repository.AvistamientoRepository;
import co.edu.unbosque.apirest17.validacion.Reloj;
import co.edu.unbosque.apirest17.validacion.RelojFijo;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración con MockMvc
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(AvistamientoApiIntegracionTest.RelojDePrueba.class)
class AvistamientoApiIntegracionTest {

    static final LocalDate HOY = LocalDate.of(2026, 10, 21);

    @TestConfiguration
    static class RelojDePrueba {
        @Bean
        @Primary
        Reloj relojFijo() {
            return new RelojFijo(HOY);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AvistamientoRepository repository;

    // Cada prueba arranca con la base vacía: no depende del orden de ejecución
    @BeforeEach
    void limpiarBaseDeDatos() {
        repository.deleteAll();
    }

    private Avistamiento guardar(String especie, String lugar, String fecha, String observador) {
        Avistamiento a = new Avistamiento();
        a.setEspecie(especie);
        a.setLugar(lugar);
        a.setFecha(LocalDate.parse(fecha));
        a.setObservador(observador);
        return repository.save(a);
    }

    private String json(String especie, String lugar, String fecha, String observador) {
        return """
                {"especie": "%s", "lugar": "%s", "fecha": "%s", "observador": "%s"}
                """.formatted(especie, lugar, fecha, observador);
    }

    // POST de avistamientos

    @Test
    void post_conDatosValidos_responde201YElNuevoIdSePuedeConsultar() throws Exception {
        // Arrange
        String cuerpo = json("Mirla", "Usaquén", "2026-10-03", "Santiago");

        // Act
        String respuesta = mockMvc.perform(post("/avistamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.especie").value("Mirla"))
                .andExpect(jsonPath("$.fecha").value("2026-10-03"))
                .andReturn().getResponse().getContentAsString();

        // Assert: el efecto, un GET al nuevo id lo devuelve
        Integer id = JsonPath.read(respuesta, "$.id");
        mockMvc.perform(get("/avistamientos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especie").value("Mirla"))
                .andExpect(jsonPath("$.lugar").value("Usaquén"))
                .andExpect(jsonPath("$.observador").value("Santiago"));
    }

    @Test
    void post_conEspecieDe2Caracteres_responde400IndicandoElCampoYNoGuarda() throws Exception {
        // Arrange
        String cuerpo = json("Ab", "Usaquén", "2026-10-03", "Santiago");

        // Act + Assert
        mockMvc.perform(post("/avistamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campo").value("especie"))
                .andExpect(jsonPath("$.mensaje").value(containsString("entre 3 y 50")));

        assertEquals(0, repository.count());
    }

    @Test
    void post_conFechaDeManana_responde400PorFecha() throws Exception {
        // Arrange
        String cuerpo = json("Mirla", "Usaquén", "2026-10-22", "Santiago");

        // Act + Assert
        mockMvc.perform(post("/avistamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campo").value("fecha"));

        assertEquals(0, repository.count());
    }

    @Test
    void post_sinEspecieNiFecha_responde400() throws Exception {
        // Arrange: cuerpo incompleto, como el que mandaría un cliente con un error
        String cuerpo = """
                {"lugar": "Cerros Orientales"}
                """;

        // Act + Assert
        mockMvc.perform(post("/avistamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campo").value("especie"));
    }

    // ---------- GET /avistamientos ----------

    @Test
    void getTodos_conDosRegistros_devuelveAmbos() throws Exception {
        // Arrange
        guardar("Mirla", "Usaquén", "2026-10-03", "Santiago");
        guardar("Colibrí", "Cerros Orientales", "2026-10-04", "Gaby");

        // Act + Assert
        mockMvc.perform(get("/avistamientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].especie", containsInAnyOrder("Mirla", "Colibrí")));
    }

    @Test
    void getTodos_sinRegistros_devuelveListaVacia() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/avistamientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ---------- GET /avistamientos/{id} ----------

    @Test
    void getPorId_existente_devuelveTodosSusCampos() throws Exception {
        // Arrange
        Avistamiento guardado = guardar("Mirla", "Usaquén", "2026-10-03", "Santiago");

        // Act + Assert
        mockMvc.perform(get("/avistamientos/{id}", guardado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(guardado.getId()))
                .andExpect(jsonPath("$.especie").value("Mirla"))
                .andExpect(jsonPath("$.lugar").value("Usaquén"))
                .andExpect(jsonPath("$.fecha").value("2026-10-03"))
                .andExpect(jsonPath("$.observador").value("Santiago"));
    }

    @Test
    void getPorId_inexistente_responde404() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/avistamientos/{id}", 99999))
                .andExpect(status().isNotFound());
    }

    // ---------- GET /avistamientos/resumen ----------

    @Test
    void getResumen_agrupaYCuentaPorEspecie() throws Exception {
        // Arrange
        guardar("Mirla", "Usaquén", "2026-10-03", "Santiago");
        guardar("Mirla", "Suba", "2026-10-04", "Gaby");
        guardar("Colibrí", "Cerros Orientales", "2026-10-04", "Gaby");

        // Act + Assert: cada elemento viene como [especie, cantidad]
        mockMvc.perform(get("/avistamientos/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@[0] == 'Mirla')][1]", hasItem(2)))
                .andExpect(jsonPath("$[?(@[0] == 'Colibrí')][1]", hasItem(1)));
    }

    // ---------- PUT /avistamientos/{id} ----------

    @Test
    void put_conDatosValidos_responde200YQuedaGuardado() throws Exception {
        // Arrange
        Avistamiento existente = guardar("Mirla", "Usaquén", "2026-10-03", "Santiago");
        String cuerpo = json("Colibrí", "Cerros Orientales", "2026-10-10", "Gaby");

        // Act
        mockMvc.perform(put("/avistamientos/{id}", existente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existente.getId()))
                .andExpect(jsonPath("$.especie").value("Colibrí"))
                .andExpect(jsonPath("$.observador").value("Gaby"));

        // Assert: el efecto, un GET devuelve los datos nuevos
        mockMvc.perform(get("/avistamientos/{id}", existente.getId()))
                .andExpect(jsonPath("$.especie").value("Colibrí"))
                .andExpect(jsonPath("$.fecha").value("2026-10-10"));
    }

    @Test
    void put_conLugarVacio_responde400YNoModificaElRegistro() throws Exception {
        // Arrange
        Avistamiento existente = guardar("Mirla", "Usaquén", "2026-10-03", "Santiago");
        String cuerpo = json("Colibrí", "", "2026-10-10", "Gaby");

        // Act
        mockMvc.perform(put("/avistamientos/{id}", existente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campo").value("lugar"));

        // Assert
        assertEquals("Mirla", repository.findById(existente.getId()).orElseThrow().getEspecie());
    }

    @Test
    void put_conIdInexistente_responde404() throws Exception {
        // Arrange
        String cuerpo = json("Colibrí", "Cerros Orientales", "2026-10-10", "Gaby");

        // Act + Assert
        mockMvc.perform(put("/avistamientos/{id}", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isNotFound());
    }

    // ---------- DELETE /avistamientos/{id} ----------

    @Test
    void delete_existente_responde204YLuegoGetResponde404() throws Exception {
        // Arrange
        Avistamiento existente = guardar("Mirla", "Usaquén", "2026-10-03", "Santiago");

        // Act
        mockMvc.perform(delete("/avistamientos/{id}", existente.getId()))
                .andExpect(status().isNoContent());

        // Assert
        mockMvc.perform(get("/avistamientos/{id}", existente.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_inexistente_responde404() throws Exception {
        // Act + Assert
        mockMvc.perform(delete("/avistamientos/{id}", 99999))
                .andExpect(status().isNotFound());
    }
}
