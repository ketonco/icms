---
name: pdf-reader
description: Extrae texto de cualquier PDF, con OCR alternativo para documentos escaneados. Úsala para leer, resumir o buscar en PDFs. Es de solo lectura.
metadata:
  mcpmarket-version: 1.0.0
---

# PDF Reader

Cuando un PDF forme parte de una tarea de ICMS, sigue AGENTS.md para las rutas y permisos. No asumas una carpeta fija ni modifiques los archivos fuente.

Extrae texto con pdf-text. Detecta PDFs con texto nativo y usa tesseract como alternativa OCR para documentos escaneados.

## Límite: lectura y escritura

| Tarea | Herramienta |
| --- | --- |
| Extraer texto, resumir o buscar | pdf-reader |
| Crear, unir, dividir, poner marcas o rellenar formularios | herramienta de edición PDF disponible |

## Cuándo usarla

- El usuario proporciona un PDF y solicita su contenido.
- Quiere resumirlo o buscar información.
- Necesita extraer datos de documentos escaneados.
- Quiere saber si el PDF contiene texto o imágenes.

## Uso

- Extrae el texto completo con pdf-text documento.pdf.
- Para páginas específicas usa pdf-text --pages 1-5 documento.pdf.
- Fuerza OCR con pdf-text --ocr documento.pdf.
- Revisa clasificación y metadatos con pdf-text --info documento.pdf.
- Para un PDF proporcionado por URL, descárgalo solo si la tarea y el acceso lo permiten; conserva el archivo en una ubicación autorizada.
- Para documentos extensos, extrae rangos de páginas para mantener la revisión manejable.

## Detección automática

La herramienta toma una muestra de las primeras páginas con pdftotext. Si encuentra muy poco texto no vacío, clasifica el documento como escaneado y recurre a OCR con tesseract. La calidad del OCR depende de las imágenes.

## Opciones

| Opción | Función |
| --- | --- |
| --pages N-M | Extraer páginas N a M |
| --pages N | Extraer una página |
| --ocr | Forzar OCR |
| --info | Mostrar metadatos y tipo de PDF |
| --version | Mostrar versión |

## Requisitos

Poppler proporciona pdftotext, pdfinfo y pdftoppm. Tesseract se necesita solo para documentos escaneados. Sigue las instrucciones de instalación de la plataforma y del plugin.

## Errores comunes

- Usar una herramienta de edición para leer texto.
- Extraer de una vez un PDF muy largo en vez de usar rangos.
- Asumir que el OCR falló cuando el resultado es imperfecto; revisa primero la clasificación y calidad del escaneo.
