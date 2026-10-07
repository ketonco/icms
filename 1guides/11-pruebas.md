# Guía de pruebas del monorepo ICMS

Esta guía explica cómo elegir, estructurar y ejecutar pruebas en `shared-kernel`,
`user-auth` y `api`. Está basada en los tests actuales del proyecto; las pruebas
genéricas heredadas solo se repiten con un módulo representativo cuando así lo
indica `.github/copilot-instructions.md` §I.

## 1. Elegir el tipo de prueba

| Qué se verifica | Tipo | Ejemplo actual |
| --- | --- | --- |
| Una regla o transformación Java sin Spring ni BD | Unitaria | `UserRulesTest`, `LanguageMapperTest` |
| Servicio coordinando mocks | Unitaria de servicio | `UserProfileServiceTest` |
| Query real, persistencia o constraint | Integración de repositorio | `LanguageRepositoryTest` |
| DTO y sus restricciones | Validación directa | `CreateUserDtoValidationIT` |
| HTTP, filtros, serialización y errores | Integración MVC | `UserAuthGlobalExceptionsTest` |
| Resolución real de mensajes dentro del contexto Spring | Integración i18n | `MessagesI18nIntegrationTest` |
| Enrutamiento reactivo del Gateway | Integración WebFlux con downstream simulado | `UserAuthGatewayRoutingIntegrationTest` |

El nombre `*IT` no hace que Gradle ejecute automáticamente una suite separada:
estas clases están bajo `src/test` y usan el source set de pruebas del módulo.

## 2. Dependencias disponibles

Las versiones se administran en `gradle/libs.versions.toml`. En los módulos se
usan aliases, no coordenadas con versión.

- `spring-boot-starter-test`: JUnit Jupiter, AssertJ, Mockito y utilidades de
  Spring Test. Ya se usa como dependencia de test de `shared-kernel`.
- `junit-platform`: launcher para que Gradle y el IDE descubran las pruebas;
  configurado como `testRuntimeOnly`.
- `instancio-junit`: crear entidades y DTOs de prueba; `Select.field(...)`
  permite fijar campos relevantes. Declarado en `user-auth`.
- `mockito-core`: mocks y verificaciones cuando no basta con el soporte del
  starter. Declarado en `user-auth`.
- `spring-boot-starter-data-jpa-test` y
  `spring-boot-test-autoconfigure`: `@DataJpaTest`,
  `TestEntityManager` y soporte de configuración JPA; declarados mediante las
  convenciones de `user-auth`.
- `spring-boot-starter-webmvc-test`: soporte de pruebas MVC. Para HTTP real,
  los tests actuales usan además `rest-assured` y `rest-assured-mockmvc`.
- `spring-security-test`: utilidades para probar seguridad si el caso lo
  requiere.
- `wiremock-standalone`: servidor HTTP simulado para el test del Gateway.
- `spring-boot-starter-webflux`: solo corresponde en pruebas que necesiten
  `WebTestClient`. El Gateway es WebFlux; en `user-auth`, que es MVC, no se
  debe añadir salvo que una prueba use ese cliente.

Antes de agregar otra dependencia, comprueba si el plugin de convención ya la
proporciona y si el alias existe en el catálogo.

## 3. Estructura básica y AAA

Organiza cada caso en tres pasos:

1. **Arrange:** construye datos válidos y prepara mocks, Locale o servidor stub.
2. **Act:** ejecuta una sola operación o petición.
3. **Assert:** comprueba el resultado observable y, cuando aplique, las
   interacciones importantes.

Nombra el método por el comportamiento esperado, por ejemplo
`canCreateRejectsExistingEmail`. Usa `@DisplayName` para expresar el escenario.
Las aserciones deben comprobar resultados relevantes, no limitarse a que no
ocurra una excepción si también se puede verificar el código, estado o datos.

## 4. `shared-kernel`: explicación de cada test

Los tres tests de `shared-kernel/src/test/java/com/icms/shared/i18n` comprueban
el contrato común de los bundles i18n. No levantan Spring ni acceden a una BD.
Leen recursos del classpath y verifican formato o comportamiento observable.

### 4.1 `MessagesBundleStructureTest`

Lee como texto UTF-8 `i18n/messages.properties` y
`i18n/messages_es.properties`. Sus trece métodos protegen distintas partes de
un único contrato estructural:

- `bothFilesHaveSameLineCount`: obliga a que ambos archivos conserven la misma
  cantidad de líneas.
- `sectionCommentsMatchLineByLine`: exige comentarios en las mismas posiciones
  y con el mismo texto; así los encabezados y el orden visual se mantienen en
  paralelo.
- `sectionsFollowTheStandardOrder`: comprueba que estén las 16 secciones
  previstas y en el orden definido por la lista `STANDARD_SECTIONS`.
- `sectionCommentsFollowStandardFormat`: verifica el patrón del encabezado,
  por ejemplo `# User Errors`.
- `keysAppearInSameOrder`: evita que el orden de las claves difiera entre
  idiomas.
- `noDuplicateKeys`: detecta una clave repetida dentro de cualquiera de los dos
  bundles.
- `businessCodesMatchPattern`: valida los códigos de negocio, como `Ent-001`.
- `businessNumberingIsSequentialPerPrefix`: agrupa por prefijo y exige una
  secuencia sin huecos desde `001`.
- `validationKeysMatchPattern`: exige claves de validación con forma
  `dominio.campo.regla`.
- `validationBlockFollowsBusinessBlock`: garantiza que los códigos de negocio
  aparezcan antes de las claves de validación.
- `placeholdersMatchBetweenLocales`: compara la lista y el orden de marcadores
  `{0}`, `{1}`, etc. para que las traducciones reciban los mismos argumentos.
- `noTrailingSpacesInSectionComments`: evita espacios sobrantes en encabezados.
- `allValuesAreNonBlank`: impide que una clave tenga un mensaje vacío.

Sus helpers separan líneas, reconocen comentarios y claves, extraen pares
clave/valor y agrupan números o placeholders. Si cambia la convención de i18n,
actualiza el estándar y sus expectativas de manera deliberada; no relajes una
aserción solo para hacer pasar una traducción mal formada.

### 4.2 `MessagesBundleCoverageTest`

Compara las claves de ambos bundles con las referencias encontradas en todos
los `.java` de `src/main/java` del repositorio.

- `everyReferencedCodeExistsInBundles`: extrae códigos de negocio escritos
  como literales y claves de validación declaradas como `message = "{...}"`.
  Exige que cada referencia exista en inglés y español.
- `everyBundleKeyIsReferencedOrReserved`: invierte la comprobación. Cada clave
  de los bundles debe aparecer en código de producción o estar añadida
  explícitamente a `RESERVED_MESSAGE_KEYS`.

El test busca el root Gradle subiendo desde `user.dir`, recorre módulos y
excluye directorios de salida como `build`. Esto evita que el resultado dependa
del módulo desde el que Gradle lanzó la prueba. Si aparece una clave huérfana,
decide si se elimina o si se reserva con motivo; no agregues reservas sin uso
intencional.

### 4.3 `MessageResolverLocaleTest`

Construye un `ResourceBundleMessageSource` equivalente al real: basename
`i18n/messages`, UTF-8 y fallback al código cuando no existe mensaje. Lo
inyecta en `MessageResolver` y fija el Locale directamente.

- `resolvesEnglishForUsLocale`: valida un mensaje conocido en `Locale.US`.
- `resolvesSpanishForEsLocale`: valida el mismo código en `es-ES`.
- `resolvesEveryBusinessCodeInBothLocales`: prueba cada código de la lista
  mediante `@ParameterizedTest` y `@MethodSource`; confirma que inglés y
  español devuelven texto y no el código literal.
- `unknownCodeFallsBackToTheCode`: fija el contrato de fallback para un código
  inexistente.
- `tearDown` llama `LocaleContextHolder.resetLocaleContext()` para impedir que
  el Locale de un caso contamine otro.

Este test no carga el contexto completo de Spring; prueba el resolver y su
fuente de mensajes en aislamiento. La integración del bean real se cubre en
`user-auth`.

### 4.4 Dependencias de `shared-kernel`

`shared-kernel/build.gradle` declara `testImplementation
libs.spring.boot.starter.test` y `testRuntimeOnly libs.junit.platform`.
Eso aporta JUnit, AssertJ y el runtime de descubrimiento. Los tests de bundles
usan APIs del JDK; `MessageResolverLocaleTest` usa Spring Context, ya presente
en el classpath de producción del módulo.

## 5. Pruebas unitarias sencillas en `user-auth`

### Mappers

Ubicación: `user-auth/src/test/java/.../mappers`.

Ejemplos: `LanguageMapperTest`, `UserMapperTest`,
`UserProfileMapperTest`, `UserStatusMapperTest` y
`UserStatusTranslationMapperTest`.

Patrón:

1. Obtener el mapper generado con `Mappers.getMapper(...)`; no levantar Spring.
2. Construir DTO y entidad con datos controlados.
3. Verificar cada campo relevante para entidad → DTO, DTO → entidad o
   actualización in-place.
4. Para mappings con defaults, roles o campos sensibles, verificar también
   que el campo se conserve, derive o quede excluido según el contrato.

Dependencias: JUnit y AssertJ del starter de pruebas; MapStruct está en el
classpath de producción. Instancio puede ayudar con datos, pero usa valores
explícitos para aquello que la aserción valida.

### Servicios

Ubicación: `user-auth/src/test/java/.../service`.

Ejemplos: `LanguageServiceTest`, `UserStatusTranslationServiceTest`,
`UserProfileServiceTest` y `UserServiceTest`.

Patrón:

1. `@ExtendWith(MockitoExtension.class)`.
2. `@Mock` para repositorios, mapper, reglas y otros colaboradores.
3. `@InjectMocks` para el servicio.
4. Preparar resultados con `when(...).thenReturn(...)`.
5. Invocar el método público del servicio.
6. Afirmar resultado/cambios y verificar llamadas externas relevantes.

No levantes contexto Spring para lógica de servicio aislada. Usa Instancio si
necesitas entidades completas; fija con `Select.field(...)` los valores que
determinan la rama del caso. Para creación de usuarios, comprueba además
contraseñas codificadas, rol y estado asignados, y que la respuesta no exponga
la contraseña.

Dependencias: JUnit, Mockito, AssertJ e Instancio. En el catálogo están
`mockito-core` e `instancio-junit`; revisa las dependencias heredadas de
convención antes de declararlas otra vez.

### Reglas de negocio

Ubicación: `user-auth/src/test/java/.../rules`.

Ejemplos: `LanguageRulesTest`, `UserRulesTest`, `UserProfileRulesTest` y
`UserStatusTranslationRulesTest`.

Patrón:

- Caso permitido: prepara repositorios sin conflicto, ejecuta la regla,
  afirma que no se lanza excepción y verifica consultas relevantes.
- Caso rechazado: prepara el conflicto, afirma el tipo de excepción y su código
  de negocio con AssertJ.
- Añade casos para límites o ramas propias de la regla; no repitas validaciones
  genéricas heredadas que ya tengan cobertura representativa.

Dependencias: JUnit, Mockito, AssertJ e Instancio; no requieren Spring ni BD.

## 6. Pruebas con integración o lógica más compleja

### Repositorios JPA

Ejemplos: `LanguageRepositoryTest` y `UserProfileRepositoryTest`.

Usa `@DataJpaTest`, `@ActiveProfiles("test")`,
`@Import(AuditConfig.class)` y
`@AutoConfigureTestDatabase(replace = Replace.NONE)` como en los tests
actuales. Persiste con `TestEntityManager.persistAndFlush`, limpia el contexto
con `clear()` si vas a volver a consultar y entonces usa el repositorio real.
Afirma la query propia y los datos devueltos. Los datos deben respetar FKs y
constraints reales. No uses Instancio para inventar el ID de una entidad
autoincremental: déjalo en `null` o límpialo antes de persistir.

Dependencias: starters de test JPA/autoconfigure declarados por las
convenciones del servicio; perfil `test` y base PostgreSQL de pruebas
configurada. Estos tests no sustituyen pruebas unitarias de reglas o servicios.

### Validación directa de DTO

Ejemplos: los `*DtoValidationIT` bajo `user-auth/src/test/java/.../dto`.

Inyecta `jakarta.validation.Validator` en un `@SpringBootTest` con perfil `test`.
Construye un DTO válido y modifica un solo campo por caso. Ejecuta
`validator.validate(dto)` y afirma la propiedad, restricción y mensaje
esperados. Para validación anidada, incluye el objeto padre válido y provoca
el error en el campo hijo. Para i18n, fija y restablece el Locale y comprueba
el mensaje exacto en ambos idiomas.

Usa este patrón cuando quieras probar las restricciones y sus bundles sin
depender de que el controlador exponga un endpoint de escritura. Reserva
RestAssured para probar el contrato HTTP cuando el endpoint sí exista.

Dependencias: Spring Boot Test, Bean Validation y AssertJ. No hace falta
RestAssured para validar directamente un DTO.

### Controladores HTTP y excepciones

`UserControllerTest`, `UserAuthGlobalExceptionsTest` y
`TestControllerIntegrationTest` levantan MVC con
`@SpringBootTest(webEnvironment = RANDOM_PORT)`, perfil `test` y RestAssured.

En cada petición afirma el estado HTTP y los campos sustantivos del cuerpo
`RestResponse`. Para errores, comprueba también el código de negocio y que el
mensaje no esté vacío. Limpia los datos insertados para que los casos sean
repetibles. Usa un servidor real aleatorio del test, no un puerto fijo.

`MessagesI18nIntegrationTest` también usa el contexto y HTTP para confirmar que
la `MessageSource` real resuelve códigos en español e inglés y que los errores
de negocio llegan traducidos por el handler global. Como crea datos persistidos,
su setup y cleanup forman parte del aislamiento del test.

Dependencias: `spring-boot-starter-test`, soporte de test MVC y `rest-assured`.
Añade `spring-security-test` solo si necesitas autenticar o simular usuarios.
La dependencia `spring-boot-starter-webflux` es exclusiva de un caso que use
`WebTestClient`; no la agregues a `user-auth` por costumbre.

### Gateway WebFlux

El Gateway usa `WebTestClient` y un downstream aislado con WireMock y puerto
dinámico. La configuración se inyecta antes de iniciar el contexto mediante
`@DynamicPropertySource`; los stubs se restablecen entre casos.

La guía [Tests del gateway](10-gateway-tests.md) detalla el ciclo de vida del
stub, las propiedades dinámicas, los casos HTTP y las opciones descartadas.
No agregues MVC, JPA ni un servicio externo vivo a las pruebas del módulo `api`.

## 7. Tests de contexto y comandos

`ApiApplicationTests` y `UserAuthApplicationTests` solo comprueban que el
contexto arranque. Son pruebas de humo; no reemplazan pruebas de reglas,
servicios ni endpoints.

Desde PowerShell, ejecuta el módulo completo:

```powershell
.\gradlew.bat :shared-kernel:test
.\gradlew.bat :user-auth:test
.\gradlew.bat :api:test
```

Ejecuta una clase concreta:

```powershell
.\gradlew.bat :shared-kernel:test --tests "com.icms.shared.i18n.MessageResolverLocaleTest"
```

O toda la suite:

```powershell
.\gradlew.bat test
```

El perfil `test` desactiva Liquibase, sesión y caché según la configuración
actual. Los tests JPA necesitan su base de pruebas accesible. Mantén los
tests aislados y deterministas; no dependas de orden, datos de desarrollo ni
servicios externos.

## 8. Checklist para un test nuevo

- [ ] Elegí unitario, repositorio, validación directa o integración HTTP según
  el comportamiento que quiero observar.
- [ ] Cubrí resultado esperado y al menos una rama de error si tiene lógica
  propia.
- [ ] Preparé datos explícitos en los campos que determinan las aserciones.
- [ ] Afirmé estado, código, campos o interacciones relevantes.
- [ ] No repetí cobertura de una clase genérica heredada sin comportamiento
  específico.
- [ ] Usé solo dependencias necesarias y aliases del catálogo central.
- [ ] Aislé Locale, datos persistidos, stubs y estado estático entre casos.
- [ ] Revisé que el módulo conserve su separación MVC/WebFlux.
