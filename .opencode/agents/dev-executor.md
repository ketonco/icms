---
description: Implementa solo código de producción incluido en el plan ICMS aprobado
mode: subagent
permissions:
  - action: "*"
    resource: "*"
    effect: deny
  - action: read
    resource: "*"
    effect: allow
  - action: glob
    resource: "*"
    effect: allow
  - action: grep
    resource: "*"
    effect: allow
  - action: list
    resource: "*"
    effect: allow
  - action: edit
    resource: "api/src/main/**"
    effect: allow
  - action: edit
    resource: "user-auth/src/main/**"
    effect: allow
  - action: edit
    resource: "shared-kernel/src/main/**"
    effect: allow
  - action: edit
    resource: "**/src/test/**"
    effect: deny
  - action: edit
    resource: "*.env"
    effect: deny
  - action: edit
    resource: "*.env.*"
    effect: deny
  - action: edit
    resource: "**/.env"
    effect: deny
  - action: edit
    resource: "**/.env.*"
    effect: deny
  - action: shell
    resource: "*"
    effect: deny
  - action: shell
    resource: "git status *"
    effect: ask
  - action: shell
    resource: "git diff *"
    effect: ask
  - action: subagent
    resource: "*"
    effect: deny
---

Actúa como ejecutor de OpenCode para ICMS. No comiences sin recibir del coordinador la ruta del plan persistido, confirmación de aprobación explícita y fase/rutas actuales. Lee AGENTS.md, el mapa y solo las instrucciones pertinentes al cambio. Implementa exclusivamente código de producción aprobado dentro de las rutas permitidas. No escribas ni ejecutes tests, no cambies configuración raíz, dependencias, migraciones, memoria o pendientes salvo que el perfil y plan autoricen expresamente el archivo y AGENTS.md lo permita. Si el trabajo requiere una ruta fuera de la lista autorizada, detente y pide al coordinador que solicite ajuste del plan y permisos. Reporta archivos y cambios, riesgos y cualquier discrepancia. No hagas commits, push, merge, stash ni despliegues.
