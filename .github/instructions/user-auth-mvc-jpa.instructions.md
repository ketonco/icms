---
applyTo: "user-auth/**"
---

# Instrucciones del módulo user-auth

- user-auth es un servicio Servlet con Spring Web MVC, Spring Data JPA, PostgreSQL, Spring Security y Liquibase. Mantén sus operaciones de aplicación síncronas.
- No agregues WebFlux, Reactor, Mono o Flux en src/main. spring-boot-starter-webflux puede estar en src/test únicamente como cliente WebTestClient cuando el caso lo requiera.
- Usa Jakarta y las convenciones compartidas de entidades, DTOs, MapStruct, validación, servicios y controladores definidas en shared-kernel. Revisa primero las clases base existentes antes de crear otra abstracción.
- La configuración de persistencia debe validar el esquema administrado por Liquibase; no habilites generación automática del esquema por Hibernate. Protege credenciales con variables de entorno y no guardes secretos.
- Los componentes de shared-kernel no se instancian por escaneo general en user-auth: verifica las autoconfiguraciones y sus beans antes de cambiar registro o escaneo. Evita registrar el mismo bean por dos vías.
- Sigue las instrucciones comunes de pruebas y migraciones. Guías útiles: 1guides/01-estructura-proyecto.md, 1guides/04-crear-migraciones.md, 1guides/09-plantilla-migracion.md y 1guides/11-pruebas.md.
