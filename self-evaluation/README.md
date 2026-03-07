# Test Framework Self-Evaluation

**Project:** Selenium Test Framework for E-commerce Application
**Stack:** Java 21, Selenium (latest), Maven
**Target App:** http://145.239.29.235
**Admin Panel:** http://145.239.29.235/admin_hackathon
**Evaluation Date:** _(fill before submission)_

---

## How to Use

Before submitting, open each section file and mark every criterion as:
- `[x]` — fully satisfied
- `[~]` — partially satisfied (add a note)
- `[ ]` — not satisfied (add a note explaining the gap)

---

## Sections

| File | Section | Max Score |
|------|---------|-----------|
| [01-test-goal-and-coverage.md](01-test-goal-and-coverage.md) | Test Goal and Requirement Coverage | 10 |
| [02-test-data-and-setup.md](02-test-data-and-setup.md) | Test Data and State Preparation | 15 |
| [03-stability.md](03-stability.md) | Stability (Flakiness Prevention) | 15 |
| [04-selectors-and-locators.md](04-selectors-and-locators.md) | Selectors and Locators | 15 |
| [05-architecture-and-patterns.md](05-architecture-and-patterns.md) | Architecture and Patterns (POM) | 15 |
| [06-assertions-quality.md](06-assertions-quality.md) | Assertions — Quality and Placement | 15 |
| [07-diagnostics.md](07-diagnostics.md) | Error Handling and Diagnostics | 5 |
| [08-engineering-quality.md](08-engineering-quality.md) | Engineering Quality | 10 |
| [09-performance.md](09-performance.md) | Performance and Execution Time | qualitative |

---

## Total Score Summary

| Section | Max | Self-Score |
|---------|-----|------------|
| 1. Test Goal & Coverage | 10 | |
| 2. Test Data & Setup | 15 | |
| 3. Stability | 15 | |
| 4. Selectors & Locators | 15 | |
| 5. Architecture & POM | 15 | |
| 6. Assertions Quality | 15 | |
| 7. Diagnostics | 5 | |
| 8. Engineering Quality | 10 | |
| 9. Performance | qualitative | |
| **TOTAL** | **100** | |

---

## Pre-Submission Checklist

```
[ ] mvn clean test runs successfully from a clean checkout
[ ] No tests are marked @Disabled / @Ignore without justification
[ ] No hardcoded localhost URLs — all pointing to 145.239.29.235
[ ] README.md explains how to run the suite
[ ] Screenshots directory is in .gitignore (but the mechanism exists in code)
[ ] No credentials committed to git (use environment variables or a .env file)
[ ] Suite passes at least 2 consecutive runs without a flaky failure
```

---

## Known Gaps and Accepted Trade-offs

_Document intentional deviations here, with justification._

| Gap | Reason / Justification |
|-----|------------------------|
| | |

---

## Architecture Decision Record (ADR)

| Decision | Rationale |
|----------|-----------|
| Build tool: Maven | Standard, widely known, good IDE support |
| Test runner: JUnit 5 | Native support for `@BeforeAll`, `@TestMethodOrder`, extensions |
| Assertions: AssertJ | Fluent API, readable failure messages |
| Waits: `WebDriverWait` + `ExpectedConditions` | Explicit, configurable, no `Thread.sleep` |
| Logging: SLF4J + Logback | No `println`, structured output, CI-friendly |
| Screenshot: JUnit 5 `TestWatcher` extension | Automatic on failure, zero boilerplate in tests |
| Config: `.properties` file + system property overrides | Easy CI parameterisation |
