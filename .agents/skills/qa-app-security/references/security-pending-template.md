# Plantilla de informe de auditoría dinámica

Esta plantilla estructura el informe de una auditoría autorizada. No es un tracker ni un registro persistente de pendientes. El informe se entrega en la conversación; guardar una copia requiere ruta y autorización específicas conforme a AGENTS.md.

## Alcance y autorización

- Solicitud/fecha:
- Origen localhost aprobado (esquema, host y puerto):
- APIs/rutas/métodos o pantallas incluidas/excluidas:
- Pruebas activas aprobadas y límites:
- Cuentas/datos sintéticos utilizados, sin credenciales:

## Resumen

- Veredicto y alcance de la conclusión:
- Hallazgos confirmados:
- Indicios por validar:
- Áreas no verificables:

## Hallazgos

Usa IDs locales a este informe: SEC-01, SEC-02, etc. No son identificadores persistentes ni se continúan entre auditorías. No asignes ID a mejoras informativas o falsos positivos.

### SEC-NN — Título conciso

- **Estado:** confirmado / indicio no confirmado / no verificable / falso positivo
- **Severidad y justificación:** crítica / alta / media / baja / informativa
- **Área:** identidad / sesiones / autorización / comunicaciones / entradas / IA / infraestructura observable / errores y filtraciones
- **Ubicación observable:** origen/ruta sin query strings ni valores sensibles
- **Evidencia y confianza:** señal redactada; alta / media / baja
- **Precondiciones:** rol y datos sintéticos, sin credenciales
- **Reproducción mínima:** pasos seguros y no destructivos
- **Esperado / observado:** comportamiento requerido y respuesta visible
- **Impacto:** consecuencia respaldada por evidencia
- **Recomendación:** remediación sugerida, sin asumir acceso a código
- **Pendiente/no verificable:** evidencia adicional requerida

## Verificaciones y limitaciones

- Herramientas y comprobaciones ejecutadas:
- Comprobaciones no ejecutadas y motivo:
- Áreas no revisadas/no verificables:
- Acciones detenidas y motivo:
- Próximos pasos sugeridos:

No incluir secretos, contraseñas, tokens, cookies, PII, cuerpos sensibles, query strings sensibles ni capturas sin redactar.
