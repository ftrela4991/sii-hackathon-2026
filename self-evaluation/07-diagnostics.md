# Section 7 — Error Handling and Diagnostics

**Max score: 5 pts**

---

## Criteria

| # | Criterion | Status | Notes |
|---|-----------|--------|-------|
| 7.1 | **Logging** is present at key steps (action taken + relevant data), using a proper logger (not `System.out.println`) | | |
| 7.2 | **Screenshots** are automatically taken on test failure and saved with a meaningful filename | | |
| 7.3 | Screenshots are saved to a configurable output directory | | |
| 7.4 | Log messages include the test name and relevant business context (e.g., user email, product SKU) | | |

---

## Checklist

- [ ] Is there a JUnit 5 `TestWatcher` (or equivalent) that captures screenshots on failure?
- [ ] Search for `System.out.println` — it must be absent (use SLF4J / Log4j2 / java.util.logging)
- [ ] Do screenshot filenames include test name + timestamp?
- [ ] Are logs readable in CI output without needing to open screenshots?

---

**Self-score:** __ / 5

**Notes:**
