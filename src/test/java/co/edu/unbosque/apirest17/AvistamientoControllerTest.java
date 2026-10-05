package co.edu.unbosque.apirest17;

import co.edu.unbosque.apirest17.model.Avistamiento;
import co.edu.unbosque.apirest17.repository.AvistamientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AvistamientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AvistamientoRepository repository;

    // Limpiar bd
    @BeforeEach
    void limpiarBaseDeDatos() {
        repository.deleteAll();
    }

    //
    private Avistamiento guardar(String especie, String lugar, String fecha, String observador) {
        Avistamiento a = new Avistamiento();
        a.setEspecie(especie);
        a.setLugar(lugar);
        a.setFecha(LocalDate.parse(fecha));
        a.setObservador(observador);
        return repository.save(a);
    }

    // posts

    @Test
    void deberiaCrearUnAvistamiento() throws Exception {
        String nuevoAvistamientoJson = """
                {
                    "especie": "Mirla",
                    "lugar": "Usaquén",
                    "fecha": "2026-10-03",
                    "observador": "Santiago"
                }
                """;

        // 201
        mockMvc.perform(post("/avistamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuevoAvistamientoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.especie").value("Mirla"))
                .andExpect(jsonPath("$.observador").value("Santiago"));

        assertEquals(1, repository.count());
    }

    @Test
    void deberiaRetornar400SiFaltanDatos() throws Exception {
        String jsonInvalido = """
                {
                    "lugar": "Cerros Orientales"
                }
                """;

        // 400
        mockMvc.perform(post("/avistamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest());

        assertEquals(0, repository.count());
    }

    //gets

    @Test
    void deberiaListarAvistamientos() throws Exception {
        guardar("Mirla", "Usaquén", "2026-10-03", "Santiago");
        guardar("Colibrí", "Cerros Orientales", "2026-10-04", "Gaby");

        // 200
        mockMvc.perform(get("/avistamientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void deberiaObtenerUnAvistamientoPorId() throws Exception {
        Avistamiento guardado = guardar("Mirla", "Usaquén", "2026-10-03", "Santiago");

        mockMvc.perform(get("/avistamientos/{id}", guardado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especie").value("Mirla"))
                .andExpect(jsonPath("$.lugar").value("Usaquén"));
    }

    @Test
    void deberiaRetornar404SiElAvistamientoNoExiste() throws Exception {
        mockMvc.perform(get("/avistamientos/{id}", 99999))
                .andExpect(status().isNotFound());
    }

    @Test
    void deberiaObtenerElResumenPorEspecie() throws Exception {
        guardar("Mirla", "Usaquén", "2026-10-03", "Santiago");
        guardar("Mirla", "Suba", "2026-10-04", "Gaby");
        guardar("Colibrí", "Cerros Orientales", "2026-10-04", "Gaby");

        // Cada elemento viene como [especie, cantidad]
        mockMvc.perform(get("/avistamientos/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    //puts

    @Test
    void deberiaActualizarUnAvistamiento() throws Exception {
        Avistamiento existente = guardar("Mirla", "Usaquén", "2026-10-03", "Santiago");

        String datosNuevos = """
                {
                    "especie": "Colibrí",
                    "lugar": "Cerros Orientales",
                    "fecha": "2026-10-10",
                    "observador": "Gaby"
                }
                """;

        mockMvc.perform(put("/avistamientos/{id}", existente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(datosNuevos))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especie").value("Colibrí"))
                .andExpect(jsonPath("$.observador").value("Gaby"));

        // Verificar que realmente quedó guardado
        Avistamiento actualizado = repository.findById(existente.getId()).orElseThrow();
        assertEquals("Colibrí", actualizado.getEspecie());
    }

    @Test
    void deberiaRetornar404AlActualizarUnAvistamientoInexistente() throws Exception {
        String datosNuevos = """
                {
                    "especie": "Colibrí",
                    "lugar": "Cerros Orientales",
                    "fecha": "2026-10-10",
                    "observador": "Gaby"
                }
                """;

        mockMvc.perform(put("/avistamientos/{id}", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(datosNuevos))
                .andExpect(status().isNotFound());
    }

    //deletes

    @Test
    void deberiaEliminarUnAvistamiento() throws Exception {
        Avistamiento existente = guardar("Mirla", "Usaquén", "2026-10-03", "Santiago");

        // 204
        mockMvc.perform(delete("/avistamientos/{id}", existente.getId()))
                .andExpect(status().isNoContent());

        assertFalse(repository.existsById(existente.getId()));
    }

    @Test
    void deberiaRetornar404AlEliminarUnAvistamientoInexistente() throws Exception {
        mockMvc.perform(delete("/avistamientos/{id}", 99999))
                .andExpect(status().isNotFound());
    }
}