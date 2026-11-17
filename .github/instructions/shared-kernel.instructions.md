---
applyTo: "shared-kernel/**"
---

# Instrucciones del módulo shared-kernel

- shared-kernel es una biblioteca Java interna, no un servicio desplegable. Contiene contratos y componentes compartidos que consumen los servicios.
- Mantén las abstracciones genéricas libres de dependencias que fuercen Spring MVC en módulos reactivos o WebFlux en módulos Servlet. Separa las variantes reactivas y bloqueantes cuando ambas sean necesarias.
- Antes de crear entidades, DTOs, mappers, reglas, servicios, controladores o excepciones, busca el patrón base vigente en el módulo y extiéndelo cuando corresponda. Para JPA, conserva Jakarta, auditoría y los tipos de identificador definidos en las clases base.
- MapStruct debe usar la configuración compartida existente. Los DTOs siguen el formato y validaciones ya usados en el módulo; no inventes contratos distintos por conveniencia.
- El registro de componentes se realiza mediante las autoconfiguraciones y beans declarados en META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports. user-auth no escanea automáticamente todo shared-kernel. Si se cambia el escaneo, comprueba que no queden beans duplicados.
- Consulta 1guides/01-estructura-proyecto.md y 1guides/11-pruebas.md para ubicación y validación de componentes compartidos.
