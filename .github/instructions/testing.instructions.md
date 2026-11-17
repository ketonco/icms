---
applyTo: "**/src/test/**"
---

# Instrucciones de pruebas

- Usa *Test para pruebas unitarias sin contexto Spring completo ni base de datos real, y *IT para pruebas de integración con contexto, HTTP o base de datos. No inventes sufijos alternativos. Todas se ejecutan con la tarea test según la configuración actual.
- Aplica cobertura selectiva: no repitas en cada entidad la cobertura de comportamiento genérico heredado. Añade pruebas específicas para consultas propias, constraints, reglas o lógica compleja.
- Para servicios unitarios, usa Mockito y verifica interacciones y resultados relevantes. Para mappers, sigue el mecanismo de instanciación y datos que usa el módulo. Para HTTP, elige RestAssured en user-auth y WebTestClient en api de acuerdo con los tests existentes.
- Los tests de repositorio usan la configuración de integración y base de datos definida por el proyecto; no cambies el perfil, proveedor de base o estrategia de esquema sin revisar las instrucciones del módulo y pedir aprobación cuando corresponda.
- No ejecutes builds ni pruebas sin autorización explícita según AGENTS.md. Al proponer un cambio de código, indica los casos concretos que lo validarían.
- Consulta 1guides/11-pruebas.md y, para gateway, 1guides/10-gateway-tests.md.
