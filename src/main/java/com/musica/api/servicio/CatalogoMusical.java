package com.musica.api.servicio;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.musica.api.dto.CancionDTO;
import com.musica.api.modelo.Cancion;

/**
 * Lógica de negocio del catálogo musical.
 *
 * Los datos se guardan en memoria (List). Para esta actividad es
 * suficiente: el objetivo es demostrar el flujo HTTP/JSON, no la
 * persistencia. Cambiar a una base de datos implicaría solo
 * reemplazar esta clase por un repositorio de Spring Data.
 */
@Service
public class CatalogoMusical {

    private final List<Cancion> canciones = new ArrayList<>();
    private final AtomicInteger contadorIds = new AtomicInteger();

    public CatalogoMusical() {
        // Datos de ejemplo con los que arranca el catálogo.
        agregar(new CancionDTO("La Camisa Negra", "Juanes", "Pop rock", 2004, 214));
        agregar(new CancionDTO("Oye Mi Amor", "Maná", "Rock en español", 1992, 273));
        agregar(new CancionDTO("La Rebelión", "Joe Arroyo", "Salsa", 1986, 287));
        agregar(new CancionDTO("Matador", "Los Fabulosos Cadillacs", "Rock en español", 1993, 312));
        agregar(new CancionDTO("De Música Ligera", "Soda Stereo", "Rock en español", 1990, 226));
        agregar(new CancionDTO("Te Aviso, Te Anuncio", "Shakira", "Pop rock", 2001, 230));
        agregar(new CancionDTO("La Tierra del Olvido", "Carlos Vives", "Vallenato", 1995, 251));
        agregar(new CancionDTO("Afuera del Planeta", "Aterciopelados", "Rock alternativo", 2003, 238));
    }

    /** Devuelve todas las canciones del catálogo. */
    public List<Cancion> listarTodas() {
        return List.copyOf(canciones);
    }

    /** Busca una canción por su id. */
    public Optional<Cancion> buscarPorId(int id) {
        return canciones.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    /** Filtra las canciones por género (comparación sin distinguir mayúsculas). */
    public List<Cancion> buscarPorGenero(String genero) {
        String generoBuscado = genero.trim().toLowerCase();
        return canciones.stream()
                .filter(c -> c.getGenero().toLowerCase().contains(generoBuscado))
                .toList();
    }

    /** Lista los géneros disponibles, sin repetidos y en orden alfabético. */
    public List<String> listarGeneros() {
        return canciones.stream()
                .map(Cancion::getGenero)
                .distinct()
                .sorted()
                .toList();
    }

    /**
     * Valida un DTO antes de crear la canción.
     *
     * @return un mensaje de error si el DTO no es válido, o {@code null}
     *         si todo está bien.
     */
    public String validar(CancionDTO dto) {
        if (dto.titulo() == null || dto.titulo().isBlank()) {
            return "El campo 'titulo' es obligatorio";
        }
        if (dto.artista() == null || dto.artista().isBlank()) {
            return "El campo 'artista' es obligatorio";
        }
        if (dto.genero() == null || dto.genero().isBlank()) {
            return "El campo 'genero' es obligatorio";
        }
        if (dto.duracionSegundos() <= 0) {
            return "El campo 'duracionSegundos' debe ser mayor que 0";
        }
        if (dto.anio() < 1900 || dto.anio() > Year.now().getValue()) {
            return "El campo 'anio' debe estar entre 1900 y " + Year.now().getValue();
        }
        return null;
    }

    /**
     * Crea una canción nueva, le asigna un id autoincremental y la guarda.
     */
    public Cancion agregar(CancionDTO dto) {
        int nuevoId = contadorIds.incrementAndGet();
        Cancion cancion = new Cancion(
                nuevoId,
                dto.titulo().trim(),
                dto.artista().trim(),
                dto.genero().trim(),
                dto.anio(),
                dto.duracionSegundos()
        );
        canciones.add(cancion);
        return cancion;
    }
}
