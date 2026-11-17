# Instrucciones técnicas de ICMS

Este archivo es el índice técnico para asistentes de código. Las instrucciones detalladas se separan por ámbito en .github/instructions/ para cargar solo las que apliquen a los archivos de la tarea.

## Fuentes técnicas canónicas

- [Convenciones comunes](instructions/project-conventions.instructions.md): stack vigente, estilo y reglas compartidas.
- [api — WebFlux](instructions/api-webflux.instructions.md): gateway reactivo, límites de dependencias y pruebas.
- [user-auth — MVC/JPA](instructions/user-auth-mvc-jpa.instructions.md): servicio Servlet, persistencia e integración con shared-kernel.
- [shared-kernel](instructions/shared-kernel.instructions.md): abstracciones, componentes compartidos y autoconfiguración.
- [Pruebas](instructions/testing.instructions.md): categorías, nombres y selección de cobertura.
- [Gradle y dependencias](instructions/gradle-dependencies.instructions.md): catálogo, plugins y cambios de dependencias.
- [Liquibase](instructions/liquibase.instructions.md): migraciones y seguridad de operaciones de base de datos.
- [Mapa del proyecto](../docs/contexto/project-map.md): módulos, rutas e instrucciones y guías relacionadas.

## Cómo aplicar estas instrucciones

1. Lee [AGENTS.md](../AGENTS.md) para las reglas de trabajo y permisos.
2. Determina qué rutas y módulos afecta la tarea.
3. Aplica las instrucciones con applyTo correspondiente y consulta el mapa para guías operativas pertinentes.
4. Usa el código, configuración y pruebas del módulo como evidencia de lo que está implementado. Si hay diferencia entre diseño documentado y realidad, señala la discrepancia antes de proponer cambios.

No asumas que los módulos futuros mencionados en documentación ya existen. Las decisiones temporales están en [MEMORY.md](../MEMORY.md); los pendientes están en [pending.md](../pending.md) y no se modifican sin autorización específica.
