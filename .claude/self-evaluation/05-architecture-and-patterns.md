# Section 5 — Test Architecture and Patterns (POM)

**Max score: 15 pts**

---

## Criteria

| # | Criterion | Status | Notes |
|---|-----------|--------|-------|
| 5.1 | **Page Object Pattern** (POM) is applied to all interacted pages | | |
| 5.2 | Page Objects contain only UI operations and element locators — **no business assertions** inside them | | |
| 5.3 | Assertions that must live in PO (e.g., `assertLoaded()`) follow a clear, documented convention | | |
| 5.4 | Test spec reads like a **business scenario** with minimal implementation details visible | | |
| 5.5 | **No copy-paste** of shared steps across tests — helpers/base classes are used | | |
| 5.6 | Clear separation: Page = UI ops, Test = scenario logic + assertions, Fixtures/Clients = data setup | | |
| 5.7 | API client classes exist for setup/teardown (not mixing HTTP calls into test specs) | | |

---

## Checklist

- [ ] Read the test spec out loud — does it sound like a user story?
- [ ] Is there a `pages/` package with one class per page?
- [ ] Is there a `fixtures/` or `support/` package for API setup helpers?
- [ ] Is there a `BaseTest` class that handles driver init, teardown, and screenshot on failure?
- [ ] Are there shared helper methods (e.g., `loginAs(user)`) reused across tests?

---

**Self-score:** __ / 15

**Notes:**
