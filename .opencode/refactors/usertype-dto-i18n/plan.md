# Plan: i18n para UserTypeDto

## Objetivo

Reemplazar los mensajes de validación inline de `UserTypeDto` por claves de propiedad
resueltas a través del `MessageSource` existente, permitiendo que los mensajes de
validación de Bean Validation se internacionalicen según el header `Accept-Language`.

## Contexto actual

`UserTypeDto` define 4 validaciones con mensajes hardcodeados en inglés:

| Campo  | Anotación    | Mensaje inline actual                     |
|--------|--------------|-------------------------------------------|
| code   | @NotBlank    | "Code must not be blank"                  |
| code   | @Pattern     | "Code must be 3 uppercase letters"        |
| name   | @NotBlank    | "Name must not be blank"                  |
| active | @NotNull     | "Active cannot be null"                   |

El proyecto ya tiene la infraestructura i18n lista:

- `MessageConfig` (`shared-kernel/.../config/i18n/MessageConfig.java:14`) configura
  un `ResourceBundleMessageSource` con basename `i18n/messages` y
  `setUseCodeAsDefaultMessage(true)`.
- `MessageResolver` (`shared-kernel/.../Utils/MessageResolver.java:15`) resuelve
  mensajes de negocio usando `LocaleContextHolder`.
- Archivos de traducción: `messages.properties` (en-US) y `messages_es.properties` (es-ES).

Spring Boot auto-configura `LocalValidatorFactoryBean` usando el `MessageSource`
bean disponible, por lo que no es necesario crear configuración adicional.

## Convención de claves

Se usa el patrón de claves de propiedad (estándar de Bean Validation):

```text
<dominio>.<campo>.<regla>
```

Claves para UserTypeDto:

| Clave                    | Uso                        |
|--------------------------|----------------------------|
| `usrtype.code.blank`     | @NotBlank en code          |
| `usrtype.code.pattern`   | @Pattern en code           |
| `usrtype.name.blank`     | @NotBlank en name          |
| `usrtype.active.null`    | @NotNull en active         |

## Archivos afectados

| Archivo | Línea(s) | Cambio |
| --------- | ---------- | -------- |
| `user-auth/src/main/java/com/icms/user_auth/dto/usertype/UserTypeDto.java` | 9, 11, 13, 15 | Reemplazar mensajes inline por claves `{...}` |
| `shared-kernel/src/main/resources/i18n/messages.properties` | nueva sección | Agregar 4 claves en inglés |
| `shared-kernel/src/main/resources/i18n/messages_es.properties` | nueva sección | Agregar 4 claves en español |
| `user-auth/src/test/java/com/icms/user_auth/dto/usertype/UserTypeDtoValidationIT.java` | nuevo | Test de integración que valida i18n de mensajes |

## Cambios paso a paso

### Paso 1: Modificar UserTypeDto

En `UserTypeDto.java`, reemplazar los mensajes inline por claves de propiedad:

```java
@NotBlank(message = "{usrtype.code.blank}")
@Pattern(regexp = "^[A-Z]{3}$", message = "{usrtype.code.pattern}")
String code,

@NotBlank(message = "{usrtype.name.blank}")
String name,

@NotNull(message = "{usrtype.active.null}")
Boolean active
```

### Paso 2: Agregar claves en messages.properties

Agregar al final del archivo (nueva sección):

```properties
# UserType Validation
usrtype.code.blank=Code must not be blank
usrtype.code.pattern=Code must be 3 uppercase letters
usrtype.name.blank=Name must not be blank
usrtype.active.null=Active cannot be null
```

### Paso 3: Agregar claves en messages_es.properties

Agregar al final del archivo (nueva sección):

```properties
# UserType Validation
usrtype.code.blank=El código no debe estar vacío
usrtype.code.pattern=El código debe tener 3 letras mayúsculas
usrtype.name.blank=El nombre no debe estar vacío
usrtype.active.null=Activo no puede ser nulo
```

### Paso 4: Crear test de integración de validación i18n

Crear `UserTypeDtoValidationIT.java` que:

1. Envía requests con `Accept-Language: en-US` y verifica mensajes en inglés.
2. Envía requests con `Accept-Language: es-ES` y verifica mensajes en español.
3. Prueba cada campo (code blank, code pattern, name blank, active null).

Estructura sugerida del test:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserTypeDtoValidationIT {

    @LocalServerPort
    private int port;

    // Test: POST /api/user-types con code vacío + Accept-Language: en-US
    // Espera: 400 con mensaje "Code must not be blank"

    // Test: POST /api/user-types con code vacío + Accept-Language: es-ES
    // Espera: 400 con mensaje "El código no debe estar vacío"

    // Test: POST /api/user-types con code="ab" + Accept-Language: en-US
    // Espera: 400 con mensaje "Code must be 3 uppercase letters"

    // Test: POST /api/user-types con name vacío + Accept-Language: es-ES
    // Espera: 400 con mensaje "El nombre no debe estar vacío"

    // Test: POST /api/user-types con active=null + Accept-Language: en-US
    // Espera: 400 con mensaje "Active cannot be null"
}
```

## Riesgos y mitigación

| Riesgo | Mitigación |
| -------- | ------------ |
| El `LocalValidatorFactoryBean` no usa el `MessageSource` de Spring | Verificar que Spring Boot auto-configura el validador con el bean `MessageSource` existente. Si no, agregar `@Bean LocalValidatorFactoryBean` en `MessageConfig`. |
| Falta de traducción en un idioma | `setUseCodeAsDefaultMessage(true)` ya está activo: si falta una clave, se muestra la clave como mensaje (ej. `usrtype.code.blank`). |
| Tests de integración fallan por dependencia de BD | El perfil `test` usa la BD real de test (Replace.NONE). Asegurar que la BD de test esté disponible o usar `@DataJpaTest` para tests aislados. |
| Romper otros DTOs | Este cambio solo toca `UserTypeDto`. No se modifican otros DTOs. |

## Tests para validar

1. **Compilación:** `./gradlew :user-auth:compileJava :shared-kernel:compileJava`
2. **Tests del módulo user-auth:** `./gradlew :user-auth:test`
3. **Test específico de i18n:** `./gradlew :user-auth:test --tests "*UserTypeDtoValidationIT*"`
4. **Tests de shared-kernel:** `./gradlew :shared-kernel:test`

## Notas didácticas

- La sintaxis `{clave}` en las anotaciones de Bean Validation le indica al
  `LocalValidatorFactoryBean` que debe resolver el mensaje desde el `MessageSource`
  en lugar de usar el texto literal.
- `LocaleContextHolder.getLocale()` se popula automáticamente desde el header
  `Accept-Language` de la request HTTP gracias a Spring MVC.
- El fallback `setUseCodeAsDefaultMessage(true)` es una red de seguridad: si alguien
  olvida agregar una traducción, al menos se ve la clave (ej. `usrtype.code.blank`)
  en lugar de una excepción `NoSuchMessageException`.
