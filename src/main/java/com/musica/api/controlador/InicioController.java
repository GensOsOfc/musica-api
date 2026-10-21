package com.musica.api.controlador;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de la raíz del sitio.
 *
 * Sin esto, abrir http://localhost:8080 en el navegador devuelve la
 * "Whitelabel Error Page" con un 404, porque la API solo responde bajo
 * /api/canciones. Este índice hace de punto de partida: dice qué rutas
 * existen sin tener que mirar el código.
 */
@RestController
public class InicioController {

    /** Una ruta de la API con su descripción, tal como se serializa en el JSON. */
    public record Endpoint(String metodo, String ruta, String descripcion) {
    }

    /**
     * GET /
     * Índice de la API. Respuesta: 200 OK.
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> indice() {
        // LinkedHashMap y no Map.of para que las claves salgan en el JSON
        // en este orden y no en uno arbitrario.
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("api", "Catálogo musical");
        cuerpo.put("endpoints", List.of(
                new Endpoint("GET", "/api/canciones",
                        "Lista todas las canciones"),
                new Endpoint("GET", "/api/canciones/generos",
                        "Géneros disponibles sin repetir"),
                new Endpoint("GET", "/api/canciones/{id}",
                        "Canción por id"),
                new Endpoint("GET", "/api/canciones/buscar?genero=rock",
                        "Canciones filtradas por género"),
                new Endpoint("POST", "/api/canciones",
                        "Crea una canción (JSON en el cuerpo)"),
                new Endpoint("DELETE", "/api/canciones/{id}",
                        "Elimina una canción por id")));
        return ResponseEntity.ok(cuerpo);
    }
}
