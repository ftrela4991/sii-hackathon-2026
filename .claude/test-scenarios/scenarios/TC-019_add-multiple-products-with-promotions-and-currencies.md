# TC-019: Add Multiple Products with Promotions and Different Currencies

**ID:** TC-019
**Category:** ORDER
**Type:** positive
**Priority:** medium
**Tags:** @cart @discount @currency

---

## Feature

Mixed Cart with Promotions and Currency

## User Story

As a customer, I want to add multiple products (some on promotion) and see the correct combined cart total.

## Background

Product A: £30.00, no discount. Product B: £50.00, 10% discount coupon available.

## Scenario

**Given** the customer's cart is empty
**When** the customer adds Product A (£30.00, no discount) to the cart
**And** the customer adds Product B (£50.00, 10% discount) to the cart
**And** the customer applies the 10% discount coupon
**Then** the cart shows Product A at "£30.00"
**And** the cart shows Product B at "£45.00" (after 10% off)
**And** the cart total is "£75.00"

---

## Acceptance Criteria

- [ ] Each product is listed separately with the correct price
- [ ] Discount applies only to eligible product(s)
- [ ] Cart total equals the sum of all discounted line items
