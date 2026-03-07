# TC-112: Email Format Validation on Registration and Checkout

**ID:** TC-112
**Category:** PRODUCTS_AND_API
**Type:** negative
**Priority:** medium
**Tags:** @validation @registration

---

## Feature

Email Format Validation

## User Story

As a visitor, entering an email without "@" or a valid domain must trigger a validation error immediately.

## Scenario

**Given** the user is on the registration page
**When** the user enters "invalidemail" (no @) in the email field and submits
**Then** a validation error is shown: "Invalid email address"
**When** the user enters "user@" (no domain) in the email field and submits
**Then** a validation error is shown

---

## Acceptance Criteria

- [ ] "invalidemail" triggers a validation error
- [ ] "user@" triggers a validation error
- [ ] Validation error appears next to the email field
- [ ] No account is created with invalid email formats
