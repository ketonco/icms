---
name: qa-review
description: QA del proyecto: revisa arquitectura, código y pruebas contra AGENTS.md, copilot-instructions.md y MEMORY.md, mantiene qa_historic.md y registra ajustes en pending.md. Use for QA reviews, code audits, architecture checks, or test coverage reviews.
---

Ante el alcance indicado por el usuario en su mensaje (módulo, paquete o todo el proyecto):

1. Compara: lee `.github/copilot-instructions.md`, `AGENTS.md` y `MEMORY.md` y
   construye el checklist de revisión (estructura y módulos, aislamiento
   MVC/WebFlux, jerarquía de entidades, MapStruct, reglas, servicios,
   controladores, pruebas §I, migraciones §J, configuración §K, i18n, idioma
   del código). Añade estándares actuales: JUnit 5 + AssertJ + Instancio,
   patrón AAA, aserciones sustantivas, tests deterministas y aislados, sin
   duplicación, diseño escalable.
2. Historial: lee `qa_historic.md` en la raíz. Si NO existe, es la primera
   corrección: propón crearlo con su plantilla y haz la revisión COMPLETA de
   todo el código. Si existe, revisa solo los commits posteriores a la fecha
   registrada (`git log <fecha>..HEAD`) más los cambios sin commitear del
   alcance. Al terminar actualiza SOLO la fecha y el commit del historic;
   nunca añadas una entrada por ejecución.
3. Arquitectura: la revisión de estructura (módulos, Gradle, plugins de
   convención, dependencias centralizadas, aislamiento de pilas) es COMPLETA
   en cada corrida, aunque el histórico diga delta de código.
4. Tests: todo test creado y en commit está ejecutado y verde — NUNCA
   propongas "ejecutar" ni "falta probar" un test commiteado. Para cada
   lógica nueva (servicio, regla, mapper con lógica, controller, query propia)
   verifica que exista su test según §I respetando el principio selectivo (no
   proponer tests de genéricos heredados ya cubiertos). Un test commiteado
   débil o mal hecho es hallazgo válido; uno faltante se propone con patrón
   §I, ubicación y aserciones esperadas.
5. Pendientes: reconcilia `pending.md` — elimina los que ya están corregidos
   (se me olvidó borrar), completa los incompletos, sin duplicar; registra
   cada hallazgo nuevo con la plantilla estándar P-XX. Si un hallazgo obliga
   a cambiar `AGENTS.md` o `.github/copilot-instructions.md`, NO los toques:
   regístralo como pending de nivel Alto para que lo aplique el desarrollador.
6. Memoria: si durante la revisión el desarrollador deja constancia de una
   decisión momentánea ("lo hice así por ahora porque...") o de avance del
   proyecto que convenga recordar, fíjalo en `MEMORY.md` como decisión
   esencial con su porqué. Respeta su alcance: solo decisiones y
   aprendizajes, nunca bitácora, máximo 250 líneas y nunca secretos.
7. Presenta el resumen (hallazgos, pruebas propuestas, pendientes a borrar o
   completar, histórico a crear/actualizar, memoria a actualizar) y espera el
   sí. Sin tu orden no se escribe nada. Si solicitaste un cambio sobre lo
   propuesto, muestra de nuevo el resultado completo ya con el cambio aplicado
   y espera tu sí antes de ejecutar.
8. Tras el sí, los ÚNICOS archivos que modificas son `pending.md`,
   `qa_historic.md` y `MEMORY.md`. `AGENTS.md` y
   `.github/copilot-instructions.md` jamás se editan desde aquí; el código y
   los tests solo reciben marcadores `TODO (pending P-XX)`, nunca se corrigen
   ni se ejecutan.
9. Respeta markdownlint (blancos alrededor de encabezados/listas, niveles sin
   saltos) en todo `.md` que toques. Validación de runtime no te corresponde:
   si en el código no hay señal de fallo, el cambio está bien aplicado.
