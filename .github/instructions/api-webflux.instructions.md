---
applyTo: "api/**"
---

# Instrucciones del módulo api

- api es el gateway Spring Cloud Gateway con WebFlux sobre Netty; enruta por HTTP no bloqueante a servicios downstream y no mantiene persistencia propia.
- No agregues spring-boot-starter-web, Tomcat, Spring MVC, JPA ni llamadas JDBC bloqueantes a este módulo. No bloquees el Event Loop. R2DBC solo se consideraría si el gateway pasara a tener una necesidad real de persistencia.
- En código reactivo de producción, conserva los tipos Reactor y las APIs WebFlux del módulo. No reutilices abstracciones MVC bloqueantes de shared-kernel.
- En pruebas, usa WebTestClient y controla los servicios downstream con stubs; la suite normal no debe requerir que los microservicios reales estén levantados ni depender de sus seeds.
- Consulta 1guides/06-api-gateway.md para rutas y comunicación, y 1guides/10-gateway-tests.md para pruebas del gateway.
- Antes de cambiar configuración o dependencias, revisa api/build.gradle, application.yml y los plugins de convención aplicados.
