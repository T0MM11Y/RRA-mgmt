# Copilot instructions for RRA-mgmt

This is a Spring Boot 3.4.x, Java 17 web app with server-side views (Thymeleaf) and JPA. It integrates with NT SSO, uses Jasypt for encrypted config values, and writes logs to the `logs/` tree.

## Build, run, debug

- Build: Maven is canonical. Tests are skipped by default (see `pom.xml` properties). Enable tests with `-Dmaven.test.skip=false` if needed.
- Run (profiles): Use Spring profiles `dev`, `uat`, `prod`. App commonly listens on 8443 with SSL. Provide `JASYPT_ENCRYPTOR_PASSWORD` when starting, or any `ENC(...)` values won’t decrypt.
- Hot reload: Devtools is enabled (`application.properties`). Java/resources reload; static assets use LiveReload.
- CSS lint: Node is used only for Stylelint via `package.json` (`npm run lint:css`).

## Architecture at a glance

- Web layer: `controller/` with MVC controllers returning views. Most controllers extend `BaseController` for common helpers (uniform `RespVo` responses, file download, URL builders). Example: `controller/LoginController.java`.
- Cross-cutting: `interceptor/RraInterceptor` enforces session auth and permission checks, handles redirect-after-login. `aspect/AccountActionHistoryAspect` records audit history around account actions.
- Service layer: Services extend `BaseService` for session-bound user info, config values, and common repository access. Example: `service/LoginService.java` assembles the per-user menu from role/account permissions.
- Persistence: JPA repositories under `persistence/repository`, entities under `persistence/entity`, DTOs under `persistence/dto`, and DAOs for complex queries. Example repo with JPQL/native: `persistence/repository/ProgramRepository.java`.
- Utilities: `utils/` contains shared helpers (e.g., `SpringUtils` for bean lookup, `JsonUtil`, `AESUtil`, mail/crypto/date helpers).
- Views & assets: Templates in `src/main/resources/templates`, static assets in `static/`. Thymeleaf is enabled; Freemarker is disabled in `application-*.properties`.

## AuthN, session, and permissions

- NT SSO: See `controller/SsoController` (entry), `ws/nt/*` types, and `application-*.properties` (`nt.sso.*`).
- Session model: `BaseService` pulls `UserInfoVo` from `HttpSession` via `RequestContextHolder`. `RraInterceptor` blocks unauthenticated requests and stores `redirectAfterLogin` to resume post-login.
- Authorization: Permissions are menu-driven. After login, `LoginService.findMenuVo()` builds menu from role/account permission repos and stores it in session. `RraInterceptor.isPermission()` authorizes by matching the request URI against session menu URLs.

## Configuration, environments, logging

- Profiles: `application-{dev,uat,prod}.properties` configure DB, SSL keystores, mail, SSO, and external APIs. Many secrets are `ENC(...)` and require `JASYPT_ENCRYPTOR_PASSWORD` (Jasypt starter is on the classpath).
- Logging: `logback-spring.xml` writes to `logs/{trace,debug,info,warn,error,fatal}` with daily rollovers; a `local` profile exists in logging. SQL logging is enabled in `dev/uat/prod` configs.
- Ports & SSL: Defaults to 8443 with SSL; dev uses `ssl/DevAP2KeyStore.jks` on the classpath.

## Conventions and patterns to follow

- Controllers should extend `BaseController` and return `RespVo` via the provided helpers for consistency (e.g., `getErrorResponse()`, `getSuccessResponse(msg)`).
- Services should extend `BaseService` and use its accessors for `accountId`, `roleId`, etc.; prefer injected repos, but `SpringUtils.getBean(...)` is used where DI is not straightforward.
- For XSS/static analysis concerns, existing code often runs objects through ESAPI encode/decode before rendering (see `LoginController.error`). Preserve that behavior when touching similar flows.
- Auditing: Account mutations are captured by `AccountActionHistoryAspect`; align new account-related operations with existing pointcuts or extend them.

## Useful entry points

- App bootstrap: `com.twm.mgmt.Application`.
- Request flow: `RraInterceptor` → Controllers (e.g., `LoginController`) → Services (e.g., `LoginService`) → Repositories (e.g., `ProgramRepository`).
- Error and file responses: `BaseController`.
- Session/user access: `BaseService`.

If any of the above is unclear (e.g., local SSO bypass for dev, keystore usage, or the expected Jasypt password), tell me what you’re trying to do and I’ll refine these instructions with the exact steps you need.
