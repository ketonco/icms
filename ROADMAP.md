# ROADMAP ICMS — visión, módulos y fases

Guía “project manager” del proyecto: objetivo, modelo de acceso, módulos
presentes y planeados, y fases de ejecución. Es informativa, no normativa:
ante choque con el código o `.github/copilot-instructions.md`, mandan ellos.

## 1. Objetivo

Plataforma backend de multiservicios para aprender, con un carrito de compras
como caso guía: Spring Security con RBAC por operación, dependencias
centralizadas, WebMVC + WebFlux, i18n, Liquibase, Kafka, Stripe en sandbox,
n8n + WhatsApp en modo test gratuito, programación async, Docker y MCP.

## 2. Modelo de acceso

- `user-auth` es la única fuente de identidad y roles. Cada microservicio
  autoriza por operación (`hasAuthority`), no solo “autenticado”.
- Matriz base: `GUE` consulta el catálogo; `USR` consulta + carrito + compra;
  `MOD` gestiona el catálogo; `ADM` administra todo vía el módulo `admin`.
- Los permisos viven como catálogo sembrado (`PermissionDataSeed`) y se
  asignan por `UserType`. Nada de permisos hardcodeados en los servicios.

## 3. Módulos

| Módulo | Estado | Stack | BD | Rol |
| --- | --- | --- | --- | --- |
| `shared-kernel` | Existe | Lib | — | entidades base, DTOs, MapStruct, i18n, `EncryptEncoder`, reglas base |
| `user-auth` | Base lista | WebMVC | `ICMS_UA` | identidad, tipos, permisos, auditoría Envers |
| `api` | Existe | WebFlux gateway | — | enrutado, CORS |
| `catalog` | Nuevo | WebMVC | Propia | productos, inventario y mini-stock; lectura pública, CRUD según permiso; reutiliza `Base*`, seeds y auditoría |
| `cart` | Nuevo | WebFlux + Redis reactivo | Redis | carrito por usuario con TTL; checkout exige `USR`; pieza reactiva con estado |
| `billing` | Nuevo | WebMVC | Propia | facturas desde el carrito confirmado |
| `payments` | Nuevo | WebMVC | Propia | Stripe en sandbox: intentos, estados y webhooks |
| `admin` | Nuevo | WebMVC | Agrega vía gateway | administración total (usuarios, tipos, permisos, catálogo, pedidos, facturas, config). Solo `ADM` |
| `notifications` | Nuevo | WebFlux | — | fan-out a WhatsApp/n8n y stream SSE de seguimiento de pedido (`Flux`), showcase reactivo |
| `event-bus` | Nuevo | Kafka | — | `order.created`, `stock.reserved`, `payment.confirmed`; checkout por saga coreografiada |

## 4. Fases y criterios de terminado

- **F1 — cerrar `user-auth`:** JWT, mecanismo de autenticación,
  seeds y tests verdes.
- **F2 — `catalog` + RBAC:** lectura para `GUE`, gestión para `MOD`/`ADM`;
  aquí se aprende `hasAuthority` por operación.
- **F3 — `cart` reactivo + Redis:** carrito con TTL y checkout; primer
  contacto con Redis reactivo y composición con `WebClient`.
- **F4 — `billing` + `payments` (Stripe sandbox)** más `admin` mínimo
  (gestión de catálogo y pedidos).
- **F5 — Kafka async:** eventos entre carrito, stock, pagos y notificaciones.
- **F6 — n8n + WhatsApp + SSE:** comercio conversacional y seguimiento
  de pedido en vivo.
- Cada fase termina con: migraciones aplicadas, seeds, tests verdes y guía
  en `1guides/`.

## 5. Precedencia

Este archivo orienta a agentes IA y desarrolladores, pero no legisla: el
código real y `.github/copilot-instructions.md` prevalecen.
