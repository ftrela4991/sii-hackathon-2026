# TC-032: Checkout Form Rejects Excessively Long Input

**ID:** TC-032
**Category:** ORDER
**Type:** negative
**Priority:** low
**Tags:** @checkout @validation @boundary

---

## Feature

Maximum Field Length Enforcement at Checkout

## User Story

As a customer, entering strings exceeding the maximum allowed length in checkout fields must be rejected or truncated safely.

## Scenario

**Given** the customer is on the checkout address step
**When** the customer enters a string of 500 characters in the "Last name" field
**And** the customer submits the form
**Then** a validation error or character limit message is displayed
**And** the order is not placed

---

## Acceptance Criteria

- [ ] 500-character last name is rejected or truncated
- [ ] An informative validation message is shown
- [ ] No server error (5xx) occurs on submission
- [ ] No order is created with malformed data
