# Section 2 — Test Data and State Preparation (Setup)

**Max score: 15 pts**

---

## Criteria

| # | Criterion | Status | Notes |
|---|-----------|--------|-------|
| 2.1 | Tests are **independent** — no manual pre-requisites needed before running | | |
| 2.2 | Data is prepared **deterministically** (same result every run) | | |
| 2.3 | Data setup is done via **API / backend calls** where possible (not UI) | | |
| 2.4 | UI-based setup is justified and minimal (only when API is unavailable) | | |
| 2.5 | All test data uses **unique identifiers** (timestamp / UUID) to avoid conflicts on parallel runs | | |
| 2.6 | **Cleanup** after each test is implemented, or its omission is explicitly justified | | |
| 2.7 | Tests have **no ordering dependency** — can be run in any order or in isolation | | |

---

## Checklist

- [ ] Can I run all tests simultaneously on two machines without conflicts?
- [ ] Can I run test #5 alone without running tests #1–#4 first?
- [ ] Does `@BeforeEach` / `@BeforeAll` fully prepare the needed state?
- [ ] Does `@AfterEach` / `@AfterAll` clean up created data?
- [ ] Are emails, usernames, order IDs always unique?

---

**Self-score:** __ / 15

**Notes:**
