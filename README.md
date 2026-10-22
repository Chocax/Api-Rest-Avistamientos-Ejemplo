<div align="center">

# 🐦 API REST · Registro de Avistamientos de Aves

**Ingeniería de Software 2 · Universidad El Bosque · 2026-2**
Santiago Pinzón Vásquez

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-6DB33F?logo=springboot&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-9.7-02303A?logo=gradle&logoColor=white)
![H2](https://img.shields.io/badge/BD-H2%20en%20memoria-1E90FF)
![Swagger](https://img.shields.io/badge/Docs-Swagger%20UI-85EA2D?logo=swagger&logoColor=black)
![Tests](https://img.shields.io/badge/pruebas-32%20✔-brightgreen)
![Coverage](https://img.shields.io/badge/cobertura-97%25%20instrucciones%20·%20100%25%20ramas-brightgreen)

</div>

> [!IMPORTANT]
> **📌 Entrega del 21 de octubre de 2026 (Sesión 22): Taller de pruebas unitarias y de integración.**
> El documento de justificación que pide el taller está en la carpeta [`docs/`](docs/) como archivo Markdown:
> **➡️ [docs/justificacion-pruebas.md](docs/justificacion-pruebas.md)**

---

## 📖 Descripción

API REST construida con **Java** y **Spring Boot** para registrar y gestionar avistamientos de aves en el campo, cumpliendo con los requisitos del Trabajo en clase (Sesión 17).

Usa **H2 Database** (base de datos en memoria) para el almacenamiento, intercambia información en formato **JSON** y aplica las buenas prácticas de métodos HTTP y códigos de estado.

Para el taller de la Sesión 22 se agregó una **regla de negocio**, una capa de servicio y una suite de **pruebas unitarias y de integración** con medición de **cobertura (JaCoCo)**.

---

## 📋 Contenido

- [Requisitos previos](#-requisitos-previos)
- [Instalación y ejecución](#-instalación-y-ejecución)
- [Swagger UI](#-swagger-ui)
- [Regla de negocio](#-regla-de-negocio)
- [Endpoints y ejemplos de uso (cURL)](#-endpoints-y-ejemplos-de-uso-curl)
- [Pruebas](#-pruebas)
- [Cobertura](#-cobertura-jacoco)
- [Estructura del proyecto](#-estructura-del-proyecto)

---

## ✅ Requisitos previos

- **Java 17** o superior
- **Git**

No se necesita tener Gradle instalado: el proyecto trae el **Gradle Wrapper**.

---

## 🚀 Instalación y ejecución

**1. Clonar el repositorio**

```bash
git clone <URL_DEL_REPOSITORIO>
cd <NOMBRE_DE_LA_CARPETA>
```

**2. Ejecutar el proyecto**

| Sistema | Comando |
|---|---|
| 🪟 Windows | `gradlew.bat bootRun` |
| 🐧 Linux / 🍎 Mac | `./gradlew bootRun` |

La API queda disponible en **http://localhost:14080**.

> [!TIP]
> **(Opcional) Consola de H2:** http://localhost:14080/h2-console
> JDBC URL: `jdbc:h2:mem:avesdb` · Usuario: `sa` · Sin contraseña

---

## 🧭 Swagger UI

Swagger UI está implementado mediante las dependencias de Gradle (`springdoc-openapi`) para probar los endpoints de forma más intuitiva. La misma herramienta muestra el comando cURL de cada petición.

Con el proyecto corriendo, entra a:

**👉 http://localhost:14080/swagger-ui/index.html**

---

## 📏 Regla de negocio

Un avistamiento **es válido** si:

| Campo | Condición |
|---|---|
| `especie` | Entre **3 y 50** caracteres |
| `fecha` | Formato **`AAAA-MM-DD`**, fecha real y **no posterior a hoy** |
| `lugar` | No vacío |
| `observador` | No vacío |

Si no es válido, **POST** y **PUT** responden **`400 Bad Request`** con el campo que falló:

```json
{
  "campo": "especie",
  "mensaje": "La especie debe tener entre 3 y 50 caracteres (tiene 2)"
}
```

La regla vive en su propia clase, `ValidadorDeAvistamiento`, separada del controlador, para poder probarla sin levantar la API.

---

## 🔌 Endpoints y ejemplos de uso (cURL)

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| ![POST](https://img.shields.io/badge/POST-49cc90) | `/avistamientos` | Registrar un avistamiento | `201` · `400` |
| ![GET](https://img.shields.io/badge/GET-61affe) | `/avistamientos` | Listar todos | `200` |
| ![GET](https://img.shields.io/badge/GET-61affe) | `/avistamientos/{id}` | Ver uno por ID | `200` · `404` |
| ![PUT](https://img.shields.io/badge/PUT-fca130) | `/avistamientos/{id}` | Actualizar | `200` · `400` · `404` |
| ![DELETE](https://img.shields.io/badge/DELETE-f93e3e) | `/avistamientos/{id}` | Eliminar | `204` · `404` |
| ![GET](https://img.shields.io/badge/GET-61affe) | `/avistamientos/resumen` | ⭐ Bonus: cantidad por especie | `200` |

### 1️⃣ Registrar un avistamiento (POST)

Crea un nuevo registro. Devuelve **`201 Created`** si tiene éxito, o **`400 Bad Request`** si faltan datos o no cumple la regla de negocio.

```bash
curl -X POST http://localhost:14080/avistamientos \
  -H "Content-Type: application/json" \
  -d '{
    "especie": "Colibrí",
    "lugar": "Cerros Orientales",
    "fecha": "2026-09-29",
    "observador": "Santiago"
  }'
```

### 2️⃣ Listar todos los avistamientos (GET)

Devuelve una lista en JSON con todos los avistamientos registrados. **`200 OK`**.

```bash
curl -X GET http://localhost:14080/avistamientos
```

### 3️⃣ Ver un avistamiento específico (GET)

Devuelve un avistamiento según su ID. **`200 OK`** si existe, o **`404 Not Found`** si no se encuentra.

```bash
curl -X GET http://localhost:14080/avistamientos/1
```

### 4️⃣ Actualizar un avistamiento (PUT)

Actualiza por completo un avistamiento existente. **`200 OK`** si se actualiza, **`400 Bad Request`** si los datos no cumplen la regla de negocio, o **`404 Not Found`** si el ID no existe.

```bash
curl -X PUT http://localhost:14080/avistamientos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "especie": "Cóndor de los Andes",
    "lugar": "PNN Los Nevados",
    "fecha": "2026-09-28",
    "observador": "Gabriela"
  }'
```

### 5️⃣ Eliminar un avistamiento (DELETE)

Borra un registro por su ID. **`204 No Content`** si tiene éxito, o **`404 Not Found`** si no existe.

```bash
curl -X DELETE http://localhost:14080/avistamientos/1
```

### ⭐ Punto opcional de bonus: resumen de avistamientos (GET)

Devuelve la cantidad de avistamientos agrupados por especie.

```bash
curl -X GET http://localhost:14080/avistamientos/resumen
```

Funciona llamando al método `contarAvistamientosPorEspecie()` del repositorio, que ejecuta esta consulta:

```sql
SELECT a.especie, COUNT(a) FROM Avistamiento a GROUP BY a.especie
```

---

## 🧪 Pruebas

Se usan **JUnit 5**, **Mockito** y **MockMvc** (dependencia `spring-boot-starter-test`).

| Comando | Qué hace |
|---|---|
| `./gradlew test` (Windows: `.\gradlew.bat test`) | Corre todas las pruebas y genera la cobertura al final |

**32 pruebas, todas en verde:**

| Clase (`src/test/java/...`) | Nivel | Pruebas | Qué verifica | Doble usado |
|---|---|:---:|---|---|
| `validacion/ValidadorDeAvistamientoTest` | 🔹 Unitaria | 14 | La regla de negocio con partición de equivalencia y valores límite | Stub `RelojFijo` |
| `service/AvistamientoServiceTest` | 🔹 Unitaria | 4 | El servicio y su colaboración con el repositorio | Mock del repositorio (Mockito) |
| `integracion/AvistamientoApiIntegracionTest` | 🔸 Integración | 14 | Los endpoints por HTTP: código, cuerpo y efecto (MockMvc + H2) | Stub `RelojFijo` como `@Primary` |

Reporte de resultados: `build/reports/tests/test/index.html`

> [!NOTE]
> En la entrega de la Sesión 17 había una sola clase, `AvistamientoControllerTest`, con 10 pruebas de caso de éxito y de error por endpoint. Para el taller se reorganizó en `AvistamientoApiIntegracionTest`, que ahora además verifica el cuerpo completo de la respuesta y el efecto de cada operación, y se le sumaron las pruebas unitarias.

---

## 📊 Cobertura (JaCoCo)

| Comando | Reporte |
|---|---|
| `./gradlew test jacocoTestReport` | `build/reports/jacoco/test/html/index.html` |

| Métrica | Resultado |
|---|---|
| Instrucciones | **97 %** (286 / 293) |
| Líneas | **95,6 %** (65 / 68) |
| Ramas | **100 %** (14 / 14) |

El análisis de lo que quedó sin cubrir está en el [documento de justificación](docs/justificacion-pruebas.md#6-lectura-de-la-cobertura).

---

## 🗂️ Estructura del proyecto

```
src/
├── main/java/co/edu/unbosque/apirest17/
│   ├── controller/   AvistamientoController, ManejadorDeErrores
│   ├── dto/          AvistamientoRequest
│   ├── model/        Avistamiento
│   ├── repository/   AvistamientoRepository
│   ├── service/      AvistamientoService
│   └── validacion/   ValidadorDeAvistamiento, Reloj, RelojDelSistema,
│                     AvistamientoInvalidoException
└── test/java/co/edu/unbosque/apirest17/
    ├── integracion/  AvistamientoApiIntegracionTest
    ├── service/      AvistamientoServiceTest
    └── validacion/   ValidadorDeAvistamientoTest, RelojFijo
docs/
├── justificacion-pruebas.md   ← documento del taller
└── img/                       ← capturas de pruebas y cobertura
```
