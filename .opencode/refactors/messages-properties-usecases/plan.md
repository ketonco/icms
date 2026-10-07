# Plan: estandarización de `messages-properties` por casos de uso

- **Slug:** `messages-properties-usecases`
- **Ciclo:** Ciclo 2 (replan)
- **Fecha:** 2026-10-07
- **Estado:** **Ciclo 2 aprobado** por el desarrollador el 2026-10-07, con
  O5 (`@Bean` en `MessageConfig`), O6 (fallo P-31 fuera del DoD) y O7
  opción A (quitar las 12 aserciones de fallback `"<code> context"` y
  dejar solo las de `code`).
- **Causa del replan:** `MessageResolver` no se registra como bean en
  `user-auth`, así que su `messageSource` estático es `null` y todo
  `RestResponse.message` llega como `"<code> context"`: T4 falla en 4 de
  sus 5 métodos (`MessagesI18nIntegrationTest:87`, `:103`, `:128` y
  `:152`), además de `MessageResolver.java:6`,
  `UserAuthApplication.java:11` y `AutoConfiguration.imports:1-5`.

> Este plan solo escribe documentos. No se ejecuta ningún comando, edición
> de código ni prueba sin aprobación explícita del desarrollador.

En las rutas, `...` abrevia `src/main/java/com/icms/` en producción y
`src/test/java/com/icms/` en pruebas.

## Herencia del Ciclo 1 (ejecutado, no se vuelve a tocar)

- Fases 0 a 3 completas: dependencias de prueba en
  `shared-kernel/build.gradle:33-35`, 5 clases de test nuevas, los dos
  bundles reescritos (61 claves: 21 de negocio y 40 de validación), 16
  literales renumerados en `src/main` y 8 aserciones actualizadas en
  `src/test`.
- Resultado del Ciclo 1: `:shared-kernel:test` con 39 tests y 0 fallos;
  `:user-auth:test` con 130 tests y 5 fallos.
- Fase 4 documental ya hecha por el orquestador: `copilot-instructions.md`
  describe el estándar (`:64-65`) y lista `S-001`/`S-002`/`S-003` (`:78`);
  `pending.md` ya contiene P-29 y P-30.
- Quedan dos asuntos, tratados aquí: el bloqueo de `MessageResolver`
  (O5, trabajo de este ciclo) y el fallo preexistente de
  `UserControllerTest` (O6, fuera de alcance).

## Objetivo del Ciclo 2

Registrar `MessageResolver` como bean para que los 4 métodos fallidos de
T4 devuelvan los textos reales EN/ES, impedir que el test de creación de
T4 siga acumulando filas en la BD de pruebas, neutralizar las aserciones
de mensaje que dependen del estado estático (consecuencia directa de
registrar el bean) y dejar el Definition of Done acotado al fallo
preexistente ya identificado como P-31.

## Diagnóstico del bloqueo (solo lectura, verificado en el repo)

Cadena de la causa:

1. `shared-kernel/.../Utils/MessageResolver.java:6` declara la clase como
   `@Component`, pero vive en el paquete `com.icms.shared.Utils`.
2. `user-auth/.../UserAuthApplication.java:11` es
   `@SpringBootApplication` sin `scanBasePackages`, así que escanea solo
   `com.icms.user_auth`; la línea `:10` con
   `scanBasePackages = {"com.icms.user_auth", "com.icms.shared"}` está
   comentada.
3. `shared-kernel/.../META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports:1-5`
   lista `ExceptionAutoConfiguration`, `MessageConfig`, `AuditConfig`,
   `SharedJacksonConfig` y `EncryptEncoder`; no lista `MessageResolver`.
4. Consecuencia: nadie instancia `MessageResolver`, su campo estático de
   `MessageResolver.java:9` queda `null` y el fallback de
   `MessageResolver.java:21` devuelve `code + " context"`.
5. `shared-kernel/.../exceptions/BaseException.java:9` y `:24` resuelven
   el mensaje con ese estático, así que todo `RestResponse.message` de
   `user-auth` sale con el código en lugar del texto.

Evidencia del Ciclo 1 en
`user-auth/build/test-results/test/TEST-com.icms.user_auth.i18n.MessagesI18nIntegrationTest.xml`:
`Actual: Ent-001 context` (esperado `Entidad no encontrada.`),
`Actual: Cat-002 context` y `Actual: S-001 context`.

Dato clave para el fix: no hace falta tocar el escaneo de componentes ni
el fichero de imports, porque `MessageConfig` ya está declarado en
`AutoConfiguration.imports:2` y sus `@Bean` se crean en el arranque del
contexto.

## Decisiones

### O5 — registrar `MessageResolver` como `@Bean` (aprobada)

- **Único archivo de `src/main` que se toca en el Ciclo 2:**
  `shared-kernel/.../config/i18n/MessageConfig.java`.
- **Imports necesarios:** solo falta
  `import com.icms.shared.Utils.MessageResolver;`. `MessageSource` ya
  está importado en `MessageConfig.java:4` y `@Bean` en
  `MessageConfig.java:7`, así que no se agregan más imports.
- **Ubicación:** método nuevo después del cierre de `messageSource()` en
  `MessageConfig.java:24` y antes de la llave de cierre de la clase en
  `MessageConfig.java:26`.

```java
@Bean
public MessageResolver messageResolver(MessageSource messageSource) {
    return new MessageResolver(messageSource);
}
```

(Método con sangría de 4 espacios dentro de la clase.)

- **Elección de la inyección: por tipo (`MessageSource`), no llamando a
  `messageSource()`.** Razones:
  1. Sigue el patrón de
     `shared-kernel/.../config/exception/ExceptionAutoConfiguration.java:17-20`,
     donde `customSecurityExceptionHandler(ObjectMapper objectMapper)`
     recibe su colaborador por tipo.
  2. Spring resuelve el orden: `messageSource` es dependencia de
     `messageResolver`, así que se crea primero.
  3. Llamar a `messageSource()` desde el mismo `@Configuration` también
     funcionaría (`proxyBeanMethods = true` por defecto intercepta la
     llamada), pero acopla el método a la semi-instancia de la
     configuración y es menos explícito que la inyección.
  4. No hay ambigüedad de tipo: el contexto de `user-auth` tiene un único
     `MessageSource`. Lo prueba T4
     (`everyBusinessCodeResolvesInRealMessageSource` hace
     `@Autowired MessageSource` sin calificar y resuelve el basename
     `i18n/messages`); además el bean de `MessageSourceAutoConfiguration`
     se omite porque nuestra definición se llama `messageSource`.
- **Efecto esperado:** al arrancar el contexto, el constructor de
  `MessageResolver.java:12` guarda el `MessageSource` real y los 4
  métodos fallidos pasan a los textos del bundle:
  - `MessagesI18nIntegrationTest:87` → `Entidad no encontrada.`
    (404, `Ent-001`, `Accept-Language: es-ES`).
  - `MessagesI18nIntegrationTest:103` → `Entity not found.`
    (404, `Ent-001`, `Accept-Language: en-US`).
  - `MessagesI18nIntegrationTest:128` →
    `El código del catálogo debe ser único.` (400, `Cat-002`).
  - `MessagesI18nIntegrationTest:152` → `Entidad creada con éxito`
    (200, creación de idioma).
  - `MessagesI18nIntegrationTest:157` sigue en verde: usa el
    `MessageSource` del contexto, no `MessageResolver`.
- No se añade aserción de `code` al test de creación: `RestResponse.ok`
  (`RestResponse.java:24-32`) no popula ese campo, por eso `:149-152`
  solo afirma `status` y `message`.

### O6 — fallo preexistente fuera de alcance (aprobada)

- El fallo real es
  `user-auth/src/test/.../controller/UserControllerTest.java:72`: espera
  200 y recibe 400 por la fila residual `testuser2@example.com` más el
  defecto de rollback del tearDown (`UserControllerTest.java:40`).
- **No se arregla en este ciclo.** El orquestador lo registra en
  `pending.md` como **P-31** y sale del Definition of Done.
- Se reprodujo en la línea base del Ciclo 1 antes de cualquier cambio
  (mismo fallo, con y sin este alcance).

### O7 — neutralizar las 12 aserciones de fallback (propuesta)

Consecuencia detectada al planificar O5: registrar el bean pone el campo
estático de `MessageResolver` a no-nulo en el JVM de pruebas de
`user-auth` en el primer `@SpringBootTest`. Los tests unitarios (Mockito,
sin contexto) que hoy afirman el fallback `"<code> context"` recibirían
el mensaje real si corren después. El orden observado en la última
ejecución (timestamps de `user-auth/build/test-results/test/*.xml`) es
Spring primero (`UserControllerTest` 14:52:47 y
`MessagesI18nIntegrationTest` 14:52:50) y `rules`/`service` después
(14:52:57 y 14:52:58): los 12 fallarían con O5 aplicado.

Inventario completo (12 aserciones en 6 clases):

- `.../rules/LanguageRulesTest.java:79`, `:111`, `:130`
- `.../rules/UserProfileRulesTest.java:78`, `:125`, `:145`
- `.../rules/UserRulesTest.java:76`, `:98`
- `.../rules/UserStatusTranslationRulesTest.java:96`
- `.../service/UserProfileServiceTest.java:109`, `:133`
- `.../service/UserStatusTranslationServiceTest.java:76`

Opciones:

- **A) Quitar la dependencia del estado estático (recomendada).** Borrar
  la línea `.hasMessage("<código> context")` en las 11 aserciones que ya
  llevan `.extracting("code")` en la línea siguiente, y cambiar
  `assertEquals("Lan-002 context", e.getMessage())` por
  `assertEquals("Lan-002", e.getCode())` en
  `UserStatusTranslationServiceTest.java:76`. La aserción de `code` ya
  existente mantiene la prueba de renumeración; el contrato de fallback
  sigue cubierto por T3
  (`MessageResolverLocaleTest.unknownCodeFallsBackToTheCode`) y el
  contrato de mensaje real por T3 y T4, sin duplicar cobertura (principio
  selectivo §I). Resultado independiente del orden de ejecución.
- **B) Forzar el fallback en `@BeforeEach`** con `new MessageResolver(null)`
  en las 6 clases. Mantiene las 12 aserciones intactas, pero muta estado
  global compartido y crea una dependencia de orden nueva: una clase
  unitaria que corra entre dos clases Spring con el contexto cacheado
  dejaría el campo en `null` y rompería T4. Descartada.

## Archivos afectados en el Ciclo 2

### 1. Producción (`src/main`): solo uno

- `shared-kernel/.../config/i18n/MessageConfig.java` → **agrega** el
  import de `MessageResolver` como primer import, en la posición de
  `MessageConfig.java:3` (hoy `ResourceBundleMessageSource`).
- `shared-kernel/.../config/i18n/MessageConfig.java` → **agrega** el
  método `messageResolver(MessageSource)` con `@Bean`, justo después del
  cierre de `messageSource()` en `MessageConfig.java:24`.

### 2. Pruebas de `user-auth` (`src/test`)

- `user-auth/.../i18n/MessagesI18nIntegrationTest.java` → **agrega**
  `import org.junit.jupiter.api.AfterEach;` y
  `import com.icms.user_auth.repository.LanguageRepository;`, el campo
  `createdLanguageCode`, el campo `@Autowired LanguageRepository` junto a
  `messageSource` (`:65-66`), el método `@AfterEach` y la reforma de
  `createSuccessReturnsRenamedCode` (`:133-153`).
- Las 6 clases del inventario de O7 → **quitan** las 12 aserciones de
  fallback (solo si O7 se aprueba en la opción A).

### 3. Sin cambios

- `shared-kernel/.../Utils/MessageResolver.java`,
  `user-auth/.../UserAuthApplication.java` y el fichero
  `AutoConfiguration.imports`: intactos, como exige O5.
- Los dos bundles, los 16 literales del Ciclo 1, los 40 DTOs,
  `gradle/libs.versions.toml` y las migraciones Liquibase.
- Documentación: la Fase 4 del Ciclo 1 ya la hizo el orquestador
  (`copilot-instructions.md:64-65` y `:78`; `pending.md` P-29 y P-30) y
  P-31 también la registra él; ninguno cuenta como pendiente de este
  ciclo.

## Cambios paso a paso

### Fase 1: `@Bean` de `MessageResolver` (O5)

1. Agregar `import com.icms.shared.Utils.MessageResolver;` en
   `MessageConfig.java`, como primer import (después de la línea en
   blanco `:2`, antes de los `java.*` y `org.*`).
2. Insertar el método `messageResolver(...)` entre `MessageConfig.java:24`
   y `:26`.
3. Verificar por lectura que `MessageConfig.java` quedó con el import
   nuevo y el método `messageResolver(...)`, sin imports sobrantes y sin
   cambios en `messageSource()`.

### Fase 2: limpieza de la fila creada por T4

1. Agregar el campo de instancia `private String createdLanguageCode;`.
2. Agregar `@Autowired private LanguageRepository languageRepository;`
   junto al `MessageSource` existente (`:65-66`).
3. Agregar un `@AfterEach` que borre la fila si el código quedó
   guardado:

   ```java
   @AfterEach
   void deleteCreatedLanguage() {
       String code = createdLanguageCode;
       createdLanguageCode = null;
       if (code != null) {
           languageRepository.findByCode(code)
               .ifPresent(languageRepository::delete);
       }
   }
   ```

4. Reformar `createSuccessReturnsRenamedCode` (`:133-153`): extraer el
   código y el nombre a variables locales, asignar
   `createdLanguageCode = code;` **antes** de enviar el `POST` y pasar
   esas variables a `formatted` en lugar de llamar a los generadores en
   línea (`:141`). Así la limpieza funciona aunque el `POST` o el aserto
   fallen.

Justificación de esta alternativa:

- La limpieza debe ser explícita porque `@Transactional` en el test no
  ayudaría: el `POST` viaja por HTTP en otro hilo y otra conexión, y esa
  es exactamente la misma raíz del defecto de rollback descrito en P-31.
- Se descarta añadir `deleteByCode` a
  `user-auth/.../repository/LanguageRepository.java`: sería un segundo
  archivo de `src/main` y O5 acota el `src/main` del Ciclo 2 a
  `MessageConfig`.
- Se descarta `@Sql` o un truncate: borraría los idiomas seed que usan
  el resto de las pruebas.
- Se descarta un código fijo con limpieza en `@BeforeEach`: una fila
  residual de una corrida anterior haría fallar el `POST` con 400
  (`Cat-002`).
- Complemento recomendado en el mismo archivo: subir la entropía del
  código aleatorio de 2 a 3 minúsculas en `randomLanguageCode`
  (`:187-194`), dentro del patrón `^[a-z]{2,3}(-[A-Z]{2})?$` de
  `LanguageDto.java:12`: pasa de 456 976 a 11 881 376 combinaciones, lo
  que reduce el riesgo de colisión con las filas residuales ya
  acumuladas.
- Las filas residuales ya acumuladas por corridas anteriores **no se
  borran** en este ciclo (sería una limpieza manual de la BD de pruebas,
  fuera de alcance); con la entropía subida su impacto queda
  despreciable. Se puede dejar anotado en `pending.md` por el
  orquestador si lo considera oportuno.

### Fase 3: aislamiento de las 12 aserciones (O7, opción A)

1. En `LanguageRulesTest.java:79`, `:111`, `:130`,
   `UserProfileRulesTest.java:78`, `:125`, `:145`,
   `UserRulesTest.java:76`, `:98`,
   `UserStatusTranslationRulesTest.java:96`,
   `UserProfileServiceTest.java:109` y `:133`: **borrar** la línea
   `.hasMessage("<código> context")`, conservando `.isInstanceOf(...)` y
   `.extracting("code").isEqualTo("<código>")`, que es lo que prueba la
   renumeración.
2. En `UserStatusTranslationServiceTest.java:76`: **cambiar**
   `assertEquals("Lan-002 context", e.getMessage());` por
   `assertEquals("Lan-002", e.getCode());` (`BaseException.getCode()` en
   `BaseException.java:18-20`).
3. Verificar con una búsqueda que no quede ninguna cadena
   `" context"` en `user-auth/src/test` (hoy hay exactamente 12).

### Fase 4: verificación

Se ejecutan en este orden y solo con autorización explícita; conviene
aplicar primero las Fases 1 a 3 para que ninguna ejecución intermedia
deje filas residuales nuevas.

1. `gradlew.bat :shared-kernel:test` → esperado `BUILD SUCCESSFUL` con
   39 tests y 0 fallos (T1 = 13, T2 = 2, T3 = 24), sin cambios respecto
   al Ciclo 1.
2. `gradlew.bat :user-auth:test` → esperado 130 tests con **1 solo
   fallo**: `UserControllerTest.testCreateUser` (preexistente, P-31).
   En verde, con sus resultados esperados:
   - `MessagesI18nIntegrationTest` 5 de 5 (los 4 que estaban en rojo más
     `everyBusinessCodeResolvesInRealMessageSource`).
   - `LanguageRulesTest` 5, `UserProfileRulesTest` 5, `UserRulesTest` 3,
     `UserStatusTranslationRulesTest` 2, `UserProfileServiceTest` 4 y
     `UserStatusTranslationServiceTest` 2, todos con las aserciones de
     `code`.
   - Los 8 `*DtoValidationIT` (80 tests), `UserAuthGlobalExceptionsTest`
     (2), los 5 mappers (14), los 2 repositories (3),
     `UserAuthApplicationTests` (1) y el resto de services/controllers
     sin cambios.
3. `gradlew.bat build --continue` → compilación, jar y assemble en verde
   en los 3 módulos; el único rojo sigue siendo el test P-31 de
   `:user-auth:test`. Alternativa para verde total:
   `gradlew.bat build -x :user-auth:test` junto al paso 2.
4. Confirmar con una consulta de solo lectura a la BD de pruebas que una
   corrida de T4 ya no deja filas nuevas en `languages` (requiere
   autorización).
5. Confirmar cero warnings de markdownlint en los `.md` de
   `.opencode/refactors/`.
6. Registrar los resultados del Ciclo 2 en
   `.opencode/refactors/messages-properties-usecases/executed.md`.

### Fase 5: documentación

No aplica en este ciclo: la Fase 4 documental del Ciclo 1 ya la ejecutó
el orquestador y P-31 también la registra él.

## Riesgos y mitigación

- **R1 — Bean duplicado futuro.** Si se reactiva
  `UserAuthApplication.java:10` (`scanBasePackages` con `com.icms.shared`),
  el `@Component` de `MessageResolver.java:6` y el nuevo
  `@Bean messageResolver` competirían con el mismo nombre de bean; Spring
  Boot omite la definición de autoconfiguración cuando el usuario ya
  definió ese nombre de bean, así que ganaría el escaneo sin excepción,
  pero con dos fuentes de verdad. *Mitigación:* mantener `:10` comentada;
  si se reactiva, retirar `@Component` de `MessageResolver` y
  registrarlo en `pending.md`.
- **R2 — Las 12 aserciones `"<código> context"` se vuelven
  dependientes del orden** al registrarse el bean. *Mitigación:* Fase 3
  (O7).
- **R3 — Filas residuales de T4 en la BD de pruebas** ya acumuladas y
  potencialmente nuevas. *Mitigación:* Fase 2 (limpieza en `@AfterEach`
  más subida de entropía); las filas previas quedan fuera de alcance.
- **R4 — Orden de creación del bean.** Si algo invocara
  `resolveMessage` durante el refresh antes de crear el bean, vería el
  fallback solo en esa llamada. *Mitigación:* T4 se ejecuta después del
  refresh y afirma el texto real en los 4 métodos.
- **R5 — Locale de los mensajes.** El texto depende de
  `LocaleContextHolder` con la cabecera `Accept-Language`. *Mitigación:*
  T4 ya envía `es-ES` y `en-US` en las peticiones correspondientes y T3
  cubre ambos locales a nivel unitario.
- **R6 — Fallo preexistente P-31.** Fuera de alcance por O6; no cuenta
  para el Definition of Done.

## Definition of Done (acotado por O6)

- `gradlew.bat :shared-kernel:test` en verde: 39 tests y 0 fallos.
- `gradlew.bat :user-auth:test` con 129 de 130 tests en verde; el único
  fallo admisible es `UserControllerTest.testCreateUser`
  (`user-auth/src/test/.../controller/UserControllerTest.java:72`),
  documentado por el orquestador como P-31 y explícitamente fuera de
  este DoD.
- `MessagesI18nIntegrationTest` 5 de 5 con los textos reales EN/ES
  (`:87`, `:103`, `:128`, `:152`) y sin dejar filas nuevas en
  `languages` tras cada corrida.
- `gradlew.bat build --continue` sin errores de compilación, de jar ni
  de assemble en `:api`, `:shared-kernel` y `:user-auth`.
- Las 12 aserciones de fallback sustituidas por aserciones de `code` (si
  O7 se aprueba), con T3 cubriendo el contrato de fallback.
- `MessageResolver.java`, `UserAuthApplication.java` y
  `AutoConfiguration.imports` intactos; `MessageConfig.java` es el único
  archivo de `src/main` modificado en el Ciclo 2.
- Fase 4 documental del Ciclo 1 cumplida por el orquestador
  (`copilot-instructions.md:64-65` y `:78`; `pending.md` P-29 y P-30) y
  P-31 también registrada por él: ninguno de los dos queda como
  pendiente de este ciclo.
- Cero warnings de markdownlint en los `.md` de `.opencode/refactors/`.

## Decisión pendiente de aprobación

- **O7 — Aislamiento de las aserciones de fallback.** A) quitar las 12
  aserciones `"<código> context"` y dejar solo las de `code`
  (**recomendada**: elimina la dependencia del estado estático y no
  duplica la cobertura de T3/T4). B) forzar el fallback en `@BeforeEach`
  con `new MessageResolver(null)` (conserva las aserciones, pero muta
  estado global y añade una dependencia de orden con los contextos
  cacheados).

Las decisiones O5 y O6 ya están tomadas por el desarrollador y se
recogen aquí como aprobadas.
