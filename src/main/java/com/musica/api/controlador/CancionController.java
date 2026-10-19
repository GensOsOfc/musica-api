package com.musica.api.controlador;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.musica.api.dto.CancionDTO;
import com.musica.api.modelo.Cancion;
import com.musica.api.servicio.CatalogoMusical;

/**
 * Controlador REST del recurso "canciones".
 *
 * Todas las rutas parten de /api/canciones.
 * Cada método devuelve un ResponseEntity para controlar de forma
 * explícita el código de estado HTTP de la respuesta.
 */
@RestController
@RequestMapping("/api/canciones")
public class CancionController {

    private final CatalogoMusical catalogo;

    // Inyección por constructor: Spring entrega la instancia del servicio.
    public CancionController(CatalogoMusical catalogo) {
        this.catalogo = catalogo;
    }

    /**
     * GET /api/canciones
     * Lista todas las canciones. Respuesta: 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<Cancion>> listar() {
        return ResponseEntity.ok(catalogo.listarTodas());
    }

    /**
     * GET /api/canciones/generos
     * Géneros disponibles. Respuesta: 200 OK.
     */
    @GetMapping("/generos")
    public ResponseEntity<List<String>> listarGeneros() {
        return ResponseEntity.ok(catalogo.listarGeneros());
    }

    /**
     * GET /api/canciones/buscar?genero=rock
     * Filtra por género usando un RequestParam (parámetro de consulta).
     * Respuesta: 200 OK con el resultado, o 400 si falta el parámetro.
     */
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorGenero(@RequestParam(required = false) String genero) {
        if (genero == null || genero.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El parámetro 'genero' es obligatorio. Ejemplo: /api/canciones/buscar?genero=rock"));
        }
        return ResponseEntity.ok(catalogo.buscarPorGenero(genero));
    }

    /**
     * GET /api/canciones/{id}
     * Busca una canción por su id usando un PathVariable (parte de la URL).
     * Respuesta: 200 OK si existe, 404 NOT FOUND si no.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        Optional<Cancion> cancion = catalogo.buscarPorId(id);
        if (cancion.isPresent()) {
            return ResponseEntity.ok(cancion.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "No existe una canción con id " + id));
    }

    /**
     * POST /api/canciones
     * Crea una canción. El JSON del cuerpo se deserializa automáticamente
     * a un CancionDTO (RequestBody).
     * Respuesta: 201 CREATED con la canción creada y la cabecera Location
     * apuntando al recurso nuevo, o 400 BAD REQUEST si el JSON es inválido.
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CancionDTO dto) {
        String error = catalogo.validar(dto);
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        Cancion creada = catalogo.agregar(dto);
        URI location = URI.create("/api/canciones/" + creada.getId());
        return ResponseEntity.created(location).body(creada);
    }
}
