package com.musica.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Pruebas de integración de los endpoints usando MockMvc.
 * Verifican método HTTP, código de respuesta y parte del JSON.
 */
@SpringBootTest
@AutoConfigureMockMvc
class MusicaApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getListarCancionesDevuelve200YArregloConDatos() throws Exception {
        mockMvc.perform(get("/api/canciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNumber());
    }

    @Test
    void getCancionPorIdInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/api/canciones/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBuscarPorGeneroDevuelve200YFiltra() throws Exception {
        mockMvc.perform(get("/api/canciones/buscar").param("genero", "salsa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].genero").value("Salsa"));
    }

    @Test
    void getBuscarSinParametroGeneroDevuelve400() throws Exception {
        mockMvc.perform(get("/api/canciones/buscar"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postCrearCancionDevuelve201YLocation() throws Exception {
        String json = """
                {
                    "titulo": "Canción de Prueba",
                    "artista": "Estudiante",
                    "genero": "Pop",
                    "anio": 2026,
                    "duracionSegundos": 200
                }
                """;

        mockMvc.perform(post("/api/canciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/canciones/9"))
                .andExpect(jsonPath("$.titulo").value("Canción de Prueba"));
    }

    @Test
    void postConDatosInvalidosDevuelve400ConMensaje() throws Exception {
        String json = """
                {
                    "titulo": "",
                    "artista": "Estudiante",
                    "genero": "Pop",
                    "anio": 2026,
                    "duracionSegundos": -5
                }
                """;

        mockMvc.perform(post("/api/canciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }
}
