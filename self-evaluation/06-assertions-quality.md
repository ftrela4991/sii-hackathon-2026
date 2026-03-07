# Section 6 — Assertions — Quality and Placement

**Max score: 15 pts**

---

## Criteria

| # | Criterion | Status | Notes |
|---|-----------|--------|-------|
| 6.1 | Assertions are placed **after actions that change state**, not at random points | | |
| 6.2 | Assertions verify **business outcome** (correct price, order confirmed), not just element presence | | |
| 6.3 | Assertions are resilient to minor UI changes (verify values/data, not layout or CSS classes) | | |
| 6.4 | Assertion failure messages are **descriptive** (state what was expected vs. actual, with context) | | |
| 6.5 | No over-asserting (no assertions checking label text that isn't the business intent) | | |
| 6.6 | Assertions compare **business data** (price, quantity, status), not HTML attributes or element states | | |

---

## Checklist

- [ ] For each assertion: what happens to the user if this assertion fails? Is it a real defect?
- [ ] Does every `assertEquals` / `assertThat` have a meaningful message?
- [ ] Are assertions using domain language? (`assertThat(cart.totalPrice()).isEqualTo(expectedTotal)`)
- [ ] Count assertions per test — more than 10 in a single test is a code smell.

---

**Self-score:** __ / 15

**Notes:**
