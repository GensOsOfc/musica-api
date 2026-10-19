package com.musica.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación.
 *
 * @SpringBootApplication habilita, entre otras cosas, el escaneo de
 * componentes (@RestController, @Service) y la autoconfiguración de
 * Spring Boot, incluido el servidor web embebido (Tomcat).
 */
@SpringBootApplication
public class MusicaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicaApiApplication.class, args);
    }
}
