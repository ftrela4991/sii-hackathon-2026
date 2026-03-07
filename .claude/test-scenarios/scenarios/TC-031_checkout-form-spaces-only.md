# TC-031: Checkout Form Rejects Spaces-Only Input

**ID:** TC-031
**Category:** ORDER
**Type:** negative
**Priority:** medium
**Tags:** @checkout @validation

---

## Feature

Whitespace-Only Input Validation at Checkout

## User Story

As a customer, filling a required checkout field with only spaces must trigger a validation error.

## Scenario

**Given** the customer is on the checkout address step
**When** the customer enters "   " (spaces only) in the "First name" field
**And** the customer submits the form
**Then** a validation error is displayed for the "First name" field
**And** the order is not placed

---

## Acceptance Criteria

- [ ] Spaces-only input is treated as empty/invalid
- [ ] Validation error appears for the affected field
- [ ] Customer cannot proceed to the next checkout step
