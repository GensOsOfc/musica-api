# MusicaAPI 🎵

API REST básica de un catálogo musical, desarrollada con **Java** y **Spring Boot** como parte de la actividad colaborativa de la Unidad 1.

La API permite listar canciones, consultar una canción por su id, filtrar por género, ver los géneros disponibles y agregar canciones nuevas. Los datos se guardan en memoria, por lo que no requiere ninguna base de datos.

## Tecnologías

- Java 17+ (desarrollado con Java 22)
- Spring Boot 3.4 (Spring Web, Tomcat embebido)
- Maven
- Jackson (serialización/deserialización JSON)

## Estructura del proyecto

```
musica-api/
├── pom.xml
└── src
    ├── main
    │   └── java
    │       └── com
    │           └── musica
    │               └── api
    │                   ├── MusicaApiApplication.java   # punto de entrada
    │                   ├── controlador
    │                   │   └── CancionController.java  # endpoints HTTP
    │                   ├── dto
    │                   │   └── CancionDTO.java         # datos de entrada (record)
    │                   ├── modelo
    │                   │   └── Cancion.java            # entidad del dominio
    │                   └── servicio
    │                       └── CatalogoMusical.java    # lógica de negocio
    └── test
        └── java
            └── com
                └── musica
                    └── api
                        └── MusicaApiApplicationTests.java
```

## Cómo ejecutar

Requisitos: JDK 17 o superior y Maven instalados.

```bash
mvn spring-boot:run
```

La aplicación queda escuchando en `http://localhost:8080`.

Para ejecutar las pruebas:

```bash
mvn test
```

## Endpoints

| Método | URL                        | Descripción                                    | Respuestas |
|--------|----------------------------|------------------------------------------------|------------|
| GET    | `/api/canciones`           | Lista todas las canciones                      | 200 OK     |
| GET    | `/api/canciones/generos`   | Géneros disponibles sin repetir                | 200 OK     |
| GET    | `/api/canciones/{id}`      | Canción por id (PathVariable)                  | 200 OK / 404 NOT FOUND |
| GET    | `/api/canciones/buscar?genero=rock` | Canciones filtradas por género (RequestParam) | 200 OK / 400 BAD REQUEST |
| POST   | `/api/canciones`           | Crea una canción (JSON en el cuerpo, RequestBody + DTO) | 201 CREATED / 400 BAD REQUEST |

## Ejemplos de uso

Con `curl`:

```bash
# Listar todas
curl http://localhost:8080/api/canciones

# Una canción por id
curl http://localhost:8080/api/canciones/3

# Filtrar por género (el texto va codificado o entre comillas)
curl "http://localhost:8080/api/canciones/buscar?genero=Rock%20en%20español"

# Crear una canción
curl -X POST http://localhost:8080/api/canciones \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Mi Canción","artista":"Yo","genero":"Pop","anio":2026,"duracionSegundos":200}'
```

Con PowerShell (`Invoke-RestMethod`):

```powershell
Invoke-RestMethod http://localhost:8080/api/canciones
Invoke-RestMethod http://localhost:8080/api/canciones/3
Invoke-RestMethod "http://localhost:8080/api/canciones/buscar?genero=Salsa"

$body = '{"titulo":"Mi Canción","artista":"Yo","genero":"Pop","anio":2026,"duracionSegundos":200}'
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/canciones `
  -ContentType "application/json" -Body $body
```

## Decisiones de diseño

- **DTO de entrada con `record`**: el cliente nunca envía el `id`; ese lo asigna el servidor con un contador autoincremental. El `record` hace el DTO inmutable y conciso.
- **`ResponseEntity` en todos los métodos**: permite elegir el código HTTP correcto para cada situación (200 para lecturas, 201 con cabecera `Location` al crear, 400 para peticiones inválidas y 404 cuando el recurso no existe).
- **Almacenamiento en memoria**: suficiente para demostrar el flujo HTTP/JSON sin agregar complejidad de base de datos. La capa de servicio está separada del controlador, así que migrar a Spring Data + JPA solo implica cambiar `CatalogoMusical`.
- **Validación manual en el servicio**: sin dependencias extra, devuelve 400 con un mensaje claro en el JSON de error.

## Uso de inteligencia artificial

Se utilizó inteligencia artificial generativa como apoyo para estructurar el proyecto, revisar errores y redactar parte de la documentación. El código final fue revisado, adaptado y probado por el/los estudiante(s).
