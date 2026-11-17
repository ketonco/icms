---
applyTo: "**/*.java, **/*.gradle, **/*.gradle.kts, **/*.toml, **/*.yml, **/*.yaml"
---

# Convenciones comunes de ICMS

## Contexto vigente

- ICMS es un monorepo Gradle con Java 24 y Spring Boot 4.1.1. El código de los servicios se escribe en Java; los scripts de convención usan Kotlin DSL.
- Los módulos desplegables actuales son api y user-auth; shared-kernel es una biblioteca compartida. No asumir que módulos planeados o mencionados en ejemplos ya existen.
- Usa paquetes jakarta.* para JPA, validación y servlets. No uses javax.*.
- Las versiones se administran en gradle/libs.versions.toml y la configuración común de build en buildSrc. Para cambios de dependencias, sigue AGENTS.md y gradle-dependencies.instructions.md.

## Código y documentación

- Código, identificadores, comentarios y Javadoc en inglés; documentación para desarrolladores en español.
- Usa camelCase para métodos y variables, PascalCase para clases y SNAKE_CASE para constantes.
- Usa Lombok y MapStruct siguiendo los patrones existentes; no agregues getters, setters o builders manuales cuando los genera Lombok.
- No introduzcas APIs marcadas como deprecated en código nuevo.
- Para reglas específicas de un módulo, consulta la instrucción correspondiente desde .github/copilot-instructions.md o docs/contexto/project-map.md.

## Fuentes y evidencia

Las instrucciones describen el diseño esperado. Inspecciona código, configuración y pruebas pertinentes para conocer el estado implementado. No reescribas código funcional solo para que coincida con una instrucción; señala la diferencia y consulta cuando se requiera una decisión. MEMORY.md aporta contexto temporal, no sustituye las instrucciones ni la evidencia del repositorio.
