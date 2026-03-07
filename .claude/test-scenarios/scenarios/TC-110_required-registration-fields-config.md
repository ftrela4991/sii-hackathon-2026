# TC-110: Configure Required Registration Fields

**ID:** TC-110
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @registration

---

## Feature

Required Fields Configuration Enforcement

## User Story

As a shop admin, I want to verify that required fields (email, first name, last name) are enforced during registration.

## Scenario

**Given** the shop is configured to require: email, first name, and last name
**When** a customer attempts to register without filling in any of these fields
**Then** a validation error is shown for each missing required field

---

## Acceptance Criteria

- [ ] Email validation error appears when email is missing
- [ ] First name validation error appears when first name is missing
- [ ] Last name validation error appears when last name is missing
- [ ] No account is created when required fields are missing
