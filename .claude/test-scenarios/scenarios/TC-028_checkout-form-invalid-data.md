# TC-028: Checkout Form Rejects Invalid Data

**ID:** TC-028
**Category:** ORDER
**Type:** negative
**Priority:** high
**Tags:** @checkout @validation

---

## Feature

Checkout Form Email Validation

## User Story

As a customer, submitting the checkout form with an invalid email format should show a validation error and prevent proceeding.

## Background

The customer is on the personal information step of the checkout flow.

## Scenario

**Given** the customer is on the checkout page at the personal information step
**When** the customer enters "notanemail" in the email field
**And** the customer clicks "Continue"
**Then** a validation error is displayed next to the email field
**And** the customer is not advanced to the next checkout step

---

## Acceptance Criteria

- [ ] Validation error appears for the invalid email field
- [ ] Customer remains on the same checkout step
- [ ] Error message is descriptive (e.g. "Invalid email address")
- [ ] No order is created
