# TC-114: Registration Requires Acceptance of Terms and Conditions

**ID:** TC-114
**Category:** PRODUCTS_AND_API
**Type:** negative
**Priority:** high
**Tags:** @registration @validation

---

## Feature

Terms and Conditions Acceptance Enforcement

## User Story

As a visitor, attempting to register without accepting the Terms and Conditions must prevent account creation.

## Scenario

**Given** the user fills in all registration fields correctly
**But** does not check "I agree to the terms and conditions and the privacy policy"
**When** the user clicks "Save"
**Then** a validation error is shown requiring the terms checkbox to be checked
**And** the account is not created

---

## Acceptance Criteria

- [ ] Unchecked T&C checkbox blocks registration
- [ ] Validation error is shown next to or near the checkbox
- [ ] No account is created without T&C acceptance
