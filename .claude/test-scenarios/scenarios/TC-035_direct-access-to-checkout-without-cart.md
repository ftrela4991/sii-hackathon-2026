# TC-035: Direct URL Access to Checkout Without Cart Redirects

**ID:** TC-035
**Category:** ORDER
**Type:** negative
**Priority:** medium
**Tags:** @checkout @security

---

## Feature

Checkout URL Access Guard

## User Story

As a security measure, accessing the checkout URL directly without an active cart must be handled gracefully without a server error.

## Scenario

**Given** the customer's cart is empty
**And** the customer is not in the middle of any checkout flow
**When** the customer navigates directly to `/order`
**Then** the application does not display a 5xx error page
**And** the customer is redirected to the cart page or homepage with an informative message

---

## Acceptance Criteria

- [ ] No 5xx error on direct checkout URL access with empty cart
- [ ] Customer is redirected appropriately
- [ ] Informative message is shown (e.g. "Your cart is empty")
