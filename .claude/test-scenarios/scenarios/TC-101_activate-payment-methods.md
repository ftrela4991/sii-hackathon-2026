# TC-101: Activate at Least Two Payment Methods

**ID:** TC-101
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** high
**Tags:** @admin @payment

---

## Feature

Payment Method Activation

## User Story

As a shop admin, I want to enable at least two payment methods so customers have a choice at checkout.

## Scenario

**Given** the admin activates "Pay by Check" and "Cash on Delivery" in the payment modules section
**When** a customer reaches the payment step at checkout
**Then** at least two payment options are listed
**And** the customer can select either option and complete the order

---

## Acceptance Criteria

- [ ] Both payment methods are visible at checkout
- [ ] Each method can be selected
- [ ] Order completes successfully with each method
