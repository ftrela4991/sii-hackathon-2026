# Section 3 — Stability (Flakiness Prevention)

**Max score: 15 pts**

---

## Criteria

| # | Criterion | Status | Notes |
|---|-----------|--------|-------|
| 3.1 | No `Thread.sleep()` calls (or each one has a written justification comment) | | |
| 3.2 | **Explicit waits** (`WebDriverWait` + `ExpectedConditions`) used instead of implicit waits | | |
| 3.3 | Waits are on **element state** (clickable, visible, text present), not on time | | |
| 3.4 | Tests pass on a slow environment (e.g., 3× slower network / CPU) | | |
| 3.5 | Animations and page transitions are handled (wait for element to stop moving or become stable) | | |
| 3.6 | AJAX / dynamic content is properly awaited before assertions | | |

---

## Checklist

- [ ] Search the codebase for `Thread.sleep` — every occurrence has a justification comment?
- [ ] Search for `implicitlyWait` — is it absent or set only globally with a low value?
- [ ] Are all waits using `WebDriverWait` with a configurable timeout constant?
- [ ] Did I test the suite at least once on a throttled / slow connection?

---

**Self-score:** __ / 15

**Notes:**
