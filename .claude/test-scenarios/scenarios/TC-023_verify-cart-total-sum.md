# TC-023: Verify Cart Total Sum is Correct

**ID:** TC-023
**Category:** ORDER
**Type:** positive
**Priority:** high
**Tags:** @cart @pricing

---

## Feature

Cart Total Calculation Accuracy

## User Story

As a customer, the cart total must always equal the exact sum of all line item prices so I can trust the displayed amount.

## Background

Products with known prices are available: Product A at £20.00, Product B at £15.00.

## Scenario

**Given** the customer adds Product A at "£20.00" with quantity 2 to the cart
**And** the customer adds Product B at "£15.00" with quantity 1 to the cart
**When** the customer views the cart page
**Then** the subtotal for Product A is "£40.00"
**And** the subtotal for Product B is "£15.00"
**And** the cart grand total is "£55.00"

---

## Acceptance Criteria

- [ ] Each line item subtotal = unit price × quantity
- [ ] Grand total = sum of all line item subtotals
- [ ] No rounding errors for whole-number amounts
