# Guía técnica de configuración de Gradle y convenciones

Este documento sirve como referencia arquitectónica para la gestión de dependencias, los ámbitos de Gradle (`configurations`) y las convenciones utilizadas en la arquitectura de microservicios.

## Índice

- [1. Objetivos y principios](#1-objetivos-y-principios)
- [2. Ámbitos de Gradle](#2-ámbitos-de-gradle)
- [3. Diccionario de dependencias](#3-diccionario-de-dependencias)
- [4. Criterios para ubicar dependencias](#4-criterios-para-ubicar-dependencias)
- [5. Plantilla base de módulos](#5-plantilla-base-de-módulos)
- [6. Buenas prácticas](#6-buenas-prácticas)
- [7. Lista de verificación](#7-lista-de-verificación)

## 1. Objetivos y principios

La configuración de dependencias debe seguir estos principios:

1. Declarar cada dependencia en el módulo o convención más específico que la necesite.
2. Evitar exponer dependencias transitivas innecesariamente.
3. Separar claramente las dependencias de compilación, ejecución y pruebas.
4. Mantener las dependencias comunes en plugins de convención reutilizables.
5. No mezclar dependencias de aplicaciones bloqueantes con dependencias reactivas.
6. Centralizar versiones mediante el catálogo de versiones de Gradle (`libs.versions.toml`) cuando el proyecto lo utilice.

## 2. Ámbitos de Gradle

Un ámbito (`configuration`) determina cuándo está disponible una dependencia y si se expone de forma transitiva a otros módulos.

| Ámbito | ¿Cuándo usarlo? | Explicación técnica |
| --- | --- | --- |
| `implementation` | Código normal del módulo. | La dependencia está disponible durante la compilación y ejecución del módulo actual, pero no se expone transitivamente a los módulos que dependan de él. Es la opción predeterminada recomendada. |
| `api` | Librerías compartidas, contratos o SDKs públicos. | Expone la dependencia transitivamente. Si el módulo B depende del módulo A y A declara `api` para Guava, B también puede utilizar Guava. Requiere el plugin `java-library`. |
| `compileOnly` | Anotaciones, APIs proporcionadas por el entorno o dependencias disponibles externamente. | Solo está disponible durante la compilación y no se empaqueta en el artefacto final. Ejemplos: Lombok y APIs proporcionadas por un servidor de aplicaciones. |
| `runtimeOnly` | Drivers y plugins necesarios únicamente en ejecución. | No está disponible para compilar el código, pero sí para ejecutar la aplicación. Ejemplo: el driver JDBC de PostgreSQL cuando no se importan sus clases directamente. |
| `annotationProcessor` | Generadores de código. | Herramientas que procesan anotaciones y generan clases durante la compilación. Ejemplos: Lombok y el procesador de MapStruct. |
| `testImplementation` | Código de pruebas unitarias e integración. | Dependencias necesarias para compilar y ejecutar pruebas en `src/test/java`. Ejemplos: JUnit, Mockito y AssertJ. |
| `testCompileOnly` | Anotaciones exclusivas de pruebas. | Equivale a `compileOnly`, pero está restringido al código de pruebas. |
| `testRuntimeOnly` | Motores o componentes requeridos solo al ejecutar pruebas. | No son necesarios para compilar el código de pruebas, pero sí para ejecutarlo. Ejemplo: `junit-platform-launcher`. |
| `liquibaseRuntime` | Dependencias utilizadas por el plugin de Liquibase. | Se emplea exclusivamente para tareas como `update` o `generateChangelog` ejecutadas mediante Gradle. |

### 2.1. Reglas prácticas

- Preferir `implementation` sobre `api` para reducir el acoplamiento entre módulos.
- Utilizar `api` únicamente cuando los tipos de la dependencia formen parte de la API pública del módulo.
- No usar `compileOnly` si la aplicación necesita la dependencia durante la ejecución.
- No usar `runtimeOnly` si el código necesita importar clases de esa dependencia para compilar.
- Mantener los procesadores en `annotationProcessor`; no declararlos únicamente como `implementation`.
- Las dependencias de pruebas no deben declararse como dependencias de producción.

## 3. Diccionario de dependencias

### 3.1. Core y utilidades (`java-common-conventions`)

- **`lombok`**: reduce código repetitivo generando getters, setters, constructores y builders mediante anotaciones en tiempo de compilación.
- **`mapstruct`**: API para declarar mapeos entre objetos, por ejemplo entre entidades y DTOs, sin depender de reflexión en tiempo de ejecución.
- **`mapstruct-processor`**: procesador que genera las implementaciones de los mapeadores de MapStruct durante la compilación.
- **`lombok-mapstruct-binding`**: adaptador que permite que MapStruct reconozca correctamente los getters y setters generados por Lombok.
- **`jackson-databind`**: motor principal de Jackson para serializar objetos Java a JSON y deserializar JSON a objetos Java.
- **`jackson-datatype-jsr310`**: añade soporte para la API de fechas de Java 8+, como `LocalDate` y `LocalDateTime`.
- **`spring-boot-starter-validation`**: integra Hibernate Validator para validar DTOs mediante anotaciones como `@NotNull`, `@Size` y `@Email`.

### 3.2. Persistencia y migraciones

Convenciones relacionadas: `spring-jpa-conventions` y `migration-conventions`.

- **`spring-boot-starter-data-jpa`**: incluye Hibernate, JPA y Spring Data para trabajar con bases de datos mediante interfaces `Repository`.
- **`spring-data-envers`**: integra Hibernate Envers con Spring Data JPA para auditoría y versionado de entidades, generando historiales en tablas como `_AUD`.
- **`postgresql`**: driver JDBC oficial para conectar aplicaciones Java con PostgreSQL.
- **`spring-boot-starter-liquibase`**: integra Liquibase con Spring Boot para ejecutar migraciones automáticamente al iniciar el contexto de la aplicación.
- **`liquibase-core`**: motor que interpreta y ejecuta changelogs en XML, YAML, JSON o SQL.
- **`picocli`**: biblioteca para crear interfaces de línea de comandos (CLI), requerida internamente por determinadas tareas del plugin de Liquibase.

### 3.3. Web y programación reactiva

Convenciones relacionadas: `spring-web-conventions` y `spring-webflux-conventions`.

- **`spring-boot-starter-web`**: incluye Spring MVC y Tomcat embebido para aplicaciones imperativas y bloqueantes.
- **`spring-boot-starter-webflux`**: incluye WebFlux, Netty embebido y Project Reactor para aplicaciones reactivas y no bloqueantes.
- **`spring-cloud-starter-gateway-server-webflux`**: proporciona Spring Cloud Gateway para enrutamiento, filtrado y balanceo de carga sobre WebFlux.

> **Importante:** un servicio debe elegir entre el modelo bloqueante (`spring-web`) y el reactivo (`spring-webflux`). No se deben mezclar ambos starters sin una justificación arquitectónica documentada.

### 3.4. Pruebas

- **`spring-boot-starter-test`**: starter de pruebas que incluye JUnit Jupiter, AssertJ, Hamcrest, Mockito, JSONPath y Spring Test.
- **`reactor-test`**: herramientas para probar flujos reactivos mediante `StepVerifier` y validar emisiones de `Mono` y `Flux`.
- **`junit-platform-launcher`**: permite al IDE y a las herramientas de compilación descubrir y ejecutar pruebas mediante la plataforma JUnit 5.

## 4. Criterios para ubicar dependencias

### 4.1. Diagrama de decisión

1. **¿Se usa en todos los módulos Java?**  
    Agregarla a `java-common-conventions.gradle` o a su equivalente Kotlin DSL.
2. **¿Se usa en todos los servicios Spring Boot?**  
    Agregarla a `spring-boot-conventions.gradle` o a su equivalente Kotlin DSL.
3. **¿Es exclusiva de persistencia o base de datos?**  
    Agregarla a `spring-jpa-conventions.gradle`.
4. **¿Es exclusiva de migraciones?**  
    Agregarla a `migration-conventions.gradle`.
5. **¿Es un starter web?**  
    Determinar si el servicio es bloqueante (`spring-web-conventions`) o reactivo (`spring-webflux-conventions`).
6. **¿Es exclusiva de un microservicio?**  
    Agregarla directamente al `build.gradle` del módulo correspondiente.

### 4.2. Ubicación recomendada por alcance

| Caso | Ubicación recomendada |
| --- | --- |
| Dependencia común a todos los módulos | Convención Java común |
| Dependencia común a todos los servicios Spring Boot | Convención Spring Boot |
| Dependencia de JPA o auditoría | Convención de persistencia |
| Dependencia de Liquibase | Convención de migraciones |
| Dependencia exclusiva de un servicio | `build.gradle` de ese servicio |
| Dependencia exclusiva de pruebas de un servicio | `testImplementation` en ese módulo |

## 5. Plantilla base de módulos

### 5.1. Microservicio REST estándar

```groovy
```groovy
plugins {
     id 'spring-boot-conventions'
     id 'spring-web-conventions'          // Usar spring-webflux-conventions si es reactivo
     id 'spring-jpa-conventions'          // Eliminar si no utiliza base de datos
     id 'migration-conventions'           // Eliminar si no ejecuta migraciones Liquibase
}

group = 'com.icms'
version = '0.0.1-SNAPSHOT'

dependencies {
     // 1. Módulos internos
     implementation project(':shared-kernel')

     // 2. Dependencias específicas de este módulo
     // implementation libs.tu.libreria.especifica

     // 3. Dependencias específicas de pruebas
     // testImplementation libs.tu.libreria.de.test
}
```

### 5.2. Módulo sin persistencia

```groovy
plugins {
     id 'spring-boot-conventions'
     id 'spring-web-conventions'
}
```

### 5.3. Módulo reactivo

```groovy
plugins {
     id 'spring-boot-conventions'
     id 'spring-webflux-conventions'
}
```

## 6. Buenas prácticas

- Mantener una única fuente de verdad para las versiones de las dependencias.
- Actualizar dependencias de forma controlada y revisar sus vulnerabilidades.
- Evitar declarar la misma dependencia en varios módulos si puede pertenecer a una convención.
- Revisar el árbol de dependencias con `./gradlew dependencies` cuando exista un conflicto de versiones.
- Inspeccionar dependencias transitivas con `./gradlew dependencyInsight --dependency <nombre>`.
- No incluir drivers, herramientas de migración ni librerías de desarrollo en el artefacto si no son necesarios en producción.
- Mantener separados los contratos públicos de los detalles internos de implementación.
- Documentar cualquier excepción a estas convenciones en el `build.gradle` del módulo.

## 7. Lista de verificación

Antes de incorporar una dependencia o crear un módulo nuevo, comprobar lo siguiente:

- [ ] Se ha definido el propósito de la dependencia.
- [ ] Se ha elegido correctamente el ámbito (`implementation`, `api`, `compileOnly`, `runtimeOnly` o pruebas).
- [ ] Se ha colocado en la convención adecuada o en el módulo específico.
- [ ] No se están mezclando los stacks bloqueante y reactivo sin justificación.
- [ ] La versión está centralizada cuando corresponde.
- [ ] Se han revisado las dependencias transitivas y posibles conflictos.
- [ ] Se ha validado la compilación con `./gradlew build`.
- [ ] Se han ejecutado las pruebas con `./gradlew test`.
- [ ] Las migraciones se han probado en un entorno controlado antes de aplicarlas.
- [ ] La dependencia no expone innecesariamente una API interna a otros módulos.
