# Plan: i18n para DTOs restantes (CreateUser, UserProfile, Language, Permission)

## Objetivo

Reemplazar los mensajes de validación inline en inglés de los cuatro DTOs
pendientes por claves `{dominio.campo.regla}` resueltas vía el `MessageSource`
existente, siguiendo el mismo patrón ya aplicado en `usertype-dto-i18n` y
`user-status-dto-i18n`. No se toca configuración i18n ni DTOs ya migrados.

## Contexto actual

Solo estos cuatro DTOs conservan mensajes literales (verificado con búsqueda
de `message =` en `user-auth/.../dto`):

| DTO | Archivo | Mensajes inline |
| --- | ------- | --------------- |
| CreateUserDto | `user-auth/.../dto/user/CreateUserDto.java` | 9 literales |
| UserProfileDto | `user-auth/.../dto/userprofile/UserProfileDto.java` | 6 literales |
| LanguageDto | `user-auth/.../dto/language/LanguageDto.java` | 7 literales |
| PermissionDto | `user-auth/.../dto/permission/PermissionDto.java` | 4 literales |

Ya migrados y fuera de alcance: `UserTypeDto`, `UserStatusDto`,
`UserStatusTranslationDto`, `UserTypeTranslationDto`.

Infraestructura existente (no crear nada nuevo):

- `MessageConfig` en `shared-kernel/.../config/i18n/MessageConfig.java:14`
  con basename `i18n/messages` y `setUseCodeAsDefaultMessage(true)`.
- `messages.properties` y `messages_es.properties` con secciones hasta la
  línea 73 (`usrtypetrans.*`); las nuevas claves se agregan al final.
- Patrón de test de referencia:
  `user-auth/.../dto/usertype/UserTypeDtoValidationIT.java:29` con
  `@SuppressWarnings("null")`, `Validator` inyectado y `Locale.of`.

## Convención de claves

Patrón `<dominio>.<campo>.<regla>` en minúsculas, sin guiones:

| DTO | Clave | Uso |
| --- | ----- | --- |
| CreateUserDto | `usr.username.blank` | @NotBlank en username |
| CreateUserDto | `usr.username.size` | @Size en username |
| CreateUserDto | `usr.password.blank` | @NotBlank en password |
| CreateUserDto | `usr.password.size` | @Size en password |
| CreateUserDto | `usr.password.pattern` | @Pattern en password |
| CreateUserDto | `usr.email.blank` | @NotBlank en email |
| CreateUserDto | `usr.email.format` | @Email en email |
| CreateUserDto | `usr.email.size` | @Size en email |
| CreateUserDto | `usr.profile.null` | @NotNull en profile |
| UserProfileDto | `usrprof.firstname.blank` | @NotBlank en firstName |
| UserProfileDto | `usrprof.firstname.size` | @Size en firstName |
| UserProfileDto | `usrprof.lastname.blank` | @NotBlank en lastName |
| UserProfileDto | `usrprof.lastname.size` | @Size en lastName |
| UserProfileDto | `usrprof.email.blank` | @NotBlank en email |
| UserProfileDto | `usrprof.email.size` | @Size en email |
| LanguageDto | `lang.code.blank` | @NotBlank en code |
| LanguageDto | `lang.code.size` | @Size en code |
| LanguageDto | `lang.code.pattern` | @Pattern en code |
| LanguageDto | `lang.name.blank` | @NotBlank en name |
| LanguageDto | `lang.name.size` | @Size en name |
| LanguageDto | `lang.isdefault.null` | @NotNull en isDefault |
| LanguageDto | `lang.active.null` | @NotNull en active |
| PermissionDto | `perm.code.blank` | @NotBlank en code |
| PermissionDto | `perm.code.pattern` | @Pattern en code |
| PermissionDto | `perm.name.blank` | @NotBlank en name |
| PermissionDto | `perm.active.null` | @NotNull en active |

## Archivos afectados

| Archivo | Línea(s) | Cambio |
| ------- | -------- | ------ |
| `user-auth/src/main/java/com/icms/user_auth/dto/user/CreateUserDto.java` | 13, 14, 17, 18, 19, 22, 23, 24, 27 | Reemplazar 9 literales por claves `{...}` |
| `user-auth/src/main/java/com/icms/user_auth/dto/userprofile/UserProfileDto.java` | 13, 14, 16, 17, 22, 23 | Reemplazar 6 literales por claves `{...}` |
| `user-auth/src/main/java/com/icms/user_auth/dto/language/LanguageDto.java` | 10, 11, 12, 14, 15, 17, 19 | Reemplazar 7 literales por claves `{...}` |
| `user-auth/src/main/java/com/icms/user_auth/dto/permission/PermissionDto.java` | 10, 11, 13, 15 | Reemplazar 4 literales por claves `{...}` |
| `shared-kernel/src/main/resources/i18n/messages.properties` | 73 (agregar al final) | Agregar 26 claves en inglés |
| `shared-kernel/src/main/resources/i18n/messages_es.properties` | 73 (agregar al final) | Agregar 26 claves en español |
| `user-auth/src/test/java/com/icms/user_auth/dto/user/CreateUserDtoValidationIT.java` | nuevo | IT i18n con `Validator` inyectado (18 tests) |
| `user-auth/src/test/java/com/icms/user_auth/dto/userprofile/UserProfileDtoValidationIT.java` | nuevo | IT i18n con `Validator` inyectado (12 tests) |
| `user-auth/src/test/java/com/icms/user_auth/dto/language/LanguageDtoValidationIT.java` | nuevo | IT i18n con `Validator` inyectado (14 tests) |
| `user-auth/src/test/java/com/icms/user_auth/dto/permission/PermissionDtoValidationIT.java` | nuevo | IT i18n con `Validator` inyectado (8 tests) |

## Cambios paso a paso

### Paso 1: Modificar CreateUserDto

En `CreateUserDto.java:13-27`, reemplazar cada literal por su clave:

```java
@NotBlank(message = "{usr.username.blank}")
@Size(min = 3, max = 50, message = "{usr.username.size}")
String username,

@NotBlank(message = "{usr.password.blank}")
@Size(min = 6, max = 100, message = "{usr.password.size}")
@Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*\\-._])(?=.*[a-zA-Z])(?=.*[A-Z]).+$", message = "{usr.password.pattern}")
String password,

@NotBlank(message = "{usr.email.blank}")
@Email(message = "{usr.email.format}")
@Size(max = 100, message = "{usr.email.size}")
String email,

@NotNull(message = "{usr.profile.null}")
@Valid
UserProfileDto profile
```

No cambiar regex, límites ni el `@Valid` anidado.

### Paso 2: Modificar UserProfileDto

En `UserProfileDto.java:13-23`, reemplazar cada literal por su clave:

```java
@NotBlank(message = "{usrprof.firstname.blank}")
@Size(min = 2, max = 50, message = "{usrprof.firstname.size}")
String firstName,
@NotBlank(message = "{usrprof.lastname.blank}")
@Size(min = 2, max = 50, message = "{usrprof.lastname.size}")
String lastName,
@NotBlank(message = "{usrprof.email.blank}")
@Size(max = 100, message = "{usrprof.email.size}")
String email
```

No validar `avatarUrl`, `contact` ni `prefs` (hoy sin anotaciones).

### Paso 3: Modificar LanguageDto

En `LanguageDto.java:10-19`, reemplazar cada literal por su clave:

```java
@NotBlank(message = "{lang.code.blank}")
@Size(min = 2, max = 10, message = "{lang.code.size}")
@Pattern(regexp = "^[a-z]{2,3}(-[A-Z]{2})?$", message = "{lang.code.pattern}")
String code,
@NotBlank(message = "{lang.name.blank}")
@Size(min = 1, max = 100, message = "{lang.name.size}")
String name,
@NotNull(message = "{lang.isdefault.null}")
Boolean isDefault,
@NotNull(message = "{lang.active.null}")
Boolean active
```

No cambiar el regex BCP 47.

### Paso 4: Modificar PermissionDto

En `PermissionDto.java:10-15`, reemplazar cada literal por su clave:

```java
@NotBlank(message = "{perm.code.blank}")
@Pattern(regexp = "^[A-Z]{2,}$", message = "{perm.code.pattern}")
String code,
@NotBlank(message = "{perm.name.blank}")
String name,
@NotNull(message = "{perm.active.null}")
Boolean active
```

Conservar el comentario de negocio de la línea 9.

### Paso 5: Agregar claves en messages.properties

Agregar al final del archivo, después de la línea 73:

```properties
# CreateUser Validation
usr.username.blank=Username cannot be blank
usr.username.size=Username must be between 3 and 50 characters
usr.password.blank=Password cannot be blank
usr.password.size=Password must be between 6 and 100 characters
usr.password.pattern=Password must include at least one number, one special character, one letter and one uppercase letter
usr.email.blank=Email cannot be blank
usr.email.format=Invalid email format
usr.email.size=Email must be at most 100 characters
usr.profile.null=User profile cannot be null

# UserProfile Validation
usrprof.firstname.blank=First name must not be blank
usrprof.firstname.size=First name must be between 2 and 50 characters
usrprof.lastname.blank=Last name must not be blank
usrprof.lastname.size=Last name must be between 2 and 50 characters
usrprof.email.blank=Email must not be blank
usrprof.email.size=Email must be at most 100 characters

# Language Validation
lang.code.blank=Code cannot be blank
lang.code.size=Code must be between 2 and 10 characters
lang.code.pattern=Code must follow BCP 47 format (e.g. 'es', 'es-ES')
lang.name.blank=Name cannot be blank
lang.name.size=Name must be between 1 and 100 characters
lang.isdefault.null=isDefault cannot be null
lang.active.null=Active cannot be null

# Permission Validation
perm.code.blank=Code must not be blank
perm.code.pattern=Code must be at least 2 uppercase letters
perm.name.blank=Name must not be blank
perm.active.null=Active cannot be null
```

Nota: `usr.email.format` normaliza el literal actual `"invalid format"`
(minúsculas, sin sujeto) a `"Invalid email format"`; el texto en inglés
queda cubierto por la nueva clave y no hay tests previos que aserten el
literal antiguo.

### Paso 6: Agregar claves en messages_es.properties

Agregar al final del archivo, después de la línea 73:

```properties
# CreateUser Validation
usr.username.blank=El nombre de usuario no debe estar vacío
usr.username.size=El nombre de usuario debe tener entre 3 y 50 caracteres
usr.password.blank=La contraseña no debe estar vacía
usr.password.size=La contraseña debe tener entre 6 y 100 caracteres
usr.password.pattern=La contraseña debe incluir al menos un número, un carácter especial, una letra y una mayúscula
usr.email.blank=El correo no debe estar vacío
usr.email.format=Formato de correo inválido
usr.email.size=El correo debe tener como máximo 100 caracteres
usr.profile.null=El perfil de usuario no puede ser nulo

# UserProfile Validation
usrprof.firstname.blank=El nombre no debe estar vacío
usrprof.firstname.size=El nombre debe tener entre 2 y 50 caracteres
usrprof.lastname.blank=El apellido no debe estar vacío
usrprof.lastname.size=El apellido debe tener entre 2 y 50 caracteres
usrprof.email.blank=El correo no debe estar vacío
usrprof.email.size=El correo debe tener como máximo 100 caracteres

# Language Validation
lang.code.blank=El código no debe estar vacío
lang.code.size=El código debe tener entre 2 y 10 caracteres
lang.code.pattern=El código debe seguir el formato BCP 47 (ej. 'es', 'es-ES')
lang.name.blank=El nombre no debe estar vacío
lang.name.size=El nombre debe tener entre 1 y 100 caracteres
lang.isdefault.null=isDefault no puede ser nulo
lang.active.null=Activo no puede ser nulo

# Permission Validation
perm.code.blank=El código no debe estar vacío
perm.code.pattern=El código debe tener al menos 2 letras mayúsculas
perm.name.blank=El nombre no debe estar vacío
perm.active.null=Activo no puede ser nulo
```

### Paso 7: Crear los cuatro IT de validación

Crear un `*ValidationIT.java` por DTO con el patrón de
`UserTypeDtoValidationIT.java:29-64`:

- `@SpringBootTest` + `@ActiveProfiles("test")` + `@SuppressWarnings("null")`.
- `Validator` inyectado con `@Autowired`.
- Helpers `validate(dto)` y
  `getMessageForPropertyAndAnnotation(violations, property, annotation)`.
- Locales `Locale.US` y `Locale.of("es", "ES")`; Javadoc e identificadores
  en inglés según copilot §3.

Detalle por archivo:

- `CreateUserDtoValidationIT`: 18 tests (9 reglas × 2 idiomas). Para aislar
  cada campo, construir el DTO con el resto de campos válidos y un
  `UserProfileDto` válido en `profile` (salvo el test de `profile null`,
  que pasa `null`). El test de `@Email` usa un email sintácticamente
  inválido (ej. `"not-an-email"`) con `@NotBlank`/`@Size` satisfechos.
- `UserProfileDtoValidationIT`: 12 tests (6 reglas × 2 idiomas), DTO válido
  base con `firstName`, `lastName` y `email` correctos.
- `LanguageDtoValidationIT`: 14 tests (7 reglas × 2 idiomas). El test de
  `code pattern` usa `"XX"` (falla BCP 47 por mayúsculas sin guion válido)
  con `@NotBlank`/`@Size` satisfechos; el de `size` usa `"a"` o un código
  de 11 caracteres.
- `PermissionDtoValidationIT`: 8 tests (4 reglas × 2 idiomas). El test de
  `code pattern` usa `"ab"` (minúsculas) con `@NotBlank` satisfecho.

## Riesgos y cómo mitigarlos

| Riesgo | Mitigación |
| ------ | ---------- |
| Colisión de claves con secciones existentes | Prefijos nuevos (`usr.*`, `usrprof.*`, `lang.*`, `perm.*`) no usados en líneas 53-73; verificar con búsqueda antes de agregar. |
| Cambio de texto en `@Email` de CreateUser (`"invalid format"` → clave) | La clave inglesa conserva el sentido; si algún test antiguo aserta el literal, actualizarlo a la clave resuelta en inglés. |
| Validación anidada `@Valid profile` resuelve locale distinto | `Validator` inyectado propaga el `LocaleContextHolder` al grafo anidado; el IT de `profile null` no depende del anidado. |
| Falsos positivos por violar dos reglas a la vez en el mismo campo | Seguir el patrón de referencia: filtrar por `propertyPath` + tipo de anotación en el helper, y construir DTOs que solo violen la regla bajo test. |
| Romper DTOs ya migrados | No tocar `UserTypeDto`, `UserStatusDto` ni traducciones; solo agregar secciones nuevas al final de los bundles. |

## Tests que deben ejecutarse para validar

1. **Compilación:** `./gradlew :user-auth:compileJava :shared-kernel:compileJava`
2. **Tests del módulo user-auth:** `./gradlew :user-auth:test`
3. **Tests específicos i18n (4 archivos nuevos):**
   - `./gradlew :user-auth:test --tests "*CreateUserDtoValidationIT*"`
   - `./gradlew :user-auth:test --tests "*UserProfileDtoValidationIT*"`
   - `./gradlew :user-auth:test --tests "*LanguageDtoValidationIT*"`
   - `./gradlew :user-auth:test --tests "*PermissionDtoValidationIT*"`
4. **Tests de shared-kernel (bundles):** `./gradlew :shared-kernel:test`
5. **Regresión i18n previa:** `./gradlew :user-auth:test --tests "*UserTypeDtoValidationIT*" --tests "*UserStatusDtoValidationIT*" --tests "*UserStatusTranslationDtoValidationIT*" --tests "*UserTypeTranslationDtoValidationIT*"`
