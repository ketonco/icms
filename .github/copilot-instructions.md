# Contexto y Convenciones del Proyecto: Factura Cart Microservices

## 1. Arquitectura General y Tecnologías Core
- **Estructura:** Monorepo con Gradle Multi-Project Build (Groovy DSL).
- **Lenguaje:** Java 21 puro (sin Kotlin en el código fuente).
- **Framework Principal:** Spring Boot 3.3+.
- **Base de Datos:** PostgreSQL gestionado exclusivamente con migraciones de Liquibase.
- **API Gateway:** Spring Cloud Gateway (Spring WebFlux reactivo). NUNCA agregar `spring-boot-starter-web` a este módulo ni `@RestController` con lógica de negocio.
- **Microservicios Backend:** Usan Spring Web (Servlets clásicos), Spring Data JPA, MapStruct y Lombok.

## 2. Patrón de Diseño Obligatorio: Generic CRUD Architecture
Todos los microservicios backend deben seguir el patrón de abstracción genérica para entidades, mappers, repositorios, reglas de negocio, servicios y controladores.

### A. Persistencia (Entities & Repositories)
- **`BaseEntity<ID>`:** `@MappedSuperclass` con auditoría (`createdAt`, `createdBy`, `lastModifiedAt`, `lastModifiedBy`) y clave primaria genérica (`ID`).
- **`BaseCatalog`:** Extiende de `BaseEntity<Long>` para entidades de catálogo con `name`, `code` y `description`.
- **`BaseRepository<T, ID>`:** Interfaz genérica `@NoRepositoryBean` que extiende de `JpaRepository<T, ID>` y provee ordenación por defecto (`getAllAscending()`, `getAllDescending()`).

### B. Mapeo y DTOs (MapStruct)
- **DTOs:** Definidos mediante Java **`record`** con validaciones de `@jakarta.validation`.
- **`BaseMapper<E, D>`:** Interfaz genérica para mapeos bidireccionales (`toDto`, `toEntity`, listas).
- **Configuración MapStruct:** Usar interfaz `@MapperConfig` centralizada (`componentModel = "spring"`, `unmappedTargetPolicy = ReportingPolicy.IGNORE`).

### C. Reglas de Negocio (Validation Layer)
- **`DaoRules<E>` / `BaseDaoRules<E, R, ID>`:** Capa explícita de validación de reglas de negocio (`canSave`, `canUpdate`, `canDelete`).
- Lanza excepciones personalizadas de tipo `BusinessRuleException` con códigos de error de negocio.

### D. Capa de Servicio (Service Layer)
- **`BaseService<T, D, ID, R, M, S>`:** Clase abstracta genérica que inyecta `repository`, `mapper` y `rules`.
- Provee de forma transparente operaciones CRUD transaccionales (`@Transactional`) tanto para Entidades como para DTOs (`findAllDto`, `findByIdDto`, `saveDto`, `updateDto`, `deleteById`).

### E. Capa de Control (REST Controllers)
- **Segregación por Interfaces:** Usar interfaces genéricas como `ReadController<T, D, ID>`, `CreateController`, etc., que utilizan métodos `default` para exponer los endpoints REST `@GetMapping`, `@PostMapping`, etc., usando la respuesta estandarizada `RestResponse<T>`.
- **`BaseController`:** Suministra el acceso al servicio genérico.

## 3. Convenciones de Código
- Usar Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`) en entidades y servicios para mantener el código limpio.
- Respuestas HTTP estandarizadas envolviendo las salidas en `RestResponse<T>`.
- Manejo de internacionalización (i18n) en mensajes de error mediante archivos `messages.properties` y la cabecera `Accept-Language`.
- Seguir las convenciones de nombres de paquetes y clases establecidas en el proyecto para mantener la coherencia y facilitar la navegación del código.
- Escribir comentarios claros y concisos en el código para mejorar la mantenibilidad y comprensión del mismo, todo comentario y nombres de variables deben ser descriptivos y en ingles. Evitar abreviaturas innecesarias y mantener la consistencia en el estilo de codificación.

# 4. Buenas Prácticas Adicionales
- Mantener la consistencia en el estilo de codificación en todo el proyecto.
- Evitar la duplicación de código mediante la reutilización de componentes genéricos.
- Escribir pruebas unitarias y de integración para asegurar la calidad del código.
- Documentar las APIs REST utilizando herramientas como Swagger/OpenAPI.
- Realizar revisiones de código periódicas para asegurar la calidad y consistencia del código.
- Mantener actualizada la documentación del proyecto para facilitar la incorporación de nuevos desarrolladores.
- Seguir las mejores prácticas de seguridad, como la gestión adecuada de credenciales y la protección de datos sensibles.
- Mantener las dependencias del proyecto actualizadas para evitar vulnerabilidades de seguridad conocidas.