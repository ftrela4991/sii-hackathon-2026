# TC-026: Checkout with Empty Cart Is Blocked

**ID:** TC-026
**Category:** ORDER
**Type:** negative
**Priority:** high
**Tags:** @cart @checkout

---

## Feature

Empty Cart Checkout Guard

## User Story

As a customer, attempting to access checkout with an empty cart should prevent me from proceeding.

## Scenario

**Given** the customer's cart is empty
**When** the customer navigates directly to the checkout URL `/order`
**Then** the customer is redirected away from the checkout page
**And** an appropriate message is shown (e.g. "Your cart is empty") or the customer is redirected to the cart page

---

## Acceptance Criteria

- [ ] Direct URL access to checkout with empty cart does not allow order placement
- [ ] Customer is redirected or shown an informative message
- [ ] No 5xx server error occurs
