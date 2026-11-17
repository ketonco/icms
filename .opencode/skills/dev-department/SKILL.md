---
name: dev-department
description: Coordina tareas de desarrollo de ICMS en OpenCode mediante planner, executor y tester. Úsala para cambios de varias fases o módulos.
metadata:
  short-description: Orquestar desarrollo en ICMS
---

# Departamento de desarrollo ICMS

Esta skill coordina los perfiles de .opencode/agents/. AGENTS.md define el comportamiento y las autorizaciones; docs/contexto/project-map.md orienta a las fuentes técnicas. El coordinador planifica, presenta planes, gestiona autorizaciones y traspasos. No sustituye el trabajo de implementación o pruebas de los perfiles.

## Contexto

Lee AGENTS.md y MEMORY.md al inicio. Usa el mapa para consultar solo instrucciones y guías que apliquen a las rutas afectadas. Inspecciona las fuentes de código/configuración con herramientas de lectura. Ejecuta git status o git diff solo tras autorización explícita conforme a AGENTS.md; si no está autorizada, no uses shell. Consulta pending.md solo si es pertinente; no agregues ni cierres entradas sin autorización específica. No trates notas históricas o pasos en MEMORY.md como tareas aprobadas.

ICMS tiene actualmente api (gateway WebFlux), user-auth (MVC/JPA) y shared-kernel. No presupongas que módulos futuros o un frontend ya existen. Si el plan abarca frontend, primero identifica su ruta, stack y scripts reales; cuando aún no exista, informa que no hay implementación que cambiar o probar y no inventes rutas, puertos ni dependencias.

## Flujo

1. **Planificación:** invoca dev-planner para analizar el alcance, rutas, riesgos, criterios y pruebas. Para tareas sencillas puede bastar un plan compacto, pero siempre debe indicar archivos y verificaciones.
2. **Aprobación:** presenta plan y archivos afectados al usuario y espera aprobación explícita. La aprobación solo cubre lo enumerado. Cambios de dependencias, esquema, base de datos, comandos con efectos o rutas nuevas siguen los gates de AGENTS.md. Persiste planes en .opencode/plans/ únicamente si el usuario aprobó esa ruta.
3. **Ejecución:** delega producción aprobada a dev-executor. Entrega únicamente fase y rutas autorizadas. No le asignes tests.
4. **Pruebas:** después de la implementación, delega a dev-tester únicamente los casos y comandos aprobados. En tareas solo de pruebas, omite executor. No ejecutes comandos por inferencia.
5. **Cierre:** contrasta el diff con el plan, presenta evidencia, comandos y resultados, pruebas no ejecutadas y limitaciones. Los cambios en pending.md o MEMORY.md requieren autorización específica conforme a AGENTS.md.

## Roles

- dev-planner: solo lectura y planificación; no edita, prueba ni delega.
- dev-executor: implementa producción en rutas aprobadas; no modifica ni ejecuta tests.
- dev-tester: escribe y ejecuta solo pruebas aprobadas; no modifica producción ni registros.

Si aparece un efecto, ruta o riesgo no aprobado, detén esa parte y consulta al usuario. Ningún perfil puede hacer commit, push, stash, merge o despliegue.
