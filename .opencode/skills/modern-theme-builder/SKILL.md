---
name: modern-theme-builder
description: Ayuda a explorar y elegir direcciones visuales y paletas de color para un frontend existente. Revisa la interfaz actual, investiga referencias del mismo tipo de producto y presenta opciones justificadas antes de hacer cambios.
---

# Modern Theme Builder

Ayuda a mejorar el criterio visual de una interfaz existente y a explorar temas coherentes con su propósito. Adapta las propuestas al producto concreto; no partas de una lista fija de estilos ni de una paleta predeterminada.

## Flujo de trabajo

### 1. Entender la interfaz y la petición

- Usa el contexto disponible del proyecto para identificar qué tipo de aplicación es, quién la usa y qué parte se quiere mejorar.
- Inspecciona solo los componentes y estilos relevantes para reconocer los colores, roles y patrones actuales. No leas todo el proyecto si basta con revisar los archivos relacionados.
- Resume los colores existentes en una tabla con su código HEX, uso observado y ubicación o elemento.
- Si el tipo de producto o el objetivo no se puede inferir del contexto, haz una pregunta breve. No conviertas la exploración en una entrevista ni preguntes cosas que puedas deducir.

### 2. Investigar referencias

- Investiga interfaces actuales del mismo tipo de producto: por ejemplo, dashboards para dashboards, tiendas para tiendas o aplicaciones de mascotas para productos de mascotas.
- Observa cómo usan color, contraste, fondos, superficies y acentos. Busca referencias que ayuden a entender decisiones visuales, no solo imágenes llamativas.
- Presenta enlaces a las referencias y distingue lo que observaste en ellas de tus propias recomendaciones.
- Si no hay acceso a herramientas de investigación web, dilo claramente y continúa con el contexto disponible; no inventes referencias.

### 3. Presentar direcciones visuales

Antes de desarrollar una paleta final, presenta una tabla con 2–3 direcciones visuales distintas que hayan surgido del contexto y la investigación. No repitas siempre categorías predeterminadas.

Para cada dirección incluye:

- Nombre y sensación que transmite.
- Colores representativos con códigos HEX.
- Cómo podrían usarse en esta interfaz.
- Qué la hace coherente con el producto y qué posible inconveniente tendría.

Las opciones pueden ser sobrias, suaves, cálidas, atrevidas u otra combinación que tenga sentido. No des por hecho que hay que mantener los colores de marca: puedes incluir una propuesta que los reinterprete o los deje en segundo plano, explicando el efecto.

Detente después de presentar las direcciones y espera a que la persona elija, pida ajustes o solicite más opciones.

### 4. Desarrollar la dirección elegida

Una vez elegida una dirección:

- Propón una o más paletas aplicadas a los elementos reales de la interfaz.
- Incluye códigos HEX y roles concretos, como fondo, superficie, navegación, texto, botón principal, selección, enlaces y estados.
- Explica brevemente la sensación, jerarquía y relación entre los colores; señala conflictos de legibilidad o contraste que convenga revisar.
- Si el contexto incluye modo claro y oscuro, indica cómo funcionaría la propuesta en ambos o qué queda por definir.
- Sugiere ajustes de estilo adicionales —por ejemplo, bordes o sombras— solo si ayudan a que la dirección visual funcione. No amplíes el cambio a otros elementos sin explicar por qué.

### 5. Iterar sin fricción

- Si la persona no conecta con las opciones, presenta nuevas direcciones o adapta las propuestas según su reacción. No defiendas una opción solo porque ya se propuso.
- Si pide más alternativas, sigue explorando sin exigirle que explique teoría de color.
- Usa sus preferencias y comentarios como información para esta iteración, no como reglas universales permanentes.
- No edites el frontend al presentar propuestas. Modifica código solo cuando la persona pida implementar una opción.
