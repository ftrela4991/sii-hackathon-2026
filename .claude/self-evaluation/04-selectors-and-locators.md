# Section 4 — Selectors and Locators

**Max score: 15 pts**

---

## Criteria

| # | Criterion | Status | Notes |
|---|-----------|--------|-------|
| 4.1 | Preferred locator strategy: `data-test` / `data-testid` attributes | | |
| 4.2 | No long brittle XPaths based on DOM tree structure (`/div/div[3]/span[1]/...`) | | |
| 4.3 | No dependency on marketing copy / display text for non-verified elements | | |
| 4.4 | No unjustified `nth-child` or positional index selectors | | |
| 4.5 | Locator strategy is **consistent** across the whole project (one convention) | | |
| 4.6 | For lists/tables: rows are found by **business key** (product name, order ID), not by index | | |

---

## Checklist

- [ ] Open DevTools on the target app — do the key elements have `data-test` or `data-testid`?
- [ ] If they do not, is there a stable `id` or `name` used instead?
- [ ] Run a search for `xpath` in the project — are any overly long / fragile?
- [ ] If I rename a button label in the UI, how many tests would break? (target: 0 outside of text-verification tests)

---

**Self-score:** __ / 15

**Notes:**
