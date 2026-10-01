---
description: Verifica o propone guías en 1guides para un tema del proyecto
agent: plan
---

Para el tema indicado en $ARGUMENTS:

1. Busca en `1guides/` si ya existe una guía que cubra el tema, por nombre de
   archivo y por contenido.
2. Si existe: informa qué archivo la cubre, resume qué contiene y qué le
   faltaría según el estado actual del proyecto. No modifiques nada.
3. Si no existe: verifica primero que el tema corresponda a algo real del
   proyecto (módulo, herramienta, flujo o convención). Si no existe en el
   proyecto, infórmalo y detente.
4. Si el tema sí existe en el proyecto: redacta la propuesta completa de la
   guía (en español, estilo de `1guides/`, con rutas y ejemplos reales del
   código) y preséntala para revisión.
5. Aplica las modificaciones que se te indiquen y espera autorización explícita;
   solo entonces crea el archivo en `1guides/` con el siguiente número
   disponible (`NN-<tema>.md`). En modo plan no se ejecuta nada sin tu orden.
6. Si el tema indicado es `all`: revisa únicamente las guías ya creadas en
   `1guides/` contra el estado actual del proyecto e informa como lista cuáles
   requieren actualización y por qué, sin modificar ninguna. Cada guía se
   actualiza después una por una invocando este mismo comando con su tema.
