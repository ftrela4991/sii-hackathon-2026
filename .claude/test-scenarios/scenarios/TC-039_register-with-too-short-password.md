# TC-039: Registration Fails with Password Below Minimum Length

**ID:** TC-039
**Category:** ACCOUNT
**Type:** negative
**Priority:** high
**Tags:** @registration @validation

---

## Feature

Minimum Password Length Enforcement

## User Story

As a visitor, entering a password shorter than 8 characters must be rejected during registration.

## Scenario

**Given** the user is on the registration page
**When** the user fills in all fields correctly except Password, which is set to "Ab1!" (4 characters)
**And** the user clicks "Save"
**Then** a validation error is shown indicating the password must be at least 8 characters long
**And** the account is not created

---

## Acceptance Criteria

- [ ] 4-character password is rejected
- [ ] Validation error references the minimum length requirement
- [ ] The password strength indicator shows insufficient strength
- [ ] No account is created
