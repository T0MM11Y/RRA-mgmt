# RRA Management Console

Management console for Taiwan Mobile's Rewards & Recognition platform. The application exposes internal tooling for account provisioning, permission management, customer-care lookups, and reward transaction operations. It is a Spring Boot 3 service that renders Thymeleaf views, talks to PostgreSQL/MongoDB, integrates with the NT SSO gateway, and pushes scheduled email alerts.

---

## Architecture Snapshot

- **Spring Boot backend** (`src/main/java/com/twm/mgmt`): layered into HTTP controllers, interceptors/aspects, application services, and persistence packages (`entity`, `dto`, `repository`, `dao`). Bootstrapped from `Application.java`.
- **Security & session control**: `SsoController` drives NT SSO callbacks via `SsoService` + `NtSsoProxy`. `RraInterceptor` (registered in `WebMvcConfig`) enforces session authorization, menu-level permission checks, and request redirect caching. `/HC/alive` and `/api/sessionTime` deliver health/session pings for infrastructure and the UI heartbeat script.
- **Application services**: examples include `AccountService`, `CustomerCareService`, `TransactionHistoryService`, `AlertFor90dayLoginService`, `LoginService`, and `SsoService`. They encapsulate validation, pagination policies, encryption, outbound REST calls, and legacy integration logic.
- **Persistence**: PostgreSQL via Spring Data JPA (`AccountEntity`, `TransactionRecordEntity`, etc.), MongoDB hooks declared in `MoDbConfig`, and hand-written DAOs where complex SQL is still used. `TransactionRecordRepository` returns DTOs for the interactive UI grids.
- **Scheduler & mail**: `AlertFor90dayLoginTasks` (cron configured in `application-*.properties`) sends reminders using `MailUtils` and templated Freemarker HTML emails (`templates/email/**`).
- **UI layer**: Thymeleaf templates under `src/main/resources/templates` share a layout shell, component fragments, and page-specific markup (`account/**`, `customer/customerCare.html`, etc.). Static assets under `static/css|js|img` provide the design system (`design-tokens.css`, `layout.css`, `sidebar.css`, `style.css`, `pages.css`) plus vendor bundles (Bootstrap, Bootstrap Table, Font Awesome, jQuery Upload File, etc.). CSS linting runs via Stylelint (`npm run lint:css`).
- **Docs/tooling**: `docs/css-refactor-summary.md` documents recent CSS cleanups. `package.json` and `stylelint.config.cjs` pin the lint toolchain. Spring DevTools (`application.properties`) watches both the canonical `src` tree and legacy `SourceFile/src`.

---

## Key User Flows

| Flow                                  | Components                                                                                                | Notes                                                                                                                                                                              |
| ------------------------------------- | --------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **NT SSO login**                      | `SsoController`, `SsoService`, `NtSsoProxy`, `layout/layout.html` heartbeat script                        | Handles token exchange, builds a `UserInfoVo` stored in session, and redirects through `/loading`. For development there is a `/sso/dummy` path that seeds fake users.             |
| **Global navigation & authorization** | `LoginController`, `RraInterceptor`, `layout/sidebar.html`, `sidebar.css`                                 | Menu definitions pulled from DB (`MenuVo`) and cached in session drive the sidebar. Interceptor gates every request and stores `redirectAfterLogin` to resume workflows post-auth. |
| **Customer care search**              | `CustomerCareController`, `CustomerCareService`, `TransactionHistoryService`, templates under `customer/` | Supports MSISDN/TWM UID/SUB ID lookups, pulls transactions + identifiers, and renders advanced grids with pagination and filtering.                                               |
| **Account & permission admin**        | `AccountController`, `AccountService`, validators under `validator/account`                               | Manage accounts, roles, menus, department status, and audit history. All posts reuse `BaseController` helpers for error handling, logging, and downloads.                          |
| **Transaction history search**        | `TransactionHistoryService`, `TransactionRecordRepository`, `pages.css`                                   | Backend streams DTOs for paged grids. AES-protected identifiers ensure lookups resolve across MSISDN/TWM UID/SUB ID combinations.                                                 |
| **90-day login alerts**               | `AlertFor90dayLoginTasks`, `AlertFor90dayLoginService`, email templates                                   | Cron `cron.alertFor90dayLogin` notifies soon-to-expire/expired accounts via templated mails pulled from `templates/email`. Runs only on specific PROD hosts (IP parity guard).     |
| **Operations & monitoring**           | Actuator (`spring-boot-starter-actuator`), `AliveController`, logback                                     | Health checks, session pings, and structured logs feed infrastructure monitors.                                                                                                    |

---

## Tech Stack & Prerequisites

| Area              | Requirement                                                                                             |
| ----------------- | ------------------------------------------------------------------------------------------------------- |
| Runtime           | JDK 17, Maven 3.9+, Node.js 18+ (for Stylelint only)                                                    |
| Datastores        | PostgreSQL 14+ (core data), MongoDB 6+ (future use), optional Redis/IndexedDB for the UI session helper |
| External services | NT SSO gateway, EPAPI endpoints, MoGW, SMTP relay                                                       |
| Build plugins     | `spring-boot-maven-plugin`, Apache POI, Jasypt (`jasypt-spring-boot-starter`)                           |
| Front-end         | Bootstrap 3, Bootstrap Table, Font Awesome, custom CSS tokens/layout/pages bundles                      |
| Tooling           | Stylelint (`npm run lint:css`), Spring DevTools, Logback                                                |

---

## Getting Started

1. **Clone & install tooling**
   ```bash
   git clone <repo-url>
   cd RRA-mgmt
   npm install        # installs Stylelint (Node 18+)
   ```
2. **Provision infrastructure**
   - PostgreSQL schema with the `account`, `role`, `transaction_record`, `user_profile`, etc. tables the repositories expect.
   - MongoDB user if you plan to enable its read paths.
   - SMTP relay and NT SSO sandbox/test credentials.
3. **Configure secrets**
   - Copy `src/main/resources/application-dev.properties` into `application-local.properties` (ignored) or export overrides via environment variables.
   - Provide a keystore under `src/main/resources/ssl/` or disable TLS locally with `server.ssl.enabled=false`.
   - Export the encryption password before starting:  
     `setx JASYPT_ENCRYPTOR_PASSWORD "<secret>"` (Windows) or `export JASYPT_ENCRYPTOR_PASSWORD=<secret>` (Unix).
4. **Compile & run**
   ```bash
   mvn clean package -DskipTests      # default POM skips tests; pass -Dmaven.test.skip=false to execute them
   java -jar target/rra-mgmt-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
   # or live reload
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```
5. **Access the console**
   - Default port `8443` with HTTPS: `https://localhost:8443/`
   - Hit `/sso/dummy` in development to seed a session before navigating to `/`.

---

## Configuration Reference

Profiles live under `src/main/resources/application-{dev,uat,prod}.properties`. The base `application.properties` only configures DevTools; you must set `spring.profiles.active`.

| Property                                       | Purpose                                                             |
| ---------------------------------------------- | ------------------------------------------------------------------- |
| `spring.datasource.*`                          | PostgreSQL URL, credentials (encrypted via Jasypt), HikariCP sizing |
| `spring.data.mongodb.*`                        | Optional Mongo consumer                                             |
| `nt.sso.url`, `nt.sso.sid`, `nt.sso.auth.type` | NT SSO proxy wiring                                                 |
| `EPAPI.master`, `EPAPI.sms`, `EPAPI.user*`     | External reward catalog APIs                                        |
| `MOGW.DomainName`                              | Mobile gateway base URL                                             |
| `spring.mail.host`, `rra.mail.from`            | SMTP delivery for alerts                                            |
| `ap.account.secrect.*`, `rc.recieve.secrect.*` | AES keys for encrypting account payloads (`AESUtil`)                |
| `cron.alertFor90dayLogin`                      | Schedule for the login alert job                                    |
| `report.path`                                  | Filesystem destination for generated reports                        |
| `server.ssl.*`                                 | PKCS12 keystore details                                             |

**Encrypted values** use `ENC(...)`. Jasypt decrypts them at runtime when `JASYPT_ENCRYPTOR_PASSWORD` is supplied. Use the same password when running Maven commands to avoid decryption failures.

---

## Build, Lint, and Test

| Task               | Command                                                               |
| ------------------ | --------------------------------------------------------------------- |
| Full build         | `mvn clean verify -Dmaven.test.skip=false`                            |
| Boot run           | `mvn spring-boot:run -Dspring-boot.run.profiles=dev`                  |
| CSS lint           | `npm run lint:css` (targets `src/main/resources/static/css/**/*.css`) |
| Dependency updates | standard Maven `versions:*` goals                                     |

Notes:

- The POM currently ships with `maven.test.skip=true` to keep builds fast; override that property locally to execute the existing unit tests in `src/test/java`.
- Spring DevTools watches `src/main/**` plus the legacy `SourceFile/src` tree. Static assets are excluded from restarts so use LiveReload or hard refresh for CSS/JS tweaks.
- Use the Stylelint config in `stylelint.config.cjs` to keep selector order and conventions consistent with the new design system (see `docs/css-refactor-summary.md`).

---

## Operations & Monitoring

- **Health checks**: `/HC/alive` returns `"O"` for load balancers. `/actuator/health` and other Actuator endpoints are available via Spring Boot (secure behind infrastructure). `/api/sessionTime` is polled by the UI (`layout/layout.html` script) to detect logged-out sessions.
- **Logging**: `logback-spring.xml` routes structured logs to the console and rotating files under `logs/`. Custom log formats live in `com.twm.mgmt.Enum.LogFormat`.
- **Email alerts**: `AlertFor90dayLoginTasks` composes Freemarker templates `email/rraWebAccountTemplate.html` and `notifyTemplate.html`. Configure SMTP + env gating before enabling in non-prod.
- **Security**: `RraInterceptor` enforces session state and page-level permissions. `SsoController` handles NT SSO interactions. AES helpers (`AESUtil`, `RandomUtil`) and OWASP ESAPI sanitize payloads before rendering.

---

## Project Layout

```
├── pom.xml
├── package.json / package-lock.json / stylelint.config.cjs
├── docs/
│   └── css-refactor-summary.md
├── src/
│   ├── main/java/com/twm/mgmt/
│   │   ├── aspect/                 # AOP logging/history
│   │   ├── config/                 # JPA, RestTemplate, MVC config
│   │   ├── constant/, enums/, Enum # Constants + error codes
│   │   ├── controller/             # MVC controllers and SSO endpoints
│   │   ├── interceptor/, validator/ # Request guards and validators
│   │   ├── model/                  # VO objects exposed to the UI
│   │   ├── persistence/            # dao/dto/entity/repository layers
│   │   ├── schedule/               # Cron tasks
│   │   ├── service/                # Application services
│   │   └── utils/, ws/, Manager/   # Shared utilities & NT SSO proxy
│   └── main/resources/
│       ├── static/css|js|img       # Custom design system + vendor bundles
│       ├── templates/              # Layout, components, feature pages
│       ├── excel/, ssl/, META-INF/ # Misc artifacts
│       └── application-*.properties
├── src/test/java/com/twm/mgmt/     # Skeleton TestApplication + persistence tests
├── SourceFile/                     # Archived legacy sources included for reference
├── rs/tokenValueRs.txt             # Sample NT SSO response payload
└── logs/, work/, target/           # Runtime/build outputs (ignored)
```

---

## Development Tips

- **Profiles**: Use `spring.profiles.active=dev` locally. Avoid committing changes to `application-prod.properties`; override via env vars instead.
- **SSL**: When running locally without a PKCS12 keystore, set `server.ssl.enabled=false` and adjust any reverse proxy configs accordingly.
- **Session UX**: The layout JS caches sidebar state in `sessionStorage` and polls `/api/sessionTime`. If you add new pages, ensure they include the base layout so these hooks stay active.
- **Front-end changes**: Keep shared styles inside `design-tokens.css`, `layout.css`, `sidebar.css`, or `style.css`. Page-specific rules belong in `pages.css`. Run `npm run lint:css` before pushing.
- **Legacy tree**: `SourceFile/src` mirrors a previous codebase. Treat it as read-only reference; new development belongs in `src/main`.

---

## Further Reading

- `docs/css-refactor-summary.md` – rationale for the current CSS architecture and lint process.
- `stylelint.config.cjs` – enforced CSS conventions.
- `logback-spring.xml` – logging categories and rolling policies.
- `KeyStoreBuilder.xml` – reference for generating the client TLS keystore (do not commit secrets).

Feel free to extend this README as new modules (e.g., API endpoints, infrastructure-as-code, or UI build pipelines) are introduced.
