# API Gateway y comunicacion entre servicios

El proyecto usa el starter WebFlux nuevo. Las rutas estan bajo `spring.cloud.gateway.server.webflux`:

```yaml
spring:
  cloud:
    gateway:
      server:
        webflux:
          routes:
```

La ruta actual hacia autenticacion usa el DNS de Compose:

```yaml
- id: servicio-autenticacion-usuarios
  uri: http://user-auth:${PORT_USER_AUTH}
  predicates:
    - Path=/api/v1/auth/**
```

La URL para cliente o navegador es `http://localhost:8080/api/v1/auth/test`. El gateway llama internamente a `http://user-auth:8081/api/v1/auth/test` y conserva la ruta salvo que se configure `RewritePath` o `StripPrefix`.

Si Spring Security protege el endpoint, una peticion HTML puede recibir `302 /login` y una peticion API normalmente `401`. No abrir en el navegador una redireccion a `http://user-auth:8081`: es un hostname interno. Configurar el backend para permitir el endpoint publico o devolver `401` sin formulario cuando corresponda.
