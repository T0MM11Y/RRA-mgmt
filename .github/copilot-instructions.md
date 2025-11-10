<!-- Copilot / AI agent guidance for the rra-mgmt repository -->

# Quick context

- This is a Spring Boot 3.4.x web application (Java 17). Main build is Maven (`pom.xml`).
- Server-side views are Thymeleaf templates in `src/main/resources/templates/` and a shared layout at `templates/layout/layout.html`.
- Controllers live in `src/main/java/com/twm/mgmt/controller`; services in `.../service`; persistence/DAO in `.../persistence`.

# How the app is wired (big picture)

- HTTP MVC: controllers return `ModelAndView` for pages and `ResponseEntity<?>` for AJAX JSON responses (see `TransactionHistoryController`).
- The DAO layer uses custom SQL builders in `persistence/dao/impl/TransactionRecordDaoImpl` (not purely Spring Data JPA queries). Follow `composeSql` / `composeParams` when changing filters or adding columns.
- Export logic (CSV/XLSX) is implemented in `TransactionHistoryService` (methods `exportToCsv` / `exportToExcel`) and uses Apache Commons CSV / Apache POI. Front-end may post to `/customer/transactionHistory/export/*` (logs reference this path).

# Developer workflows (build / run / debug)

- Build locally: `mvn -DskipTests package` (pom sets `maven.test.skip=true` by default). To run tests, remove `-DskipTests` or edit the pom property.
- Run in dev mode with hot reload: `mvn spring-boot:run`. DevTools is enabled in `src/main/resources/application.properties`.
- Run packaged jar: `java -jar target/rra-mgmt-0.0.1-SNAPSHOT.jar` after a successful `mvn package`.

# Project-specific conventions & patterns (for an AI agent)

- Templates: use Thymeleaf layout dialect. Pages declare `layout:decorate="~{layout/layout}"` and place content in `layout:fragment="content"`.
- Inline Thymeleaf JS: the project uses Thymeleaf template expressions in JS blocks. To keep editor JS parsers happy, prefer the Thymeleaf inline-comment form when emitting URLs into JavaScript:

  callAjax(/_[[@{/customer/transactionHistory}]]_/, payload, renderTransactionTable);

  Using `/*[[@{...}]]*/` prevents VS Code / eslint from treating `@` as a JS decorator.

- Session/state: controllers save filter state in HTTP session keys (see `SESSION_CONDITION_KEY` in `TransactionHistoryController`). If you change the session contract, update uses in controller + front-end logic.
- Pagination + sorting: service layer normalizes paging (see `normalizePagination` in `TransactionHistoryService`) and returns a `QueryResultVo` consumed by front-end bootstrap table.

# Integration points & external dependencies

- Jasypt is included (`jasypt-spring-boot-starter`) — secrets/properties may be encrypted. Check `application-*.properties` and how Jasypt is configured for decrypting runtime properties.
- Export libraries: `commons-csv`, `poi`/`poi-ooxml`, and `jxls` are used for CSV/XLSX export. Editing export behaviour should be done in `TransactionHistoryService` and the DAO `findForExport` query.
- ESAPI is present for security utilities. Be careful when altering input/output handling.

# Files to examine for changes or to implement new features

- Controller patterns: `src/main/java/com/twm/mgmt/controller/CustomerCareController.java` (unified search and transaction history)
- Service + export: `src/main/java/com/twm/mgmt/service/TransactionHistoryService.java`
- Customer care service: `src/main/java/com/twm/mgmt/service/CustomerCareService.java` (user identification and transaction search)
- DAO SQL builder: `src/main/java/com/twm/mgmt/persistence/dao/impl/TransactionRecordDaoImpl.java`
- Templates: `src/main/resources/templates/customer/customerCare.html` (unified single-page interface with identifier tabs) and shared layout `src/main/resources/templates/layout/layout.html` (contains global JS helpers such as `initFileUploadWatcher`).

# Common gotchas discovered in this repo

- Thymeleaf inline expressions like `[[@{/path}]]` inside JS will make VS Code show "Decorators are not valid here". Use `/*[[@{/path}]]*/` or the `/*[[...]]*/` inline comment form.
- The project frequently uses server-side session to store user filter state — stateless refactors will need careful migration.
- DAO uses hand-crafted SQL. Adding fields requires updating `composeSql`, `composeParams` and mapping DTOs.

# If you need more info

- Tell me which feature or file you want to change (controller, DAO, template, or export). I can produce targeted diffs (patches) and also update tests or add a small integration check.
