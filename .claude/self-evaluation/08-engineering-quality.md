# Section 8 — Engineering Quality (Readability, Standards)

**Max score: 10 pts**

---

## Criteria

| # | Criterion | Status | Notes |
|---|-----------|--------|-------|
| 8.1 | Code style is **consistent** (formatting, naming, package structure) | | |
| 8.2 | **Maven** (or Gradle) is used as the build tool with a correct `pom.xml` / `build.gradle` | | |
| 8.3 | Method and test names **describe intent** (`shouldApplyCouponAndReduceTotal`) | | |
| 8.4 | No **magic values** — timeouts, URLs, credentials are in constants or a config file | | |
| 8.5 | **DRY** principle applied without over-abstraction (readability beats cleverness) | | |
| 8.6 | Tests follow **Arrange–Act–Assert** (or Given–When–Then) structure consistently | | |
| 8.7 | Java 21 features used appropriately where they add clarity | | |

---

## Checklist

- [ ] Is there a `TestConfig` / `AppConfig` class or `application.properties` holding all constants?
- [ ] Does `pom.xml` define the Java 21 compiler target?
- [ ] Are test method names in `camelCase` starting with `should` or `when`?
- [ ] Are there inline magic numbers or hardcoded URLs in test bodies?
- [ ] Is there a `.editorconfig` or formatter config ensuring consistent code style?

---

**Self-score:** __ / 10

**Notes:**
