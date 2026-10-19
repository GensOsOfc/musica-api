package com.musica.api.modelo;

/**
 * Representa una canción del catálogo.
 * Es la "entidad" que la API devuelve al cliente serializada como JSON.
 *
 * El id lo asigna el servidor (servicio), por eso no se recibe desde
 * afuera: los datos que el cliente sí envía van en {@code CancionDTO}.
 */
public class Cancion {

    private int id;
    private String titulo;
    private String artista;
    private String genero;
    private int anio;
    private int duracionSegundos;

    public Cancion(int id, String titulo, String artista, String genero,
                   int anio, int duracionSegundos) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.genero = genero;
        this.anio = anio;
        this.duracionSegundos = duracionSegundos;
    }

    // Getters: Jackson los usa para convertir la entidad a JSON.
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public String getGenero() {
        return genero;
    }

    public int getAnio() {
        return anio;
    }

    public int getDuracionSegundos() {
        return duracionSegundos;
    }
}
