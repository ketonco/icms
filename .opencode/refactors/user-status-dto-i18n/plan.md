# Plan: i18n para UserStatusDto, UserStatusTranslationDto y UserTypeTranslationDto

## Objetivo

Reemplazar los mensajes de validación inline de tres DTOs por claves de propiedad
resueltas a través del `MessageSource` existente, permitiendo que los mensajes de
validación de Bean Validation se internacionalicen según el header `Accept-Language`,
siguiendo exactamente el mismo patrón ya aplicado a `UserTypeDto`.

## Contexto actual

Los tres DTOs definen validaciones con mensajes hardcodeados en inglés:

### UserStatusDto (`user-auth/.../dto/userstatus/UserStatusDto.java`)

| Campo  | Anotación    | Mensaje inline actual                     |
|--------|--------------|-------------------------------------------|
| code   | @NotBlank    | "Code must not be blank"                  |
| code   | @Pattern     | "Code must be 3 uppercase letters"        |
| name   | @NotBlank    | "Name must not be blank"                  |
| active | @NotNull     | "Active cannot be null"                   |

### UserStatusTranslationDto (`user-auth/.../dto/userstatus/UserStatusTranslationDto.java`)

| Campo      | Anotación    | Mensaje inline actual                     |
|------------|--------------|-------------------------------------------|
| catalogId  | @NotNull     | "Catalog ID cannot be blank"              |
| languageId | @NotNull     | "Language ID cannot be blank"             |
| translation| @NotBlank    | "Translation cannot be blank"             |

### UserTypeTranslationDto (`user-auth/.../dto/usertype/UserTypeTranslationDto.java`)

| Campo      | Anotación    | Mensaje inline actual                     |
|------------|--------------|-------------------------------------------|
| catalogId  | @NotNull     | "Catalog ID cannot be blank"              |
| languageId | @NotNull     | "Language ID cannot be blank"             |
| translation| @NotBlank    | "Translation cannot be blank"             |

El proyecto ya tiene la infraestructura i18n lista:

- `MessageConfig` (`shared-kernel/.../config/i18n/MessageConfig.java:14`) configura
  un `ResourceBundleMessageSource` con basename `i18n/messages` y
  `setUseCodeAsDefaultMessage(true)`.
- Archivos de traducción: `messages.properties` (en-US) y
`messages_es.properties` (es-ES).
- Spring Boot auto-configura `LocalValidatorFactoryBean` usando el `MessageSource`
  bean disponible, por lo que no es necesario crear configuración adicional.

## Convención de claves

Se usa el patrón de claves de propiedad (estándar de Bean Validation):

```text
<dominio>.<campo>.<regla>
```

Claves para los tres DTOs:

| DTO                        | Clave                              | Uso                        |
|----------------------------|------------------------------------|----------------------------|
| UserStatusDto              | `usrstatus.code.blank`             | @NotBlank en code          |
| UserStatusDto              | `usrstatus.code.pattern`           | @Pattern en code           |
| UserStatusDto              | `usrstatus.name.blank`             | @NotBlank en name          |
| UserStatusDto              | `usrstatus.active.null`            | @NotNull en active         |
| UserStatusTranslationDto   | `usrstatustrans.catalogid.null`    | @NotNull en catalogId      |
| UserStatusTranslationDto   | `usrstatustrans.languageid.null`   | @NotNull en languageId     |
| UserStatusTranslationDto   | `usrstatustrans.translation.blank` | @NotBlank en translation   |
| UserTypeTranslationDto     | `usrtypetrans.catalogid.null`      | @NotNull en catalogId      |
| UserTypeTranslationDto     | `usrtypetrans.languageid.null`     | @NotNull en languageId     |
| UserTypeTranslationDto     | `usrtypetrans.translation.blank`   | @NotBlank en translation   |

## Archivos afectados

| Archivo | Línea(s) | Cambio |
|---------|----------|--------|
| `user-auth/src/main/java/com/icms/user_auth/dto/userstatus/UserStatusDto.java` | 9, 11, 13, 15 | Reemplazar mensajes inline por claves `{...}` |
| `user-auth/src/main/java/com/icms/user_auth/dto/userstatus/UserStatusTranslationDto.java` | 9, 11, 13 | Reemplazar mensajes inline por claves `{...}` |
| `user-auth/src/main/java/com/icms/user_auth/dto/usertype/UserTypeTranslationDto.java` | 8, 10, 12 | Reemplazar mensajes inline por claves `{...}` |
| `shared-kernel/src/main/resources/i18n/messages.properties` | nueva sección | Agregar 10 claves en inglés |
| `shared-kernel/src/main/resources/i18n/messages_es.properties` | nueva sección | Agregar 10 claves en español |
| `user-auth/src/test/java/com/icms/user_auth/dto/userstatus/UserStatusDtoValidationIT.java` | nuevo | Test de integración que valida i18n de mensajes |
| `user-auth/src/test/java/com/icms/user_auth/dto/userstatus/UserStatusTranslationDtoValidationIT.java` | nuevo | Test de integración que valida i18n de mensajes |
| `user-auth/src/test/java/com/icms/user_auth/dto/usertype/UserTypeTranslationDtoValidationIT.java` | nuevo | Test de integración que valida i18n de mensajes |

## Cambios paso a paso

### Paso 1: Modificar UserStatusDto

En `UserStatusDto.java`, reemplazar los mensajes inline por claves de propiedad:

```java
@NotBlank(message = "{usrstatus.code.blank}")
// must be 3 UPPERCASE letters Ej: ACT, INA, SUS
@Pattern(regexp = "^[A-Z]{3}$", message = "{usrstatus.code.pattern}")
String code,

@NotBlank(message = "{usrstatus.name.blank}")
String name,

@NotNull(message = "{usrstatus.active.null}")
Boolean active
```

### Paso 2: Modificar UserStatusTranslationDto

En `UserStatusTranslationDto.java`, reemplazar los mensajes inline por claves de propiedad:

```java
@NotNull(message = "{usrstatustrans.catalogid.null}")
Long catalogId,
@NotNull(message = "{usrstatustrans.languageid.null}")
Long languageId,
@NotBlank(message = "{usrstatustrans.translation.blank}")
String translation,
```

### Paso 3: Modificar UserTypeTranslationDto

En `UserTypeTranslationDto.java`, reemplazar los mensajes inline por claves de propiedad:

```java
@NotNull(message = "{usrtypetrans.catalogid.null}")
Long catalogId,
@NotNull(message = "{usrtypetrans.languageid.null}")
Long languageId,
@NotBlank(message = "{usrtypetrans.translation.blank}")
String translation,
```

### Paso 4: Agregar claves en messages.properties

Agregar al final del archivo (nueva sección):

```properties
# UserStatus Validation
usrstatus.code.blank=Code must not be blank
usrstatus.code.pattern=Code must be 3 uppercase letters
usrstatus.name.blank=Name must not be blank
usrstatus.active.null=Active cannot be null

# UserStatusTranslation Validation
usrstatustrans.catalogid.null=Catalog ID cannot be blank
usrstatustrans.languageid.null=Language ID cannot be blank
usrstatustrans.translation.blank=Translation cannot be blank

# UserTypeTranslation Validation
usrtypetrans.catalogid.null=Catalog ID cannot be blank
usrtypetrans.languageid.null=Language ID cannot be blank
usrtypetrans.translation.blank=Translation cannot be blank
```

### Paso 5: Agregar claves en messages_es.properties

Agregar al final del archivo (nueva sección):

```properties
# UserStatus Validation
usrstatus.code.blank=El código no debe estar vacío
usrstatus.code.pattern=El código debe tener 3 letras mayúsculas
usrstatus.name.blank=El nombre no debe estar vacío
usrstatus.active.null=Activo no puede ser nulo

# UserStatusTranslation Validation
usrstatustrans.catalogid.null=El ID del catálogo no puede ser nulo
usrstatustrans.languageid.null=El ID del idioma no puede ser nulo
usrstatustrans.translation.blank=La traducción no puede estar vacía

# UserTypeTranslation Validation
usrtypetrans.catalogid.null=El ID del catálogo no puede ser nulo
usrtypetrans.languageid.null=El ID del idioma no puede ser nulo
usrtypetrans.translation.blank=La traducción no puede estar vacía
```

### Paso 6: Crear test de integración para UserStatusDto

Crear `UserStatusDtoValidationIT.java` siguiendo el patrón de `UserTypeDtoValidationIT`:

- `@SpringBootTest` + `@ActiveProfiles("test")` + `@SuppressWarnings("null")`
- `Validator` inyectado vía `@Autowired`
- Helper `validate(dto)` que retorna `Set<ConstraintViolation<UserStatusDto>>`
- Helper `getMessageForPropertyAndAnnotation(violations, propertyPath, annotationType)`
- 8 tests (4 validaciones × 2 idiomas):
  - `codeBlankEnglish` / `codeBlankSpanish`
  - `codePatternEnglish` / `codePatternSpanish`
  - `nameBlankEnglish` / `nameBlankSpanish`
  - `activeNullEnglish` / `activeNullSpanish`

### Paso 7: Crear test de integración para UserStatusTranslationDto

Crear `UserStatusTranslationDtoValidationIT.java` con la misma estructura:

- 6 tests (3 validaciones × 2 idiomas):
  - `catalogIdNullEnglish` / `catalogIdNullSpanish`
  - `languageIdNullEnglish` / `languageIdNullSpanish`
  - `translationBlankEnglish` / `translationBlankSpanish`

### Paso 8: Crear test de integración para UserTypeTranslationDto

Crear `UserTypeTranslationDtoValidationIT.java` con la misma estructura:

- 6 tests (3 validaciones × 2 idiomas):
  - `catalogIdNullEnglish` / `catalogIdNullSpanish`
  - `languageIdNullEnglish` / `languageIdNullSpanish`
  - `translationBlankEnglish` / `translationBlankSpanish`

## Riesgos y mitigación

| Riesgo | Mitigación |
|--------|------------|
| El `LocalValidatorFactoryBean` no usa el `MessageSource` de Spring | Verificar que Spring Boot auto-configura el validador con el bean `MessageSource` existente. Si no, agregar `@Bean LocalValidatorFactoryBean` en `MessageConfig`. |
| Falta de traducción en un idioma | `setUseCodeAsDefaultMessage(true)` ya está activo: si falta una clave, se muestra la clave como mensaje (ej. `usrstatus.code.blank`). |
| Tests de integración fallan por dependencia de BD | El perfil `test` usa la BD real de test (Replace.NONE). Asegurar que la BD de test esté disponible o usar `@DataJpaTest` para tests aislados. |
| Romper otros DTOs | Este cambio solo toca los 3 DTOs del alcance. No se modifican otros DTOs. |
| Mensajes duplicados entre UserStatusTranslationDto y UserTypeTranslationDto | Cada DTO tiene su propio prefijo de dominio (`usrstatustrans.*` vs `usrtypetrans.*`), por lo que las claves son únicas aunque el texto del mensaje sea idéntico. |

## Tests para validar

1. **Compilación:** `./gradlew :user-auth:compileJava :shared-kernel:compileJava`
2. **Tests del módulo user-auth:** `./gradlew :user-auth:test`
3. **Tests específicos de i18n:**
   - `./gradlew :user-auth:test --tests "*UserStatusDtoValidationIT*"`
   - `./gradlew :user-auth:test --tests "*UserStatusTranslationDtoValidationIT*"`
   - `./gradlew :user-auth:test --tests "*UserTypeTranslationDtoValidationIT*"`
4. **Tests de shared-kernel:** `./gradlew :shared-kernel:test`

## Notas didácticas

- La sintaxis `{clave}` en las anotaciones de Bean Validation le indica al
  `LocalValidatorFactoryBean` que debe resolver el mensaje desde el `MessageSource`
  en lugar de usar el texto literal.
- `LocaleContextHolder.getLocale()` se popula automáticamente desde el header
  `Accept-Language` de la request HTTP gracias a Spring MVC.
- El fallback `setUseCodeAsDefaultMessage(true)` es una red de seguridad: si alguien
  olvida agregar una traducción, al menos se ve la clave (ej. `usrstatus.code.blank`)
  en lugar de una excepción `NoSuchMessageException`.
- Los tests usan `Validator` inyectado directamente en lugar de peticiones HTTP,
  porque no todos los DTOs nacen por un endpoint (ej.: `UserTypeController` solo
  implementa `ReadController`, no expone `POST`). Esto es más rápido y aislado.
- `Locale.of("es", "ES")` es la forma moderna (Java 19+) de crear un `Locale`,
  evitando la deprecación de `new Locale(...)`.
