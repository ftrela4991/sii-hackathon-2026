# TC-029: Checkout Form Validates Required Fields

**ID:** TC-029
**Category:** ORDER
**Type:** negative
**Priority:** high
**Tags:** @checkout @validation

---

## Feature

Required Field Validation at Checkout

## User Story

As a customer, leaving required checkout fields empty or using invalid input must trigger visible validation messages.

## Scenario

**Given** the customer is on the checkout address step with an empty form
**When** the customer submits the form without filling in any required fields
**Then** validation error messages appear for all empty required fields
**When** the customer enters an email with a trailing space (e.g. "user@test.com ")
**Then** the form either trims the space or displays a format validation error

---

## Acceptance Criteria

- [ ] All required field errors appear simultaneously on empty submit
- [ ] Trailing space in email is handled (trimmed or rejected)
- [ ] Customer cannot proceed to the next step while errors exist
