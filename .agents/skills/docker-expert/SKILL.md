---
name: docker-expert
description: Audita, modifica y explica Dockerfiles, Docker Compose y flujos de contenedores usando patrones y contexto de ICMS. Úsala para builds, seguridad, redes, volúmenes, healthchecks, depuración y configuración.
---

# Docker Expert

Ayuda a auditar, modificar y explicar Dockerfiles, Docker Compose y flujos de contenedores. Puede auditar, implementar cambios autorizados o guiar al usuario. La skill no concede permisos adicionales.

## Selección de modo

- **Auditoría:** inspecciona la configuración solicitada, informa hallazgos y propone cambios; no los aplica salvo que el usuario también haya pedido corregirlos.
- **Modificación:** implementa el alcance pedido después de inspeccionar el estado actual y las instrucciones del proyecto. Mantén los cambios acotados y explica su efecto.
- **Guía:** explica conceptos, comandos o pasos con ejemplos válidos para ICMS. Aclara qué comandos solo inspeccionan y cuáles construyen, inician, detienen, eliminan o alteran recursos.
- Si el modo no está claro, infiérelo del verbo del usuario. Si el cambio pedido es ambiguo, presenta una recomendación sin editar.

## Contexto de ICMS

Lee AGENTS.md y usa docs/contexto/project-map.md para encontrar las instrucciones técnicas y guías pertinentes. Para Docker y Compose consulta 1guides/05-docker-compose.md y 1guides/docker.md. Inspecciona los archivos reales pertinentes; el mapa orienta, pero no reemplaza evidencia. No crees un resumen canónico duplicado ni supongas rutas o servicios inexistentes.

## Auditoría

Para cada hallazgo accionable indica archivo y ubicación, evidencia, impacto, prioridad, recomendación y si es observación o cambio propuesto. Distingue hechos comprobados de riesgos condicionales y recomendaciones opcionales. Indica aspectos relevantes que no pudiste verificar.

Revisa según pertinencia:

- **Build e imágenes:** etapas, orden de COPY y caché, contexto, versiones/tags, tamaño, archivos innecesarios y .dockerignore.
- **Ejecución y seguridad:** usuario no root cuando sea compatible, privilegios mínimos, puertos, secretos fuera de imágenes y control de versiones; filesystem read-only o tmpfs solo si la aplicación lo soporta. No presentes .env como almacén cifrado ni asumas que Docker secrets está disponible en cualquier modo Compose.
- **Compose y red:** DNS de servicios, redes, puertos publicados/internos, dependencias y disponibilidad; depends_on no garantiza por sí mismo que una aplicación esté lista.
- **Datos y volúmenes:** persistencia, separación de datos/código/cachés e inicialización idempotente. Explica claramente operaciones que puedan borrar datos.
- **Salud y diagnóstico:** healthchecks compatibles con utilidades presentes y endpoints reales; comandos de inspección para estado, logs, procesos, configuración y conectividad.
- **Desarrollo y producción:** bind mounts, hot reload, depuración y overrides para desarrollo; imágenes reproducibles, configuración externa, exposición reducida y límites de recursos para producción cuando correspondan. No impongas un orquestador sin necesidad demostrada.

No conviertas cada patrón general en requisito. Comprueba compensaciones: read-only, no-root o quitar capacidades puede romper rutas de escritura o capacidades requeridas.

## Modificación y verificación

Sigue AGENTS.md para autorización. Revisa el estado de los archivos antes de editar; la aprobación cubre solo los archivos y acciones enumerados en el plan. Builds, pruebas, cambios de dependencias, servicios compartidos, comandos Docker y acciones de Git requieren autorización aplicable.

Prefiere primero comandos de inspección. Marca el efecto de cada comando que construye, inicia, detiene, elimina o limpia recursos. No ejecutes limpieza destructiva como down -v o system prune --volumes sin aprobación explícita y alcance entendido. Limita o redacta salida de docker inspect, variables y logs que puedan revelar secretos.

Al cerrar, resume archivos y comportamiento cambiado, evidencia/diff, comandos y resultados exactos y verificaciones no ejecutadas. No afirmes que una imagen, servicio o build funciona si no se verificó.

## Límites

Esta skill no presupone acceso autorizado a daemon, registry, servidores, nube o producción. Una auditoría de archivos no equivale a auditar contenedores en ejecución. No despliegues, publiques imágenes ni alteres entornos compartidos sin autorización específica. Presenta hallazgos para pending.md; no lo actualices sin autorización específica.
