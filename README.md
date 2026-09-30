API REST - Registro de Avistamientos de Aves

Esta es una API REST construida con Java y Spring Boot para registrar y gestionar avistamientos de aves en el campo, cumpliendo con los requisitos del Trabajo en clase (Sesión 17).

Utiliza H2 Database (base de datos en memoria) para el almacenamiento de datos, intercambia información en formato JSON y aplica las mejores prácticas de métodos HTTP y códigos de estado.

Requisitos previos

Java 17 o superior.

Git.

Cómo instalar y ejecutar la API localmente

Clonar el repositorio:

git clone <URL_DEL_REPOSITORIO>
cd <NOMBRE_DE_LA_CARPETA>


Ejecutar el proyecto con Gradle:
Como el proyecto usa Gradle Wrapper, no se necesita tener Gradle instalado globalmente. Ejecuta el siguiente comando en la raíz del proyecto:

En Windows:

gradlew.bat bootRun


En Linux/Mac:

./gradlew bootRun


La API estará disponible en http://localhost:14080.

(Opcional) se puede acceder a la consola de la base de datos H2 ingresando a http://localhost:14080/h2-console (JDBC URL: jdbc:h2:mem:avesdb, User: sa, sin contraseña).

Endpoints y Ejemplos de uso (cURL)

A continuación, se listan todos los endpoints disponibles.

1. Registrar un avistamiento (POST)

Crea un nuevo registro en el sistema. Devuelve el código de estado 201 Created en caso de éxito, o 400 Bad Request si faltan datos.

curl -X POST http://localhost:14080/avistamientos \
-H "Content-Type: application/json" \
-d '{
  "especie": "Colibrí",
  "lugar": "Cerros Orientales",
  "fecha": "2026-09-29",
  "observador": "Santiago"
}'


2. Listar todos los avistamientos (GET)

Devuelve una lista en formato JSON con todos los avistamientos registrados. Código de estado 200 OK.

curl -X GET http://localhost:14080/avistamientos


3. Ver un avistamiento específico (GET)

Devuelve un avistamiento según su ID. Código 200 OK si existe, o 404 Not Found si no se encuentra.

curl -X GET http://localhost:14080/avistamientos/1


4. Actualizar un avistamiento (PUT)

Actualiza por completo un avistamiento existente. Código 200 OK si se actualiza correctamente, o 404 Not Found si el ID no existe.

curl -X PUT http://localhost:14080/avistamientos/1 \
-H "Content-Type: application/json" \
-d '{
  "especie": "Cóndor de los Andes",
  "lugar": "PNN Los Nevados",
  "fecha": "2026-09-28",
  "observador": "Gabriela"
}'


5. Eliminar un avistamiento (DELETE)

Borra un registro del sistema por su ID. Código 204 No Content al ser exitoso, o 404 Not Found si no existe.

curl -X DELETE http://localhost:14080/avistamientos/1


Punto opcional de Bonus: Resumen de avistamientos (GET)

Devuelve una lista con la cantidad de avistamientos agrupados por especie.

curl -X GET http://localhost:14080/avistamientos/resumen

Este funciona llamando al metodo contarAvistamientosPorEspecie(); en el repository que inyecta la siguiente consulta en la base de datos:
SELECT a.especie, COUNT(a) FROM Avistamiento a GROUP BY a.especie

