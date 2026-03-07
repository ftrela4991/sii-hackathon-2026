# Section 9 — Performance and Execution Time

**Max score: qualitative (affects overall impression)**

---

## Criteria

| # | Criterion | Status | Notes |
|---|-----------|--------|-------|
| 9.1 | Each test completes in a reasonable time (no unnecessary UI navigation) | | |
| 9.2 | Browser is not restarted between tests unless isolation requires it | | |
| 9.3 | Login is performed once per suite (session reuse) when it doesn't compromise isolation | | |
| 9.4 | No redundant waits or repeated page loads within a single test | | |

---

## Checklist

- [ ] Run the full suite and record total time — is it under 5 minutes for 10–15 tests?
- [ ] Is WebDriver instantiated once per class (`@BeforeAll`) where safe to do so?
- [ ] Is the admin login performed via API / cookie injection instead of UI where possible?

---

**Self-score:** qualitative

**Notes:**
