# AGENTS.md — Reglas de trabajo de ICMS

Este archivo reúne reglas generales de colaboración. Las convenciones técnicas por ámbito están en [.github/copilot-instructions.md](.github/copilot-instructions.md) y sus fuentes enlazadas. El mapa para encontrar el contexto relevante es [docs/contexto/project-map.md](docs/contexto/project-map.md).

## Precedencia y evidencia

- Las instrucciones del sistema y las indicaciones explícitas del usuario tienen prioridad.
- Las instrucciones canónicas del proyecto definen el diseño y las convenciones esperadas.
- El código, la configuración y las pruebas son evidencia del estado realmente implementado; no se modifica código funcional solo para hacerlo coincidir con documentación. Si hay discrepancias, descríbelas y consulta antes de decidir cambios de diseño.
- MEMORY.md aporta contexto temporal y decisiones previas; no reemplaza las fuentes canónicas ni la evidencia actual.
- Lee primero este archivo y MEMORY.md. Usa el mapa para elegir solo las instrucciones y guías pertinentes a la tarea; no es necesario releer toda la documentación.

## Forma de trabajo y autorización

- Antes de editar, crear o eliminar archivos, presenta el plan, los archivos afectados y el cambio propuesto. Espera autorización explícita. La autorización se limita al alcance aprobado; cambios adicionales requieren consulta.
- Antes de ejecutar comandos, builds o pruebas, explica el propósito y espera autorización explícita. No ejecutes git commit, git stash ni git push.
- No añadas ni cambies dependencias en gradle/libs.versions.toml sin explicar la necesidad, ofrecer al menos dos opciones y obtener aprobación explícita.
- Los cambios de esquema y las migraciones Liquibase requieren plan y aprobación explícita. Para operaciones destructivas de base de datos, confirma también la base destino y el efecto concreto.
- Al presentar cambios, indica qué se agregó, quitó y conservó, con archivo y líneas; muestra el diff o el contenido exacto relevante antes de dar la tarea por terminada.
- Para cambios de código, describe los casos de prueba unitarios y de integración que los validarían. No ejecutes pruebas sin autorización.

## Documentación y estilo

- Código, identificadores, comentarios y Javadoc en inglés. Documentación para desarrolladores en español.
- Los comandos canónicos de OpenCode se nombran en inglés dentro de .opencode/commands/; el cuerpo de comandos y las guías de 1guides/ van en español. La carpeta commands/ de la raíz es legado.
- Los archivos Markdown siguen markdownlint v0.41.1 sin warnings, con líneas en blanco alrededor de encabezados y listas y sin saltos de nivel.
- Si una decisión se vuelve regla permanente, propone incorporarla en la fuente canónica adecuada. Mantén MEMORY.md breve, con máximo 250 líneas, y no guardes secretos.

## Gestión de pending.md y MEMORY.md

- pending.md en minúsculas es el registro canónico; PENDING.md es histórico y su eliminación queda reservada al desarrollador.
- No agregues, edites ni elimines entradas de pending.md por iniciativa propia. Presenta hallazgos y opciones al usuario y modifica el archivo solo con autorización explícita específica para esos cambios. Una revisión o generación de código no concede por sí sola permiso para actualizarlo.
- No cambies MEMORY.md salvo que la tarea lo pida o exista una decisión nueva cuya inclusión se haya autorizado.
- Cuando se autorice registrar un hallazgo, cada entrada usa identificador consecutivo P-XX, ubicación exacta y estado agregado/no agregado, problema, contexto, opciones, recomendación y nivel de acción. No inventes ni reutilices identificadores.

## Lectura y privacidad

- La inspección de solo lectura está permitida. Antes de modificar archivos aplica el proceso de autorización anterior.
- Nunca guardes contraseñas o tokens en archivos versionados, salvo valores ficticios de ejemplo claramente identificados.
- Al empezar una tarea, revisa MEMORY.md y las instrucciones específicas seleccionadas desde el mapa.
