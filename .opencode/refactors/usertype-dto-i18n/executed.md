# Ejecución del plan: i18n para UserTypeDto

## Resumen

Se reemplazaron los 4 mensajes de validación inline de `UserTypeDto` por claves de propiedad
resueltas a través del `MessageSource` existente, permitiendo que los mensajes de validación
de Bean Validation se internacionalicen según el header `Accept-Language`.

## Archivos modificados

### 1. `user-auth/src/main/java/com/icms/user_auth/dto/usertype/UserTypeDto.java`

**Líneas modificadas:** 9, 11, 13, 15

**Cambios:**

- `@NotBlank(message = "Code must not be blank")` → `@NotBlank(message = "{usrtype.code.blank}")`
- `@Pattern(regexp = "^[A-Z]{3}$", message = "Code must be 3 uppercase letters")` → `@Pattern(regexp = "^[A-Z]{3}$", message = "{usrtype.code.pattern}")`
- `@NotBlank(message = "Name must not be blank")` → `@NotBlank(message = "{usrtype.name.blank}")`
- `@NotNull (message = "Active cannot be null")` → `@NotNull(message = "{usrtype.active.null}")`

**Nota:** Se corrigió también el espaciado en `@NotNull (message = ...)` → `@NotNull(message = ...)`.

### 2. `shared-kernel/src/main/resources/i18n/messages.properties`

**Líneas agregadas:** 53-57 (nueva sección al final)

```properties
# UserType Validation
usrtype.code.blank=Code must not be blank
usrtype.code.pattern=Code must be 3 uppercase letters
usrtype.name.blank=Name must not be blank
usrtype.active.null=Active cannot be null
```

### 3. `shared-kernel/src/main/resources/i18n/messages_es.properties`

**Líneas agregadas:** 53-57 (nueva sección al final)

```properties
# UserType Validation
usrtype.code.blank=El código no debe estar vacío
usrtype.code.pattern=El código debe tener 3 letras mayúsculas
usrtype.name.blank=El nombre no debe estar vacío
usrtype.active.null=Activo no puede ser nulo
```

### 4. `user-auth/src/test/java/com/icms/user_auth/dto/usertype/UserTypeDtoValidationIT.java` (NUEVO)

**Archivo creado:** Test de integración que valida la i18n de mensajes de validación.

**Estructura:**

- `@SpringBootTest` + `@ActiveProfiles("test")`
- Inyecta `Validator` de Bean Validation
- 8 tests que cubren cada campo (code blank, code pattern, name blank, active null) en ambos idiomas (en-US, es-ES)
- Patrón AAA (Arrange, Act, Assert)
- Helper `getMessageForPropertyAndAnnotation()` para filtrar violaciones por propiedad y tipo de anotación

## Archivos que quedaron igual

- `shared-kernel/src/main/java/com/icms/shared/config/i18n/MessageConfig.java` — No requiere cambios. El `MessageSource` ya está configurado correctamente con `setUseCodeAsDefaultMessage(true)`.
- `shared-kernel/src/main/java/com/icms/shared/utils/MessageResolver.java` — No requiere cambios.
- `user-auth/src/main/java/com/icms/user_auth/controller/UserTypeController.java` — No se modificó (ver decisión abajo).
- Otros DTOs del proyecto — No se tocaron, respetando el alcance del plan.

## Decisiones que el plan no cubría

### 1. No existe endpoint POST para UserType

**Problema:** El plan sugería un test de integración que envía requests HTTP `POST /api/user-types`, pero `UserTypeController` solo implementa `ReadController` (GET). No existe endpoint POST para crear UserTypes.

**Decisión:** En lugar de agregar `WriteController` al controlador (cambio fuera del alcance del plan), se creó un test de integración que valida directamente el `UserTypeDto` usando el `Validator` de Bean Validation inyectado por Spring. Esto permite verificar que los mensajes se resuelvan correctamente según el `Locale` sin depender de un endpoint HTTP que no existe.

**Justificación:** El objetivo del plan es validar la i18n de mensajes de validación, no probar el endpoint POST. El test propuesto cumple este objetivo de forma más directa y sin requerir cambios adicionales en el controlador.

### 2. Filtrado de violaciones por tipo de anotación

**Problema:** Cuando `code=""` (vacío), se generan dos violaciones para la misma propiedad: una de `@NotBlank` y otra de `@Pattern`. Un simple `findFirst()` no garantiza cuál se obtiene primero.

**Decisión:** Se implementó un helper `getMessageForPropertyAndAnnotation()` que filtra por propiedad Y tipo de anotación (`NotBlank.class`, `Pattern.class`, `NotNull.class`).

**Justificación:** Asegura que el test verifique el mensaje correcto para cada validación, independientemente del orden en que Bean Validation las evalúe.

## Tests ejecutados

### Test específico de i18n

```bash
./gradlew :user-auth:test --tests "*UserTypeDtoValidationIT*"
```

**Resultado:** ✅ BUILD SUCCESSFUL — 8 tests passed

### Tests de shared-kernel

```bash
./gradlew :shared-kernel:test
```

**Resultado:** ✅ BUILD SUCCESSFUL — No hay tests en shared-kernel (NO-SOURCE)

## Riesgos y mitigación aplicada

| Riesgo | Mitigación aplicada |
| ------ | ------------------- |
| El `LocalValidatorFactoryBean` no usa el `MessageSource` de Spring | ✅ Verificado: Spring Boot auto-configura el validador con el bean `MessageSource` existente. Los tests confirman que los mensajes se resuelven correctamente. |
| Falta de traducción en un idioma | ✅ `setUseCodeAsDefaultMessage(true)` ya está activo como red de seguridad. |
| Tests de integración fallan por dependencia de BD | ✅ El test propuesto no requiere BD: valida el DTO directamente sin persistir. |
| Romper otros DTOs | ✅ Este cambio solo toca `UserTypeDto`. No se modifican otros DTOs. |

## Notas didácticas

- La sintaxis `{clave}` en las anotaciones de Bean Validation le indica al `LocalValidatorFactoryBean` que debe resolver el mensaje desde el `MessageSource` en lugar de usar el texto literal.
- `LocaleContextHolder.getLocale()` se popula automáticamente desde el header `Accept-Language` de la request HTTP gracias a Spring MVC. En el test, se configura manualmente con `LocaleContextHolder.setLocale()`.
- El fallback `setUseCodeAsDefaultMessage(true)` es una red de seguridad: si alguien olvida agregar una traducción, al menos se ve la clave (ej. `usrtype.code.blank`) en lugar de una excepción `NoSuchMessageException`.
