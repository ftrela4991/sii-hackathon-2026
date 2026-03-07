# TC-063: Product with Decimal Price (£19.99 and £0.01)

**ID:** TC-063
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @product @pricing @boundary

---

## Feature

Decimal Price Accuracy

## User Story

As a shop admin, I want decimal prices to be stored and displayed exactly without rounding errors.

## Scenario

**Given** the admin creates Product A with price "19.99" and Product B with price "0.01"
**When** a customer views both product detail pages
**Then** Product A displays "£19.99"
**And** Product B displays "£0.01"
**When** the customer adds both products to the cart
**Then** the cart total is "£20.00"

---

## Acceptance Criteria

- [ ] £19.99 is not rounded to £20.00 or £19.9
- [ ] £0.01 is not rounded to £0.00
- [ ] Cart total equals exact sum: 19.99 + 0.01 = 20.00
- [ ] Both products are deleted via API after the test
